-- Insert MainNote
INSERT INTO main_note (id, section, level_id, identifier, pattern_name, main_function, main_use_when, main_note, created_at)
VALUES
    (1, 'Grammar', 1, 'MN-001', 'Pattern A', 'Function A1', 'Use when A1', 'Note A1', NOW()),
    (2, 'Grammar', 2, 'MN-002', 'Pattern B', 'Function B1', 'Use when B1', 'Note B1', NOW()),
    (3, 'Vocabulary', 3, 'MN-003', 'Pattern C', 'Function C1', 'Use when C1', 'Note C1', NOW());

-- Insert Formula (2 tiap MainNote)
INSERT INTO formula (id, main_note_id, section, sub_section, sub_function, sub_use_when, sub_note)
VALUES
    (1, 1, 'Grammar', 'Sub A1', 'Sub Function A1-1', 'Sub Use A1-1', 'Sub Note A1-1'),
    (2, 1, 'Grammar', 'Sub A2', 'Sub Function A1-2', 'Sub Use A1-2', 'Sub Note A1-2'),
    (3, 2, 'Grammar', 'Sub B1', 'Sub Function B1-1', 'Sub Use B1-1', 'Sub Note B1-1'),
    (4, 2, 'Grammar', 'Sub B2', 'Sub Function B1-2', 'Sub Use B1-2', 'Sub Note B1-2'),
    (5, 3, 'Vocabulary', 'Sub C1', 'Sub Function C1-1', 'Sub Use C1-1', 'Sub Note C1-1'),
    (6, 3, 'Vocabulary', 'Sub C2', 'Sub Function C1-2', 'Sub Use C1-2', 'Sub Note C1-2');

-- Insert Example (2 tiap Formula)
INSERT INTO example (id, formula_id, sample_kanji, sample_non_kanji, meaning, note)
VALUES
    (1, 1, '漢字A1-1', 'Kanji A1-1', 'Meaning A1-1', 'Note Ex A1-1'),
    (2, 1, '漢字A1-2', 'Kanji A1-2', 'Meaning A1-2', 'Note Ex A1-2'),
    (3, 2, '漢字A2-1', 'Kanji A2-1', 'Meaning A2-1', 'Note Ex A2-1'),
    (4, 2, '漢字A2-2', 'Kanji A2-2', 'Meaning A2-2', 'Note Ex A2-2'),
    (5, 3, '漢字B1-1', 'Kanji B1-1', 'Meaning B1-1', 'Note Ex B1-1'),
    (6, 3, '漢字B1-2', 'Kanji B1-2', 'Meaning B1-2', 'Note Ex B1-2'),
    (7, 4, '漢字B2-1', 'Kanji B2-1', 'Meaning B2-1', 'Note Ex B2-1'),
    (8, 4, '漢字B2-2', 'Kanji B2-2', 'Meaning B2-2', 'Note Ex B2-2'),
    (9, 5, '漢字C1-1', 'Kanji C1-1', 'Meaning C1-1', 'Note Ex C1-1'),
    (10, 5, '漢字C1-2', 'Kanji C1-2', 'Meaning C1-2', 'Note Ex C1-2'),
    (11, 6, '漢字C2-1', 'Kanji C2-1', 'Meaning C2-1', 'Note Ex C2-1'),
    (12, 6, '漢字C2-2', 'Kanji C2-2', 'Meaning C2-2', 'Note Ex C2-2');
