package pck1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.SwingWorker;

/**
 * Runs the Python scrapers (overpassAPI.py, hotelAPI2.py) in a background
 * SwingWorker so the GUI thread stays responsive while the network calls
 * to Overpass / SerpApi complete.
 *
 * The scripts are resolved relative to the project root (current working
 * directory at launch). They use their own BASE_DIR internally, so the
 * working directory passed to ProcessBuilder does not matter.
 *
 * Typical usage:
 *     ScraperService.runOverpassSearch(
 *         city, tags,
 *         () -> nav.showPage("search"),   // onSuccess (called on EDT)
 *         err -> showError(err)            // onFailure (called on EDT)
 *     );
 */
public final class ScraperService {

    private static final Path SCRIPT_DIR = Paths.get("..", "hotelAPI");
    private static final Path INPUT_DIR  = SCRIPT_DIR.resolve("input");

    private ScraperService() {
        // utility class
    }

    /**
     * Writes the overpass input file and launches overpassAPI.py in a
     * background thread. Callbacks fire on the Swing EDT.
     */
    public static void runOverpassSearch(String city,
                                          String tagsCsv,
                                          Runnable onSuccess,
                                          java.util.function.Consumer<String> onFailure) {
        new SwingWorker<Integer, String>() {
            private String errorMessage = null;

            @Override
            protected Integer doInBackground() {
                try {
                    writeOverpassInput(city, tagsCsv);
                    int code = runPython("overpassAPI.py");
                    if (code != 0) return code;
                    // Ruleaza hotelAPI2.py daca scriptul exista (non-fatal daca lipseste)
                    runHotelSearch();
                    return 0;
                } catch (Exception e) {
                    errorMessage = e.getMessage();
                    return -1;
                }
            }

            @Override
            protected void done() {
                int exitCode;
                try {
                    exitCode = get();
                } catch (Exception e) {
                    onFailure.accept("Eroare interna: " + e.getMessage());
                    return;
                }
                if (exitCode == 0) {
                    onSuccess.run();
                } else {
                    onFailure.accept(errorMessage != null
                            ? errorMessage
                            : "Scraperul a returnat cod " + exitCode);
                }
            }
        }.execute();
    }

    /**
     * Actualizează orașul în hotel_input.txt, păstrând celelalte câmpuri (date, adulți etc.).
     * Dacă fișierul nu există, scrie valori implicite rezonabile.
     */
    public static void updateHotelCity(String city) {
        Path hotelInput = INPUT_DIR.resolve("hotel_input.txt");
        Map<String, String> params = new LinkedHashMap<>();

        // Valori implicite dacă fișierul nu există
        LocalDate today = LocalDate.now();
        params.put("city",      city);
        params.put("check_in",  today.plusDays(7).toString());
        params.put("check_out", today.plusDays(14).toString());
        params.put("adults",    "2");
        params.put("children",  "0");
        params.put("currency",  "EUR");

        // Citește valorile existente și le suprascrie pe city
        if (Files.exists(hotelInput)) {
            try {
                for (String line : Files.readAllLines(hotelInput, StandardCharsets.UTF_8)) {
                    if (line.contains("=")) {
                        String[] parts = line.split("=", 2);
                        String key = parts[0].trim();
                        if (!key.equals("city")) {
                            params.put(key, parts[1].trim());
                        }
                    }
                }
            } catch (IOException ignored) {}
        }

        try {
            Files.createDirectories(INPUT_DIR);
            try (PrintWriter w = new PrintWriter(
                    Files.newBufferedWriter(hotelInput, StandardCharsets.UTF_8))) {
                for (Map.Entry<String, String> e : params.entrySet()) {
                    w.println(e.getKey() + "=" + e.getValue());
                }
            }
        } catch (IOException ex) {
            System.err.println("[ScraperService] Could not update hotel_input.txt: " + ex.getMessage());
        }
    }

    private static void writeOverpassInput(String city, String tagsCsv) throws IOException {
        Files.createDirectories(INPUT_DIR);
        Path inputFile = INPUT_DIR.resolve("overpass_input.txt");
        try (PrintWriter w = new PrintWriter(
                Files.newBufferedWriter(inputFile, StandardCharsets.UTF_8))) {
            w.println("city=" + city);
            w.println("tags=" + tagsCsv);
        }
    }

    private static void runHotelSearch() {
        Path script = SCRIPT_DIR.resolve("hotelAPI2.py");
        if (!Files.exists(script)) {
            System.out.println("[ScraperService] hotelAPI2.py not found — skipping hotel search.");
            return;
        }
        try {
            int code = runPython("hotelAPI2.py");
            if (code != 0) {
                System.out.println("[ScraperService] hotelAPI2.py finished with code " + code + " — partial results may be shown.");
            }
        } catch (Exception e) {
            System.out.println("[ScraperService] hotelAPI2.py error (non-fatal): " + e.getMessage());
        }
    }

    /**
     * Spawns `python <script>` from the script directory and streams stdout
     * to System.out so progress is visible in the console.
     */
    private static int runPython(String scriptName) throws IOException, InterruptedException {
        Path script = SCRIPT_DIR.resolve(scriptName);
        if (!Files.exists(script)) {
            throw new IOException("Script not found: " + script.toAbsolutePath());
        }

        ProcessBuilder pb = new ProcessBuilder("python", script.getFileName().toString())
                .directory(SCRIPT_DIR.toFile())
                .redirectErrorStream(true);

        Process p = pb.start();
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                System.out.println("[" + scriptName + "] " + line);
            }
        }
        return p.waitFor();
    }
}
