package pck1.Proiect_PIP;

import java.io.*;
import java.nio.file.*;

public class PipelineRunner_atractii {

    public static void run_pipeline_atractii() throws Exception {
        Path databaseDir = Paths.get("database").toAbsolutePath();
        Files.createDirectories(databaseDir);

        System.out.println(">>> Rulare script atractii turistice...");
        run_script(
            new String[]{"python", "scripts/overpassAPI.py"},
            "Pipeline atractii finalizat — DB salvat în: " + databaseDir.resolve("atractii.db")
        );

        System.out.println("✔ Pipeline atractii complet.");
    }

    private static void run_script(String[] command, String successMessage) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        pb.directory(Paths.get("").toAbsolutePath().toFile());
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[Python] " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Scriptul a eșuat cu exit code: " + exitCode
                                       + " | Comandă: " + String.join(" ", command));
        }
        System.out.println(successMessage);
    }
}