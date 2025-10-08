package com.ds.jlptnoteapp.util;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
@Log4j2
public class GlobalUtil {

    public static String CONTAINER;
    public static String DB_NAME;
    public static String USER;
    public static String PASSWORD;
    public static String SQL_FILE_PATH;

    public static void exportDml() {
        try {
            File outputFile = Paths.get("script-db", "02_dml.sql").toFile();

            if (outputFile.exists()) {
                log.info("Existing file {} will be overwritten.", outputFile.getAbsolutePath());
            }

            // tulis header dulu
            try (FileWriter fw = new FileWriter(outputFile, false)) {
                fw.write("-- Pastikan session pakai utf8mb4\n");
                fw.write("/*!40101 SET NAMES utf8mb4 */;\n\n");
            }

            // perintah docker exec mysqldump
            ProcessBuilder pb = new ProcessBuilder(
                    "docker", "exec", "notes-mysql",
                    "mysqldump",
                    "-u", "root",
                    "-proot",
                    "--skip-triggers",
                    "--no-create-info",
                    "--set-gtid-purged=OFF",
                    "--default-character-set=utf8mb4",
                    "notesdb"
            );

            // arahkan hasil mysqldump append ke file
            pb.redirectOutput(ProcessBuilder.Redirect.appendTo(outputFile));
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);

            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("DML export successful, saved to {}", outputFile.getAbsolutePath());
            } else {
                throw new RuntimeException("Failed to export DML, exit code " + exitCode);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to export DML", e);
        }
    }

    // Pola: harus di AWAL string -> N{1digit}-L{2digit}{bebas}
    private static final Pattern NL_HEAD = Pattern.compile("^N(\\d)-L(\\d{2})(.*)$");

    /** Deteksi "N{1digit}-L{2digit}{bebas}" di awal string. */
    public static boolean isN1L2(String s) {
        if (s == null) return false;
        return NL_HEAD.matcher(s.trim()).matches();
    }

    /**
     * Pisahkan "N{1digit}-L{2digit}{bebas}" menjadi:
     *   ["N{digit}", "L{2digit}{suffix}"]
     * Hanya '-' pertama yang dihilangkan (antara N dan L).
     * Return Optional.empty() jika tidak match.
     */
    public static Optional<String[]> splitN1L2(String s) {
        if (s == null) return Optional.empty();
        Matcher m = NL_HEAD.matcher(s.trim());
        if (!m.matches()) return Optional.empty();

        String left  = "N" + m.group(1);               // N{digit}
        String right = "L" + m.group(2) + m.group(3);  // L{2digit}{suffix}
        return Optional.of(new String[]{ left, right });
    }

    public void truncateSelectedThenImport(List<String> tableWhitelist) {
        if (tableWhitelist == null || tableWhitelist.isEmpty()) {
            throw new IllegalArgumentException("Daftar tabel untuk dikosongkan tidak boleh kosong.");
        }

        try {
            // 1) TRUNCATE yang dipilih
            String truncateScript = buildTruncateScript(tableWhitelist);
            execMysqlWithInlineSql(truncateScript);
            log.info("TRUNCATE selected tables done: {}", tableWhitelist);

            // 2) Import DML dari host file
            Path sql = Path.of(SQL_FILE_PATH);
            if (!Files.exists(sql)) {
                throw new RuntimeException("File SQL tidak ditemukan: " + sql.toAbsolutePath());
            }
            importSqlFromHostFile(sql);
            log.info("DML import successful from {}", sql.toAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException("Maintenance failed (truncateSelectedThenImport)", e);
        }
    }

    /**
     * Hanya import DML (tanpa truncate).
     */
    public void importOnly() {
        try {
            Path sql = Path.of(SQL_FILE_PATH);
            if (!Files.exists(sql)) {
                throw new RuntimeException("File SQL tidak ditemukan: " + sql.toAbsolutePath());
            }
            importSqlFromHostFile(sql);
            log.info("DML import successful from {}", sql.toAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("DML import failed", e);
        }
    }

    /**
     * Hanya TRUNCATE tabel yang dipilih (tanpa import).
     */
    public void truncateSelectedOnly(List<String> tableWhitelist) {
        if (tableWhitelist == null || tableWhitelist.isEmpty()) {
            throw new IllegalArgumentException("Daftar tabel untuk dikosongkan tidak boleh kosong.");
        }
        try {
            String truncateScript = buildTruncateScript(tableWhitelist);
            execMysqlWithInlineSql(truncateScript);
            log.info("TRUNCATE selected tables done: {}", tableWhitelist);
        } catch (Exception e) {
            throw new RuntimeException("TRUNCATE failed", e);
        }
    }

    // ------ helpers ------

    private String buildTruncateScript(List<String> tables) {
        StringBuilder sb = new StringBuilder("SET FOREIGN_KEY_CHECKS=0; ");
        for (String t : tables) {
            if (t == null || t.isBlank()) continue;
            sb.append("TRUNCATE TABLE `").append(t.trim()).append("`; ");
        }
        sb.append("SET FOREIGN_KEY_CHECKS=1;");
        return sb.toString();
    }

    private void execMysqlWithInlineSql(String sql) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "docker", "exec", "-i", CONTAINER,
                "mysql",
                "-u", USER,
                "-p" + PASSWORD,
                "--default-character-set=utf8mb4",
                DB_NAME,
                "-e", sql
        );
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();
        int code = p.waitFor();
        if (code != 0) throw new RuntimeException("MySQL exec failed, exit code " + code);
    }

    private void importSqlFromHostFile(Path sqlFile) throws IOException, InterruptedException {
        // Stabil & simpel: pipe stdin dari host file ke mysql di dalam CONTAINER
        ProcessBuilder pb = new ProcessBuilder(
                "docker", "exec", "-i", CONTAINER,
                "mysql",
                "-u", USER,
                "-p" + PASSWORD,
                "--default-character-set=utf8mb4",
                DB_NAME
        );
        pb.redirectInput(sqlFile.toFile());
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process p = pb.start();
        int code = p.waitFor();
        if (code != 0) throw new RuntimeException("MySQL import failed, exit code " + code);
    }
}
