package com.ds.jlptnoteapp.model.enums;


public enum PosEnum {

    MEISHI(1L, "名詞", "Noun / kata benda"),
    DAIMEISHI(2L, "代名詞", "Pronoun / kata ganti"),
    DOSHI_ICHIDAN(3L, "動詞（一段）", "Verb Ichidan"),
    DOSHI_GODAN(4L, "動詞（五段）", "Verb Godan"),
    DOSHI_IRREGULAR(5L, "動詞（不規則）", "Irregular Verb"),
    KEIYOSHI(6L, "形容詞", "i-adjective"),
    KEIYODOUSHI(7L, "形容動詞", "na-adjective"),
    FUKUSHI(8L, "副詞", "Adverb"),
    JOSHI(9L, "助詞", "Particle"),
    JODOUSHI(10L, "助動詞", "Auxiliary Verb"),
    SETSUZOKUSHI(11L, "接続詞", "Conjunction"),
    RENTAISHI(12L, "連体詞", "Prenominal Adjectival"),
    KANDOUSHI(13L, "感動詞", "Interjection"),
    SETTOUJI(14L, "接頭辞", "Prefix"),
    SETSUBIJI(15L, "接尾辞", "Suffix");

    private final Long id;
    private final String name;
    private final String note;

    PosEnum(Long id, String name, String note) {
        this.id = id;
        this.name = name;
        this.note = note;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getNote() { return note; }

    // Optional helper: cari PosEnum dari id
    public static PosEnum fromId(Long id) {
        for (PosEnum pos : values()) {
            if (pos.id.equals(id)) return pos;
        }
        return null;
    }

    // Optional helper: cari PosEnum dari name
    public static PosEnum fromName(String name) {
        for (PosEnum pos : values()) {
            if (pos.name.equals(name)) return pos;
        }
        return null;
    }
}
