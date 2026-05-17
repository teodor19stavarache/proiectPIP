package pck1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Ensures that the Python packages required by the data-collection scripts
 * (hotelAPI2.py and overpassAPI.py) are installed on the local interpreter.
 *
 * The check is performed locally with `pip show <package>`, which does not
 * contact the network when the package is already present. Only the missing
 * packages are forwarded to `pip install`, so subsequent application starts
 * have effectively zero overhead (one fast subprocess per package).
 *
 * Call {@link #ensureDependencies()} once during application startup, before
 * any code path that might invoke the Python scripts.
 */
public final class PythonDependencyManager {

    /**
     * Path to the requirements file, resolved relative to the project root
     * (current working directory at launch time). Adjust if the project
     * layout changes.
     */
    private static final String REQUIREMENTS_PATH = "requirements.txt";

    private PythonDependencyManager() {
        // utility class, no instances
    }

    /**
     * Reads the requirements file, checks which packages are missing, and
     * installs only those. Silently does nothing if the requirements file
     * cannot be found (in case the Python side is not yet deployed).
     */
    public static void ensureDependencies() {
        Path req = Paths.get(REQUIREMENTS_PATH);
        if (!Files.exists(req)) {
            System.err.println("[PythonDeps] requirements.txt not found at "
                    + req.toAbsolutePath() + " — skipping dependency check.");
            return;
        }

        List<String> required;
        try {
            required = parseRequirements(req);
        } catch (IOException e) {
            System.err.println("[PythonDeps] Could not read requirements.txt: "
                    + e.getMessage());
            return;
        }

        List<String> missing = new ArrayList<>();
        for (String pkg : required) {
            if (!isInstalled(pkg)) {
                missing.add(pkg);
            }
        }

        if (missing.isEmpty()) {
            System.out.println("[PythonDeps] All Python dependencies already installed.");
            return;
        }

        System.out.println("[PythonDeps] Missing packages: " + missing
                + " — installing now...");
        installPackages(req);
    }

    /**
     * Extracts package names from requirements.txt, stripping comments,
     * blank lines, and version specifiers (==, >=, ~=, etc.).
     */
    private static List<String> parseRequirements(Path req) throws IOException {
        List<String> packages = new ArrayList<>();
        for (String raw : Files.readAllLines(req, StandardCharsets.UTF_8)) {
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            // Split on the first occurrence of any version-specifier character.
            String name = line.split("[<>=~!\\s]", 2)[0].trim();
            if (!name.isEmpty()) {
                packages.add(name);
            }
        }
        return packages;
    }

    /**
     * Runs `pip show <package>` and returns true if pip reports the package
     * as installed. This is a local lookup — no network calls.
     */
    private static boolean isInstalled(String pkg) {
        try {
            Process p = new ProcessBuilder("pip", "show", pkg)
                    .redirectErrorStream(true)
                    .start();
            // drain output so the process can exit cleanly on Windows
            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
                while (r.readLine() != null) { /* discard */ }
            }
            return p.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            System.err.println("[PythonDeps] Failed to check package '" + pkg
                    + "': " + e.getMessage());
            // Assume installed so we don't trigger an install loop on a broken env.
            return true;
        }
    }

    /**
     * Installs all packages listed in the requirements file in a single
     * pip call. pip itself will no-op packages that are already at the
     * correct version, so passing the whole file is safe.
     */
    private static void installPackages(Path req) {
        try {
            Process p = new ProcessBuilder(
                    "pip", "install", "-r", req.toAbsolutePath().toString())
                    .inheritIO()
                    .start();
            int code = p.waitFor();
            if (code != 0) {
                System.err.println("[PythonDeps] pip install exited with code " + code
                        + ". Run manually: pip install -r " + REQUIREMENTS_PATH);
            }
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            System.err.println("[PythonDeps] pip install failed: " + e.getMessage()
                    + ". Make sure Python and pip are on PATH.");
        }
    }
}
