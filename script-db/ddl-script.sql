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
                           main_function VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                           main_use_when VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                           main_note TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                           created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table formula
CREATE TABLE formula (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         main_note_id BIGINT,
                         sub_section VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                         pattern VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                         sub_function VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                         sub_use_when VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                         sub_note TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
);

-- Table example
CREATE TABLE example (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         formula_id BIGINT,
                         sample_kanji VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                         sample_non_kanji VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                         meaning VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                         note TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
);
