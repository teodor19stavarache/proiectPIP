package pck1.Proiect_PIP;
import java.util.Base64;

public class Utilizator {
	private String email;
	private String nume;
	private String parola;
	private String numar_telefon;
	
	public static String encode(String parola_encode) {
        return Base64.getEncoder().encodeToString(parola_encode.getBytes());
    }
	
	 public static String decode(String hash) {
	        return new String(Base64.getDecoder().decode(hash));
	    }
	
	public void setEmail(String email_primit) {
		email = email_primit;
	}
	
	public void setNume(String nume_primit) {
		nume = nume_primit;
	}
	
	public void setParola(String parola_primita) {
		parola = encode(parola_primita);
	}
	
	public void setNumarTelefon(String numar_telefon_primit) {
		numar_telefon = numar_telefon_primit;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getNume() {
		return nume;
	}
	
	public String getParola() {
		return decode(parola);
	}
	
	public String getNumarTelefon() {
		return numar_telefon;
	}
}
