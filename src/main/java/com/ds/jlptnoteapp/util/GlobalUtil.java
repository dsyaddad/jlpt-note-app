package com.ds.jlptnoteapp.util;

import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

import java.io.*;

@UtilityClass
public class GlobalUtil {
    public static void exportDml(String dbUser, String dbPassword, String dbName) {
        String path = "script-db";  // folder penyimpanan
        String fileName = "dml-script.sql"; // fixed file name

        try {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File outFile = new File(dir, fileName);

            if (outFile.exists()) {
                System.out.println("[INFO] Existing file " + outFile.getAbsolutePath() + " will be overwritten.");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile, false))) {
                writer.write("-- Pastikan session pakai utf8mb4");
                writer.newLine();
                writer.write("/*!40101 SET NAMES utf8mb4 */;");
                writer.newLine();
                writer.newLine();

                // Command mysqldump
                String[] cmd = {
                        "mysqldump",
                        "-u" + dbUser,
                        "-p" + dbPassword,
                        "--no-create-info",
                        "--skip-triggers",
                        dbName
                };

                ProcessBuilder pb = new ProcessBuilder(cmd);
                pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                Process process = pb.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    System.out.println("[INFO] Export success: " + outFile.getAbsolutePath());
                } else {
                    throw new RuntimeException("Export failed with code: " + exitCode);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to export DML", e);
        }
    }
}
