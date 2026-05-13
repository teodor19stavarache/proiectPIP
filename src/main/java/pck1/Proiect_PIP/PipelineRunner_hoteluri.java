package pck1.Proiect_PIP;

import java.io.*;
import java.nio.file.*;

public class PipelineRunner_hoteluri {

    public static void run_pipeline() throws Exception {

        Path resourcesDir = Paths.get("src","main","resources").toAbsolutePath();
        Path databaseDir = Paths.get("database").toAbsolutePath();
        
        Files.createDirectories(resourcesDir);
        Files.createDirectories(databaseDir);

        Path jsonFile = resourcesDir.resolve("hotels_filtered.json");
        Path sqliteFile = databaseDir.resolve("output.db");

        // ═══════════════════════════════════════════
        // PASUL 1 — Rulează scriptul care generează JSON
        // ═══════════════════════════════════════════
        System.out.println(">>> Pasul 1: Generare JSON...");
        run_script(
            new String[]{"python", "scripts/script_json.py"},
            "Pasul 1 finalizat — JSON generat în: " + jsonFile.toAbsolutePath()
        );

        // Verifici că fișierul JSON a fost creat
        if (!Files.exists(jsonFile)) {
            throw new FileNotFoundException("Fișierul JSON nu a fost generat: " + jsonFile);
        }

        // ═══════════════════════════════════════════
        // PASUL 2 — Rulează scriptul care convertește JSON → SQLite
        // ═══════════════════════════════════════════
        System.out.println(">>> Pasul 2: Conversie JSON → SQLite...");
        run_script(
            new String[]{"python", "scripts/script_sqlite.py",
                         jsonFile.toString(),       // input  — fișierul JSON
                         sqliteFile.toString()},    // output — fișierul .db
            "Pasul 2 finalizat — SQLite salvat în: " + sqliteFile.toAbsolutePath()
        );

        System.out.println("✔ Pipeline complet.");
    }

    // ───────────────────────────────────────────────
    // Metodă helper — rulează orice script și îi citește output-ul
    // ───────────────────────────────────────────────
    private static void run_script(String[] command, String successMessage) throws Exception {

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        pb.directory(Paths.get("").toAbsolutePath().toFile());

        Process process = pb.start();

        // Citește și afișează output-ul scriptului în timp real
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
