package pck1;

import java.util.ArrayList;
import java.util.List;


public class Itinerariu {
    private List<Locatie> locatii;
    private double cost_total;
    private int durata_totala_ore;

    public Itinerariu() {
        this.locatii = new ArrayList<>();
        this.cost_total = 0.0;
        this.durata_totala_ore = 0;
    }

    public void adaugaLocatie(Locatie loc) {
        locatii.add(loc);
        cost_total += loc.getCostEstimat();
        durata_totala_ore += loc.getDurataVizitaOre();
    }

    public List<Locatie> getLocatii() { return locatii; }
    public double getCostTotal() { return cost_total; }
    public int getDurataTotalaOre() { return durata_totala_ore; }

    public String afisare() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ITINERARIU RECOMANDAT ===\n");
        if (locatii.isEmpty()) {
            sb.append("Nicio locatie nu se potriveste preferintelor.\n");
            return sb.toString();
        }
        int pas = 1;
        for (Locatie loc : locatii) {
            sb.append(pas++).append(". ").append(loc).append("\n");
        }
        sb.append("-----------------------------\n");
        sb.append(String.format("Cost total: %.2f RON\n", cost_total));
        sb.append(String.format("Durata totala: %d ore\n", durata_totala_ore));
        return sb.toString();
    }
}