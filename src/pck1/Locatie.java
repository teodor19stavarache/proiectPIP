package pck1;

import java.util.HashSet;
import java.util.Set;


public class Locatie {
    private int id;
    private String nume;
    private String oras;
    private String tip_locatie;         
    private Set<String> etichete;        
    private double rating;               
    private double cost_estimat;         
    private int durata_vizita_ore;
    private String descriere;
    private double latitudine;
    private double longitudine;
    private Set<Integer> luni_optime;    
    private Set<Integer> zile_inchise;   

    public Locatie(int id, String nume, String oras, String tip_locatie,
                   Set<String> etichete, double rating, double cost_estimat,
                   int durata_vizita_ore, String descriere,
                   double latitudine, double longitudine,
                   Set<Integer> luni_optime, Set<Integer> zile_inchise) {
        this.id = id;
        this.nume = nume;
        this.oras = oras;
        this.tip_locatie = tip_locatie;
        this.etichete = (etichete != null) ? etichete : new HashSet<>();
        this.rating = rating;
        this.cost_estimat = cost_estimat;
        this.durata_vizita_ore = durata_vizita_ore;
        this.descriere = descriere;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.luni_optime = (luni_optime != null) ? luni_optime : new HashSet<>();
        this.zile_inchise = (zile_inchise != null) ? zile_inchise : new HashSet<>();
    }

    // Getteri
    public int getId() { return id; }
    public String getNume() { return nume; }
    public String getOras() { return oras; }
    public String getTipLocatie() { return tip_locatie; }
    public Set<String> getEtichete() { return etichete; }
    public double getRating() { return rating; }
    public double getCostEstimat() { return cost_estimat; }
    public int getDurataVizitaOre() { return durata_vizita_ore; }
    public String getDescriere() { return descriere; }
    public double getLatitudine() { return latitudine; }
    public double getLongitudine() { return longitudine; }
    public Set<Integer> getLuniOptime() { return luni_optime; }
    public Set<Integer> getZileInchise() { return zile_inchise; }

    /** Verifica daca locatia e deschisa intr-o anumita zi din saptamana. */
    public boolean esteDeschisa(int zi_saptamana) {
        return !zile_inchise.contains(zi_saptamana);
    }

    /** Verifica daca locatia e in sezonul optim. Gol = tot anul. */
    public boolean esteInSezon(int luna) {
        return luni_optime.isEmpty() || luni_optime.contains(luna);
    }

    @Override
    public String toString() {
        return String.format("%s [%s] (%s) - Rating: %.1f, Cost: %.0f RON, Durata: %dh",
                nume, tip_locatie, oras, rating, cost_estimat, durata_vizita_ore);
    }
}