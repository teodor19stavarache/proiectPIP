package pck1;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Motorul de recomandare multi-factor.
 *
 * Algoritmul are 3 faze:
 *   [A] Filtrare stricta - elimina locatiile ce nu respecta constrangerile dure
 *   [B] Scoring multi-factor - calculeaza un scor de baza pentru fiecare locatie
 *   [C] Construire cu bonusuri dinamice - selecteaza iterativ locatii,
 *       aplicand bonus pentru proximitate si penalizare pentru tipuri repetate
 *
 * Scor de baza (6 componente ponderate, toate normalizate 0-1):
 *    25% potrivirea etichetelor explicite
 *    20% rating-ul
 *    15% potrivirea cu profilul calatorului
 *    15% sezonalitatea
 *    15% costul (cu cat mai mic, cu atat mai bine)
 *    10% tipul locatiei dorit de user
 */
public class MotorRecomandare {

    private final LocatieRepository repository;

    // Ponderi scor de baza
    private static final double POND_ETICHETE    = 0.25;
    private static final double POND_RATING      = 0.20;
    private static final double POND_PROFIL      = 0.15;
    private static final double POND_SEZON       = 0.15;
    private static final double POND_COST        = 0.15;
    private static final double POND_TIP_LOCATIE = 0.10;

    // Ponderi bonusuri dinamice la construire
    private static final double BONUS_PROXIMITATE   = 0.15;
    private static final double PENALIZARE_REPETARE = 0.20;

    // Distanta peste care nu mai dam bonus de proximitate
    private static final double PRAG_PROXIMITATE_KM = 100.0;

    public MotorRecomandare(LocatieRepository repository) {
        this.repository = repository;
    }

    /**
     * Metoda principala - genereaza itinerariul pentru preferintele date.
     */
    public Itinerariu genereazaItinerariu(PreferinteUtilizator prefs) {
        List<Locatie> toate = repository.getToateLocatiile();
        List<Locatie> filtrate = filtreazaLocatii(toate, prefs);

        Map<Locatie, Double> scoruri_baza = new HashMap<>();
        for (Locatie loc : filtrate) {
            scoruri_baza.put(loc, calculeazaScorBaza(loc, prefs));
        }

        return construiesteItinerariu(scoruri_baza, prefs);
    }

    // ==================== [A] FILTRARE ====================

    /**
     * Elimina locatiile care NU respecta constrangerile stricte.
     * Tolerant la date lipsa (ex: rating 0 = necunoscut, nu e exclus).
     */
    private List<Locatie> filtreazaLocatii(List<Locatie> locatii, PreferinteUtilizator prefs) {
        int luna = prefs.getDataCalatoriei().getMonthValue();
        int zi_sapt = prefs.getDataCalatoriei().getDayOfWeek().getValue();

        return locatii.stream()
                .filter(loc -> prefs.getOrasPreferat() == null
                        || loc.getOras().equalsIgnoreCase(prefs.getOrasPreferat()))
                .filter(loc -> loc.getRating() == 0 || loc.getRating() >= prefs.getRatingMinim())
                .filter(loc -> loc.getCostEstimat() <= prefs.getBugetMaxim())
                .filter(loc -> loc.getZileInchise().isEmpty() || loc.esteDeschisa(zi_sapt))
                .filter(loc -> loc.esteInSezon(luna))
                .collect(Collectors.toList());
    }

    // ==================== [B] SCOR DE BAZA ====================

    /**
     * Calculeaza scorul de baza (fara bonusuri dinamice).
     */
    private double calculeazaScorBaza(Locatie loc, PreferinteUtilizator prefs) {
        return  scorEtichete(loc, prefs)      * POND_ETICHETE
              + scorRating(loc)                * POND_RATING
              + scorProfil(loc, prefs)         * POND_PROFIL
              + scorSezonalitate(loc, prefs)   * POND_SEZON
              + scorCost(loc, prefs)           * POND_COST
              + scorTipLocatie(loc, prefs)     * POND_TIP_LOCATIE;
    }

    /** Procentul de etichete dorite care apar la locatie. */
    private double scorEtichete(Locatie loc, PreferinteUtilizator prefs) {
        if (prefs.getEticheteDorite().isEmpty()) return 0.5;
        Set<String> comun = new HashSet<>(loc.getEtichete());
        comun.retainAll(prefs.getEticheteDorite());
        return (double) comun.size() / prefs.getEticheteDorite().size();
    }

    /** Rating normalizat (0-5 -> 0-1). Rating 0 = necunoscut -> neutru. */
    private double scorRating(Locatie loc) {
        if (loc.getRating() == 0) return 0.5;
        return loc.getRating() / 5.0;
    }

    /** Cat de bine se potriveste locatia cu profilul calatorului. */
    private double scorProfil(Locatie loc, PreferinteUtilizator prefs) {
        Set<String> etichete_profil = ProfilCalator.getEticheteProfil(prefs.getTipCalator());
        if (etichete_profil.isEmpty()) return 0.5;
        Set<String> comun = new HashSet<>(loc.getEtichete());
        comun.retainAll(etichete_profil);
        return Math.min(1.0, comun.size() / 2.0);
    }

