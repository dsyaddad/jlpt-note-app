package com.ds.jlptnoteapp.service;

import com.ds.jlptnoteapp.model.dto.ConjugationRequestDto;
import com.ds.jlptnoteapp.model.dto.ConjugationResultDto;
import com.ds.jlptnoteapp.model.dto.InflectedDto;
import com.ds.jlptnoteapp.model.dto.KatsuyouDto;
import com.ds.jlptnoteapp.model.entity.ConjugationOverride;
import com.ds.jlptnoteapp.model.entity.Lemma;
import com.ds.jlptnoteapp.model.enums.*;
import com.ds.jlptnoteapp.model.repository.ConjugationOverrideRepository;
import com.ds.jlptnoteapp.model.repository.LemmaRepository;
import com.ds.jlptnoteapp.model.transformer.LemmaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConjugationService {

    private final LemmaRepository lemmaRepo;
    private final ConjugationOverrideRepository overrideRepo;
    private final LemmaMapper lemmaMapper;

    public List<KatsuyouDto> getAllSample(){
        List<Lemma> sampleLemmas = lemmaRepo.findAll();

        return sampleLemmas.stream()
                .map(lemma -> {
                    ConjugationResultDto result = buildResult(lemma);
                    Map<String,String> formMap = result.getForms().stream()
                            .collect(Collectors.toMap(
                                    f -> f.getFormType().name(),   // key = String
                                    f -> f.getSurface(),
                                    (a,b) -> a,
                                    LinkedHashMap::new
                            ));
                    return KatsuyouDto.builder()
                            .lemmaDto(lemmaMapper.toDto(lemma))
                            .result(result)
                            .formMap(formMap)
                            .build();
                })
                .toList();
    }

    // ---------- API publik ----------
    public ConjugationResultDto generateByLemmaId(Long lemmaId) {
        Lemma l = lemmaRepo.findById(lemmaId)
                .orElseThrow(() -> new IllegalArgumentException("Lemma not found: " + lemmaId));
        return buildResult(l);
    }

    public ConjugationResultDto previewByInput(ConjugationRequestDto req) {
        validate(req);
        Lemma l = Lemma.builder()
                .id(null)
                .headwordKana(req.getHeadwordKana())
                .kanji(req.getKanji())
                .posType(req.getPosType())
                .godanEndingKana(req.getGodanEndingKana())
                .meaning(null)
                .build();
        // Untuk preview, override diambil dari lemma yang cocok (jika ada), atau kosong
        List<ConjugationOverride> overrides = lemmaRepo.findByHeadwordKana(req.getHeadwordKana())
                .map(existing -> overrideRepo.findByLemma_Id(existing.getId()))
                .orElseGet(List::of);
        return buildResult(l, overrides);
    }

    // ---------- Core ----------
    private ConjugationResultDto buildResult(Lemma l) {
        List<ConjugationOverride> overrides =
                (l.getId() != null) ? overrideRepo.findByLemma_Id(l.getId()) : List.of();
        return buildResult(l, overrides);
    }

    private ConjugationResultDto buildResult(Lemma l, List<ConjugationOverride> overrides) {
        Map<FormType, InflectedDto> generated = switch (l.getPosType()) {
            case DOSHI_GODAN -> genGodan(l);
            case DOSHI_ICHIDAN -> genIchidan(l);
            case KEIYOSHI -> genIAdj(l);
            case KEIYODOUSHI -> mapOf(
                    inf(FormType.CONDITIONAL, l.getHeadwordKana() + "なら", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.BASIC, null),
                    inf(FormType.CONDITIONAL_NEG, l.getHeadwordKana() + "でなければ", Politeness.PLAIN, Polarity.NEGATIVE, Voice.BASIC, null)
            );
            case MEISHI -> mapOf(
                    inf(FormType.CONDITIONAL, l.getHeadwordKana() + "なら", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.BASIC, null),
                    inf(FormType.CONDITIONAL_NEG, l.getHeadwordKana() + "でなければ", Politeness.PLAIN, Polarity.NEGATIVE, Voice.BASIC, null)
            );
            case DOSHI_IRREGULAR -> new LinkedHashMap<>(); // diisi lewat logic khusus + overrides
        };

        // Irregular logic untuk compound 〜する / 〜くる
        if (l.getPosType() == PosType.DOSHI_IRREGULAR && l.getHeadwordKana() != null) {
            String w = l.getHeadwordKana();
            if (w.endsWith("する")) generated.putAll(genSuruCompound(w));
            if (w.endsWith("くる")) generated.putAll(genKuruCompound(w));
        }

        // Aturan khusus 行く -> いって/いった (kalau kamu simpan sebagai godan+ending く)
        if (l.getPosType() == PosType.DOSHI_GODAN && "いく".equals(l.getHeadwordKana())) {
            generated.put(FormType.TE, decorate(FormType.TE, "いって"));
            generated.put(FormType.TA, decorate(FormType.TA, "いった"));
        }

        // Apply overrides dari DB
        if (overrides != null) {
            for (ConjugationOverride ov : overrides) {
                generated.put(ov.getFormType(), decorate(ov.getFormType(), ov.getSurface()));
            }
        }

        // Sort stabil by FormType name
        List<InflectedDto> list = new ArrayList<>(generated.values());
        list.sort(Comparator.comparing(i -> i.getFormType().name()));

        return ConjugationResultDto.builder()
                .lemma(lemmaMapper.toDto(l))
                .forms(list)
                .build();
    }

    // ---------- Generators ----------
    private Map<FormType, InflectedDto> genGodan(Lemma l) {
        String w = l.getHeadwordKana();
        String end = l.getGodanEndingKana();
        if (!StringUtils.hasText(end))
            throw new IllegalArgumentException("godan_ending_kana wajib untuk DOSHI_GODAN: " + w);

        Row row = GODAN.get(end);
        if (row == null) throw new IllegalArgumentException("Ending godan tak dikenal: " + end);

        String stem = w.substring(0, w.length() - 1);
        String stemA = stem + row.a;
        String stemI = stem + row.i;
        String stemE = stem + row.e;
        String stemO = stem + row.o;

        String te = switch (end) {
            case "う", "つ", "る" -> stem + "っ" + "て";
            case "む", "ぶ", "ぬ" -> stem + "ん" + "で";
            case "く" -> stem + "い" + "て";
            case "ぐ" -> stem + "い" + "で";
            case "す" -> stem + "し" + "て";
            default -> throw new IllegalStateException();
        };
        String ta = te.replace("て", "た").replace("で", "だ");

        Map<FormType, InflectedDto> m = new LinkedHashMap<>();
        m.put(FormType.DICTIONARY, inf(FormType.DICTIONARY, w, Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.BASIC, Tense.NONPAST));
        m.put(FormType.MASU,       inf(FormType.MASU, stemI + "ます", Politeness.POLITE, Polarity.AFFIRMATIVE, Voice.BASIC, Tense.NONPAST));
        m.put(FormType.TE,         inf(FormType.TE, te, null, null, Voice.BASIC, null));
        m.put(FormType.TA,         inf(FormType.TA, ta, Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.BASIC, Tense.PAST));
        m.put(FormType.NAI,        inf(FormType.NAI, end.equals("う") ? stem + "わない" : stemA + "ない", Politeness.PLAIN, Polarity.NEGATIVE, Voice.BASIC, null));
        m.put(FormType.POTENTIAL,  inf(FormType.POTENTIAL, stemE + "る", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.BASIC, Tense.NONPAST));
        m.put(FormType.VOLITIONAL, inf(FormType.VOLITIONAL, stemO + "う", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.BASIC, Tense.NONPAST));
        m.put(FormType.CONDITIONAL,inf(FormType.CONDITIONAL, stemE + "ば", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.BASIC, null));
        m.put(FormType.CONDITIONAL_NEG, inf(FormType.CONDITIONAL_NEG, (end.equals("う") ? stem + "わなければ" : stemA + "なければ"), Politeness.PLAIN, Polarity.NEGATIVE, Voice.BASIC, null));
        m.put(FormType.IMPERATIVE, inf(FormType.IMPERATIVE, stemE, Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.BASIC, null));
        m.put(FormType.PASSIVE,    inf(FormType.PASSIVE, stemA + "れる", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.PASSIVE, Tense.NONPAST));
        m.put(FormType.CAUSATIVE,  inf(FormType.CAUSATIVE, stemA + "せる", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.CAUSATIVE, Tense.NONPAST));
        m.put(FormType.CAUSATIVE_PASSIVE, inf(FormType.CAUSATIVE_PASSIVE, stemA + "せられる", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.CAUSATIVE_PASSIVE, Tense.NONPAST));
        return m;
    }

    private Map<FormType, InflectedDto> genIchidan(Lemma l) {
        String w = l.getHeadwordKana();
        if (!w.endsWith("る")) {
            // bukan fatal, tapi logikanya ichidan hampir selalu -る
        }
        String stem = w.substring(0, w.length() - 1);
        Map<FormType, InflectedDto> m = new LinkedHashMap<>();
        m.put(FormType.DICTIONARY, inf(FormType.DICTIONARY, w, Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.BASIC, Tense.NONPAST));
        m.put(FormType.MASU,       inf(FormType.MASU, stem + "ます", Politeness.POLITE, Polarity.AFFIRMATIVE, Voice.BASIC, Tense.NONPAST));
        m.put(FormType.TE,         inf(FormType.TE, stem + "て", null, null, Voice.BASIC, null));
        m.put(FormType.TA,         inf(FormType.TA, stem + "た", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.BASIC, Tense.PAST));
        m.put(FormType.NAI,        inf(FormType.NAI, stem + "ない", Politeness.PLAIN, Polarity.NEGATIVE, Voice.BASIC, null));
        m.put(FormType.POTENTIAL,  inf(FormType.POTENTIAL, stem + "られる", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.BASIC, Tense.NONPAST));
        m.put(FormType.VOLITIONAL, inf(FormType.VOLITIONAL, stem + "よう", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.BASIC, Tense.NONPAST));
        m.put(FormType.CONDITIONAL,inf(FormType.CONDITIONAL, stem + "れば", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.BASIC, null));
        m.put(FormType.CONDITIONAL_NEG, inf(FormType.CONDITIONAL_NEG, stem + "なければ", Politeness.PLAIN, Polarity.NEGATIVE, Voice.BASIC, null));
        m.put(FormType.IMPERATIVE, inf(FormType.IMPERATIVE, stem + "ろ", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.BASIC, null));
        m.put(FormType.PASSIVE,    inf(FormType.PASSIVE, stem + "られる", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.PASSIVE, Tense.NONPAST));
        m.put(FormType.CAUSATIVE,  inf(FormType.CAUSATIVE, stem + "させる", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.CAUSATIVE, Tense.NONPAST));
        m.put(FormType.CAUSATIVE_PASSIVE, inf(FormType.CAUSATIVE_PASSIVE, stem + "させられる", Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.CAUSATIVE_PASSIVE, Tense.NONPAST));
        return m;
    }

    private Map<FormType, InflectedDto> genIAdj(Lemma l) {
        String w = l.getHeadwordKana();
        String cond, condNeg;
        if ("いい".equals(w)) {
            cond = "よければ";
            condNeg = "よくなければ";
        } else {
            String stem = w.endsWith("い") ? w.substring(0, w.length() - 1) : w;
            cond = stem + "ければ";
            condNeg = stem + "くなければ";
        }
        return mapOf(
                inf(FormType.CONDITIONAL, cond, Politeness.PLAIN, Polarity.AFFIRMATIVE, Voice.BASIC, null),
                inf(FormType.CONDITIONAL_NEG, condNeg, Politeness.PLAIN, Polarity.NEGATIVE, Voice.BASIC, null)
        );
    }

    private Map<FormType, InflectedDto> genSuruCompound(String w) {
        // w diakhiri "する"
        String base = w.substring(0, w.length() - 2);
        return mapOf(
                decorate(FormType.DICTIONARY, base + "する"),
                decorate(FormType.MASU,       base + "します"),
                decorate(FormType.TE,         base + "して"),
                decorate(FormType.TA,         base + "した"),
                decorate(FormType.NAI,        base + "しない"),
                decorate(FormType.POTENTIAL,  base + "できる"),
                decorate(FormType.VOLITIONAL, base + "しよう"),
                decorate(FormType.CONDITIONAL,base + "すれば"),
                decorate(FormType.CONDITIONAL_NEG, base + "しなければ"),
                decorate(FormType.IMPERATIVE, base + "しろ"),
                decorate(FormType.PASSIVE,    base + "される"),
                decorate(FormType.CAUSATIVE,  base + "させる"),
                decorate(FormType.CAUSATIVE_PASSIVE, base + "させられる")
        );
    }

    private Map<FormType, InflectedDto> genKuruCompound(String w) {
        // w diakhiri "くる"
        String base = w.substring(0, w.length() - 2);
        return mapOf(
                decorate(FormType.DICTIONARY, base + "くる"),
                decorate(FormType.MASU,       base + "きます"),
                decorate(FormType.TE,         base + "きて"),
                decorate(FormType.TA,         base + "きた"),
                decorate(FormType.NAI,        base + "こない"),
                decorate(FormType.POTENTIAL,  base + "こられる"),
                decorate(FormType.VOLITIONAL, base + "こよう"),
                decorate(FormType.CONDITIONAL,base + "くれば"),
                decorate(FormType.CONDITIONAL_NEG, base + "こなければ"),
                decorate(FormType.IMPERATIVE, base + "こい"),
                decorate(FormType.PASSIVE,    base + "こられる"),
                decorate(FormType.CAUSATIVE,  base + "こさせる"),
                decorate(FormType.CAUSATIVE_PASSIVE, base + "こさせられる")
        );
    }

    // ---------- Utils ----------
    private static InflectedDto inf(FormType t, String s, Politeness p, Polarity pl, Voice v, Tense te) {
        return InflectedDto.builder().formType(t).surface(s).politeness(p).polarity(pl).voice(v).tense(te).build();
    }
    private static InflectedDto decorate(FormType t, String s) {
        Tense te = (t == FormType.TA) ? Tense.PAST :
                (t == FormType.DICTIONARY || t == FormType.MASU || t == FormType.POTENTIAL
                        || t == FormType.VOLITIONAL || t == FormType.PASSIVE
                        || t == FormType.CAUSATIVE || t == FormType.CAUSATIVE_PASSIVE) ? Tense.NONPAST : null;
        Voice v = switch (t) {
            case PASSIVE -> Voice.PASSIVE;
            case CAUSATIVE -> Voice.CAUSATIVE;
            case CAUSATIVE_PASSIVE -> Voice.CAUSATIVE_PASSIVE;
            default -> Voice.BASIC;
        };
        Politeness pol = (t == FormType.MASU) ? Politeness.POLITE : Politeness.PLAIN;
        Polarity po = (t == FormType.NAI || t == FormType.CONDITIONAL_NEG) ? Polarity.NEGATIVE : Polarity.AFFIRMATIVE;
        return InflectedDto.builder().formType(t).surface(s).politeness(pol).polarity(po).voice(v).tense(te).build();
    }
    @SafeVarargs
    private static Map<FormType, InflectedDto> mapOf(InflectedDto... arr) {
        Map<FormType, InflectedDto> m = new LinkedHashMap<>();
        for (InflectedDto x : arr) m.put(x.getFormType(), x);
        return m;
    }

    // baris a/i/u/e/o untuk 9 akhiran 五段
    private record Row(String a, String i, String u, String e, String o) {}
    private static final Map<String, Row> GODAN = Map.of(
            "う", new Row("わ","い","う","え","お"),
            "つ", new Row("た","ち","つ","て","と"),
            "る", new Row("ら","り","る","れ","ろ"),
            "む", new Row("ま","み","む","め","も"),
            "ぶ", new Row("ば","び","ぶ","べ","ぼ"),
            "ぬ", new Row("な","に","ぬ","ね","の"),
            "く", new Row("か","き","く","け","こ"),
            "ぐ", new Row("が","ぎ","ぐ","げ","ご"),
            "す", new Row("さ","し","す","せ","そ")
    );

    private static void validate(ConjugationRequestDto req) {
        if (!StringUtils.hasText(req.getHeadwordKana()))
            throw new IllegalArgumentException("headwordKana wajib");
        if (req.getPosType() == null)
            throw new IllegalArgumentException("posType wajib");
        if (req.getPosType() == PosType.DOSHI_GODAN &&
                !StringUtils.hasText(req.getGodanEndingKana()))
            throw new IllegalArgumentException("godanEndingKana wajib untuk DOSHI_GODAN");
    }
}