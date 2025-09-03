package com.ds.jlptnoteapp.util;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Paths;

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

}