    /** Bonus maxim daca locatia e in sezonul optim in luna calatoriei. */
    private double scorSezonalitate(Locatie loc, PreferinteUtilizator prefs) {
        if (loc.getLuniOptime().isEmpty()) return 0.7;
        int luna = prefs.getDataCalatoriei().getMonthValue();
        return loc.getLuniOptime().contains(luna) ? 1.0 : 0.3;
    }

    /** Cost mic raportat la buget = scor mare. */
    private double scorCost(Locatie loc, PreferinteUtilizator prefs) {
        if (prefs.getBugetMaxim() <= 0) return 0.5;
        double raport = loc.getCostEstimat() / prefs.getBugetMaxim();
        return Math.max(0.0, 1.0 - raport);
    }

    /** Potrivirea cu tipurile de locatii dorite explicit de user. */
    private double scorTipLocatie(Locatie loc, PreferinteUtilizator prefs) {
        if (prefs.getTipuriLocatiiDorite().isEmpty()) return 0.5;
        return prefs.getTipuriLocatiiDorite().contains(loc.getTipLocatie()) ? 1.0 : 0.0;
    }

    // ==================== [C] CONSTRUIRE CU BONUSURI DINAMICE ====================

    /**
     * Construieste iterativ itinerariul:
     *   - la fiecare pas alege locatia cu scor ajustat maxim
     *   - scor ajustat = scor baza + bonus proximitate - penalizare repetare
     *   - adauga daca incape in timp si buget
     *   - opreste cand atinge numarul maxim de locatii (depinde de ritm)
     */
    private Itinerariu construiesteItinerariu(Map<Locatie, Double> scoruri_baza,
                                              PreferinteUtilizator prefs) {
        Itinerariu itinerariu = new Itinerariu();
        int max_locatii = determinaMaxLocatii(prefs);
        List<Locatie> disponibile = new ArrayList<>(scoruri_baza.keySet());

        while (!disponibile.isEmpty() && itinerariu.getLocatii().size() < max_locatii) {
            Locatie urmatoarea = null;
            double scor_max = -1;

            for (Locatie candidat : disponibile) {
                double scor_ajustat = scoruri_baza.get(candidat)
                        + bonusProximitate(candidat, itinerariu)
                        - penalizareRepetare(candidat, itinerariu);

                if (scor_ajustat > scor_max) {
                    boolean incape_timp = itinerariu.getDurataTotalaOre()
                            + candidat.getDurataVizitaOre() <= prefs.getOreDisponibile();
                    boolean incape_buget = itinerariu.getCostTotal()
                            + candidat.getCostEstimat() <= prefs.getBugetMaxim();

                    if (incape_timp && incape_buget) {
                        scor_max = scor_ajustat;
                        urmatoarea = candidat;
                    }
                }
            }

            if (urmatoarea == null) break;
            itinerariu.adaugaLocatie(urmatoarea);
            disponibile.remove(urmatoarea);
        }

        return itinerariu;
    }

    /** Bonus crescator cu proximitatea fata de ultima locatie adaugata. */
    private double bonusProximitate(Locatie candidat, Itinerariu itinerariu) {
        if (itinerariu.getLocatii().isEmpty()) return 0;
        Locatie ultima = itinerariu.getLocatii().get(itinerariu.getLocatii().size() - 1);
        double dist = distantaKm(candidat, ultima);
        if (dist >= PRAG_PROXIMITATE_KM) return 0;
        return BONUS_PROXIMITATE * (1.0 - dist / PRAG_PROXIMITATE_KM);
    }

    /** Penalizare daca tipul locatiei apare deja de multe ori. */
    private double penalizareRepetare(Locatie candidat, Itinerariu itinerariu) {
        long aparitii = itinerariu.getLocatii().stream()
                .filter(l -> l.getTipLocatie().equals(candidat.getTipLocatie()))
                .count();
        if (aparitii < 2) return 0;
        return PENALIZARE_REPETARE * (aparitii - 1);
    }

    /** Distanta geografica in km intre doua locatii (formula haversine). */
    private double distantaKm(Locatie a, Locatie b) {
        final int R = 6371;  // raza Pamantului in km
        double latA = Math.toRadians(a.getLatitudine());
        double latB = Math.toRadians(b.getLatitudine());
        double dLat = Math.toRadians(b.getLatitudine() - a.getLatitudine());
        double dLon = Math.toRadians(b.getLongitudine() - a.getLongitudine());

        double h = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(latA) * Math.cos(latB)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h));
        return R * c;
    }

    /** Numarul maxim de locatii in functie de ritmul cerut de user. */
    private int determinaMaxLocatii(PreferinteUtilizator prefs) {
        switch (prefs.getRitm().toLowerCase()) {
            case "relaxat": return 3;
            case "intens":  return 8;
            case "mediu":
            default:        return 5;
        }
    }
}