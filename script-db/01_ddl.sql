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
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE jlpt_examples (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               word_id BIGINT, -- referensi ke jlpt_words.id (tanpa constraint)
                               jp_sentence TEXT,
                               translation TEXT,
                               note TEXT
);
-- Tabel kata dasar (lemma) tanpa constraint/keys
CREATE TABLE lemma (
                       id                BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                       headword_kana     VARCHAR(64)  NOT NULL,  -- あう / たべる / いい / ひま / あめ
                       kanji             VARCHAR(64)  NULL,      -- 会う / 食べる / 良い / 暇 / 雨
                       pos_type          VARCHAR(32)  NOT NULL,  -- asalnya ENUM, jadi VARCHAR bebas
                       godan_ending_kana CHAR(1)      NULL,      -- asalnya constrained, sekarang bebas
                       meaning           VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin;

-- Tabel override (tanpa constraint/keys)
CREATE TABLE conjugation_override (
                                      id        BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                      lemma_id  BIGINT UNSIGNED NOT NULL,   -- tidak ada FOREIGN KEY
                                      form_type VARCHAR(32)  NOT NULL,      -- asalnya ENUM
                                      surface   VARCHAR(64)  NOT NULL,      -- hasil konjugasi override
                                      note      VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin;
