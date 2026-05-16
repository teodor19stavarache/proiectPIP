package pck1.Proiect_PIP;

import java.util.ArrayList;
import java.util.List;

public class Utilizator {

    private String email;
    private String nume;
    private String parola;
    private String numarTelefon;
    private List<String> tags;

    public Utilizator() {
        this.tags = new ArrayList<>();
    }

    public Utilizator(String email, String nume, String parola, String numarTelefon, List<String> tags) {
        this.email = email;
        this.nume = nume;
        this.parola = parola;
        this.numarTelefon = numarTelefon;
        this.tags = tags != null ? tags : new ArrayList<>();
    }

    public String getEmail()          { return email; }
    public String getNume()           { return nume; }
    public String getParola()         { return parola; }
    public String getNumarTelefon()   { return numarTelefon; }
    public List<String> getTags()     { return tags; }

    public void setEmail(String email)                { this.email = email; }
    public void setNume(String nume)                  { this.nume = nume; }
    public void setParola(String parola)              { this.parola = parola; }
    public void setNumarTelefon(String numarTelefon)  { this.numarTelefon = numarTelefon; }
    public void setTags(List<String> tags)            { this.tags = tags; }

    @Override
    public String toString() {
        return "Utilizator{email='" + email + "', nume='" + nume + "', tags=" + tags + "}";
    }
}
