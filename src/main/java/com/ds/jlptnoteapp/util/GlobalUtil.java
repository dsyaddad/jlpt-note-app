package com.ds.jlptnoteapp.util;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
@Log4j2
public class GlobalUtil {
    public static void exportDml() {
        try {
            File outputFile = Paths.get("script-db", "dml-script.sql").toFile();

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


}
