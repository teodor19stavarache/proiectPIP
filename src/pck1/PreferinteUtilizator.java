package pck1;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


public class PreferinteUtilizator {
    private Set<String> etichete_dorite;
    private Set<String> tipuri_locatii_dorite;
    private String oras_preferat;          // null = oriunde
    private double buget_maxim;
    private int ore_disponibile;
    private double rating_minim;
    private String tip_calator;            // "aventurier"/"cultural"/"relaxare"/"gastronomic"/"mixt"
    private LocalDate data_calatoriei;
    private String ritm;                   // "relaxat"/"mediu"/"intens"

    public PreferinteUtilizator(Set<String> etichete_dorite,
                                Set<String> tipuri_locatii_dorite,
                                String oras_preferat,
                                double buget_maxim,
                                int ore_disponibile,
                                double rating_minim,
                                String tip_calator,
                                LocalDate data_calatoriei,
                                String ritm) {
        this.etichete_dorite = (etichete_dorite != null) ? etichete_dorite : new HashSet<>();
        this.tipuri_locatii_dorite = (tipuri_locatii_dorite != null) ? tipuri_locatii_dorite : new HashSet<>();
        this.oras_preferat = oras_preferat;
        this.buget_maxim = buget_maxim;
        this.ore_disponibile = ore_disponibile;
        this.rating_minim = rating_minim;
        this.tip_calator = (tip_calator != null) ? tip_calator : "mixt";
        this.data_calatoriei = (data_calatoriei != null) ? data_calatoriei : LocalDate.now();
        this.ritm = (ritm != null) ? ritm : "mediu";
    }

    // Getteri
    public Set<String> getEticheteDorite() { return etichete_dorite; }
    public Set<String> getTipuriLocatiiDorite() { return tipuri_locatii_dorite; }
    public String getOrasPreferat() { return oras_preferat; }
    public double getBugetMaxim() { return buget_maxim; }
    public int getOreDisponibile() { return ore_disponibile; }
    public double getRatingMinim() { return rating_minim; }
    public String getTipCalator() { return tip_calator; }
    public LocalDate getDataCalatoriei() { return data_calatoriei; }
    public String getRitm() { return ritm; }
}