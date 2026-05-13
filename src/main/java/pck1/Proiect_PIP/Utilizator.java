package pck1.Proiect_PIP;
import java.util.Base64;

import java.util.List;

public class Utilizator {
	private String email = "";
	private String nume = "";
	private String parola = "";
	private String numar_telefon = "";
	private List<String> tags;
	
	/**
	 * Constructor Utilizator
	 * @param e
	 * @param n
	 * @param p
	 * @param nr_tel
	 */
	public Utilizator(String e, String n, String p, String nr_tel,List<String> t) {
		email = e;
		nume = n;
		parola = p;
		numar_telefon = nr_tel;
		tags = t;
	}
	
	/**
	 * 
	 * @param parola_encode
	 * @return parola encodata pentru a proteja parola utilizatorului
	 */
	public static String encode(String parola_encode) {
        return Base64.getEncoder().encodeToString(parola_encode.getBytes());
    }
	
	/**
	 * 
	 * @param hash
	 * @return parola decodata pentru a se face verificarile
	 */
	 public static String decode(String hash) {
	        return new String(Base64.getDecoder().decode(hash));
	    }
	
	 /**
	  * schimba email-ul utilizatorului in obiect 
	  * @param email_primit
	  */
	public void setEmail(String email_primit) {
		email = email_primit;
	}
	
	/**
	 * schimba numele utilizatorului in obiect
	 * @param nume_primit
	 */
	public void setNume(String nume_primit) {
		nume = nume_primit;
	}
	
	/**
	 * schimba parola utilizatorului in obiect
	 * @param parola_primita
	 */
	public void setParola(String parola_primita) {
		parola = parola_primita;
	}
	
	/**
	 * schimba numarul de telefon al utilizatorului in obiect
	 * @param numar_telefon_primit
	 */
	public void setNumarTelefon(String numar_telefon_primit) {
		numar_telefon = numar_telefon_primit;
	}
	
	/**
	 * 
	 * @return email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * 
	 * @return nume
	 */
	public String getNume() {
		return nume;
	}
	
	/**
	 * 
	 * @return parola
	 */
	public String getParola() {
		return parola;
	}
	
	/**
	 * 
	 * @return numar_telefon
	 */
	public String getNumarTelefon() {
		return numar_telefon;
	}
	
	public List<String> getTags(){
		return tags;
	}
}
