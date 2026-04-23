package pck1;

import java.util.*;


public class ProfilCalator {

    private static final Map<String, Set<String>> PREFERINTE = new HashMap<>();

    static {
        PREFERINTE.put("aventurier", new HashSet<>(Arrays.asList(
                "drumetie", "aventura", "natura", "panorama", "munti", "sport")));

        PREFERINTE.put("cultural", new HashSet<>(Arrays.asList(
                "istorie", "arhitectura", "muzeu", "medieval", "unesco",
                "religie", "cultura", "arta")));

        PREFERINTE.put("relaxare", new HashSet<>(Arrays.asList(
                "parc", "spa", "familie", "natura", "liniste", "peisaj")));

        PREFERINTE.put("gastronomic", new HashSet<>(Arrays.asList(
                "restaurant", "traditie", "mancare", "vin", "cafenea")));

        PREFERINTE.put("mixt", new HashSet<>(Arrays.asList(
                "istorie", "natura", "familie", "traditie")));
    }

    public static Set<String> getEticheteProfil(String tip_calator) {
        if (tip_calator == null) return new HashSet<>();
        return PREFERINTE.getOrDefault(tip_calator.toLowerCase(), new HashSet<>());
    }
}