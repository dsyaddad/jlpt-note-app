-- Create database
CREATE DATABASE IF NOT EXISTS notesdb
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE notesdb;

-- Table level
CREATE TABLE level (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       level VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                       note TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
);

-- Table main_note
CREATE TABLE main_note (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           section VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                           level_id BIGINT,
                           identifier VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                           pattern_name VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                           main_function TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                           main_use_when TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                           main_note TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                           created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table formula
CREATE TABLE formula (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         main_note_id BIGINT,
                         type_form VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                         pattern VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                         sub_function TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                         sub_use_when TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                         sub_note TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
);

-- Table example
CREATE TABLE example (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         formula_id BIGINT,
                         sample_kanji TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                         sample_non_kanji TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                         meaning TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                         note TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
);

CREATE TABLE jlpt_words (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            section VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                            level_id BIGINT,
                            kanji VARCHAR(255),
                            kana VARCHAR(255) NOT NULL,
                            romaji VARCHAR(255),
                            meaning_en TEXT,
                            meaning_id TEXT,
                            pos VARCHAR(100),
                            note TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP NULL
);

CREATE TABLE jlpt_examples (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               word_id BIGINT, -- referensi ke jlpt_words.id (tanpa constraint)
                               jp_sentence TEXT,
                               translation TEXT,
                               note TEXT
);
-- Tabel kata dasar (lemma)
CREATE TABLE lemma (
                       id                  BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                       headword_kana       VARCHAR(64)  NOT NULL,  -- あう / たべる / いい / ひま / あめ
                       kanji               VARCHAR(64)  NULL,      -- 会う / 食べる / 良い / 暇 / 雨
                       pos_type            ENUM('DOSHI_GODAN','DOSHI_ICHIDAN','DOSHI_IRREGULAR','KEIYOSHI','KEIYODOUSHI','MEISHI') NOT NULL,
                       godan_ending_kana   CHAR(1) NULL,           -- う/つ/る/む/ぶ/ぬ/く/ぐ/す (hanya untuk 五段)
                       meaning             VARCHAR(255) NULL,

    -- Validasi sederhana (wajib ending jika godan; selain itu harus NULL)
                       CONSTRAINT ck_godan_ending
                           CHECK (
                               (pos_type <> 'DOSHI_GODAN' AND godan_ending_kana IS NULL)
                                   OR
                               (pos_type = 'DOSHI_GODAN' AND godan_ending_kana IN ('う','つ','る','む','ぶ','ぬ','く','ぐ','す'))
                               ),

                       KEY ix_lemma_headword (headword_kana),
                       KEY ix_lemma_pos (pos_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin;

-- Tabel override (hanya untuk anomali: する / くる / いい / compound)
CREATE TABLE conjugation_override (
                                      id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                      lemma_id    BIGINT UNSIGNED NOT NULL,
                                      form_type   ENUM('DICTIONARY','MASU','TE','TA','NAI','POTENTIAL','VOLITIONAL','CONDITIONAL','CONDITIONAL_NEG','IMPERATIVE','PASSIVE','CAUSATIVE','CAUSATIVE_PASSIVE') NOT NULL,
                                      surface     VARCHAR(64) NOT NULL,    -- hasil konjugasi override
                                      note        VARCHAR(255) NULL,

                                      CONSTRAINT fk_override_lemma
                                          FOREIGN KEY (lemma_id) REFERENCES lemma(id)
                                              ON DELETE CASCADE ON UPDATE CASCADE,

                                      UNIQUE KEY uq_override (lemma_id, form_type),
                                      KEY ix_override_form (form_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin;