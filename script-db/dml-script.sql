-- Pastikan session pakai utf8mb4
/*!40101 SET NAMES utf8mb4 */;

-- Insert Level
INSERT INTO level (id, level, note)
VALUES
    (1, 'JLPT-N5', 'Level: Mampu memahami bahasa Jepang dasar. Target: ±100 kanji, ±800 kosakata. Nilai Minimal: Seksi (38 dari 120, 19 dari 60), Total (80 dari 180).'),
    (2, 'JLPT-N4', 'Level: Mampu memahami bahasa Jepang dasar, memahami percakapan sehari-hari yang diucapkan perlahan. Target: ±300 kanji, ±1.500 kosakata. Nilai Minimal: Seksi (38 dari 120, 19 dari 60), Total (90 dari 180).'),
    (3, 'JLPT-N3', 'Level: Mampu memahami bahasa Jepang yang digunakan dalam percakapan sehari-hari dengan kecepatan mendekati normal. Target: ±650 kanji, ±3.750 kosakata. Nilai Minimal: Seksi (19 dari 60), Total (95 dari 180).'),
    (4, 'JLPT-N2', 'Level: Mampu memahami bahasa Jepang yang digunakan dalam berbagai situasi sehari-hari dan profesional. Target: ±1.000 kanji, ±6.000 kosakata. Nilai Minimal: Seksi (19 dari 60), Total (90 dari 180).'),
    (5, 'JLPT-N1', 'Level: Mampu memahami bahasa Jepang yang digunakan dalam berbagai situasi, termasuk tulisan dengan topik yang kompleks. Target: ±2.000 kanji, ±10.000 kosakata. Nilai Minimal: Seksi (19 dari 60), Total (100 dari 180).');

-- Insert MainNote
INSERT INTO main_note (id, section, level_id, identifier, pattern_name, main_function, main_use_when, main_note, created_at)
VALUES
    (1,'N4-L1.1.1',2,' ～が + V可能形','Ungkapan Bisa Melakukan Sesuatu','Menyatakan kemampuan, kesanggupan, atau kemungkinan bahwa subjek (yang ditandai dengan が) dapat melakukan suatu tindakan.', 'Ketika ingin menyatakan kemampuan fisik, keterampilan, atau kondisi yang memungkinkan/menghambat seseorang melakukan suatu hal.', 'Subjek untuk verba potensial biasanya ditandai dengan が, bukan は.', NOW()),
    (2,'N4-L1.1.2',2,'～ので/～なので','Pola Menyatakan Alasan (ので/なので)','Menyatakan alasan atau sebab dari suatu kondisi atau tindakan. Mirip dengan から, tetapi lebih lembut, sopan, dan cocok untuk situasi formal atau ketika berbicara dengan seseorang yang dihormati.','Dalam percakapan formal atau sopan, seperti laporan, presentasi, atau saat meminta izin. Ketika ingin memberi alasan dengan nada tidak terlalu langsung atau menyalahkan.','Jangan lupa, setelah na-adjective dan noun, harus tambah な sebelum ので → 静かなので, 学生なので', NOW());

-- Insert Formula (2 tiap MainNote)
INSERT INTO formula (id, main_note_id, sub_section, pattern, sub_function, sub_use_when, sub_note)
VALUES
    (1, 1, 'N4-L1.1.1.1', 'Noun + が + Vkanokei','Ungkapan Bisa Melakukan Sesuatu', 'Menyatakan kemampuan, kesanggupan, atau kemungkinan bahwa subjek (yang ditandai dengan が) dapat melakukan suatu tindakan.', ''),
    (2, 2, 'N4-L1.1.2.1', '[Vfutsukei・Adj-い] + ので、[other phrase]', '', '',''),
    (3, 2, 'N4-L1.1.2.2', '[Adj-な・Noun] + なので、[other phrase]', '', '','');

-- Insert Example (2 tiap Formula)
INSERT INTO example (id, formula_id, sample_kanji, sample_non_kanji, meaning, note)
VALUES
    (1, 1, 'メニューの日本語が読めますか。', '', 'apakah Anda bisa membaca tulisan Jepang di menu?', ''),
    (2, 1, 'いそがしかったけど、映画館で映画が見られました。', '', 'Saya sibuk, tetapi saya bisa menonton film di bioskop.', ''),
    (3, 1, '魚が食べられます', '', 'Bisa makan ikan', ''),
    (4, 1, 'パソコンが使えます', '', 'Bisa menggunakan komputer', ''),
    (5, 1, 'パソコンで日本語の入力ができます。', '', 'Bisa mengetik bahasa Jepang dengan komputer.', ''),
    (6, 1, '簡単な料理なら作れます', '', 'kalau masakan yang sederhana, saya bisa memasak.', ''),
    (7, 1, '自分で車が運転できます', '', 'Saya bisa mengendarai mobil saya sendiri', ''),
    (8, 2, '午後は授業があるので、午前でもいいですか。', 'ごごはじゅぎょうがあるので、ごぜんでもいいですか。', 'Saya ada pelajaran di sore hari, jadi apakah boleh di pagi hari?', 'Verb (aru) in futsu-kei: ある'),
    (9, 2, 'この本は面白いので、買いたいです。', 'このほんはおもしろいので、かいたいです。', 'Buku ini menarik, jadi saya ingin membelinya.', 'Adjective-i: 面白い (omoshiroi)'),
    (10, 2, '宿題が終わらなかったので、遊びに行けません。', 'しゅくだいがおわらなかったので、あそびにいけません。', 'PR-nya belum selesai, jadi saya tidak bisa pergi bermain.', 'Verb (owaru) in futsu-kei, past negative: 終わらなかった (owaranakatta)'),
    (11, 2, '昨日は寒かったので、家でゆっくりしました。', 'きのうはさむかったので、いえでゆっくりしました。', 'Kemarin dingin, jadi saya santai di rumah.', 'Adjective-i in past tense: 寒かった (samukatta)');
