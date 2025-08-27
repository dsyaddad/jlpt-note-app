-- Create database
CREATE DATABASE IF NOT EXISTS notesdb
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE notesdb;

-- Table level
CREATE TABLE level (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       level VARCHAR(255),
                       note TEXT
);

-- Table main_note
CREATE TABLE main_note (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           section VARCHAR(255),
                           level_id BIGINT,
                           identifier VARCHAR(255),
                           pattern_name VARCHAR(255),
                           main_function VARCHAR(255),
                           main_use_when VARCHAR(255),
                           main_note TEXT,
                           created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table formula
CREATE TABLE formula (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         main_note_id BIGINT,
                         sub_section VARCHAR(255),
                         pattern VARCHAR(255),
                         sub_function VARCHAR(255),
                         sub_use_when VARCHAR(255),
                         sub_note TEXT
);

-- Table example
CREATE TABLE example (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         formula_id BIGINT,
                         sample_kanji VARCHAR(255),
                         sample_non_kanji VARCHAR(255),
                         meaning VARCHAR(255),
                         note TEXT
);
