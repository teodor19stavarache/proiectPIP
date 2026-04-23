package pck1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBase {

	String url = "jdbc:sqlite:actualul.db";

	public DataBase() {
		initializare_baza_de_date();
	}

	public void initializare_baza_de_date() {
		String s_utilizatori = "CREATE TABLE IF NOT EXISTS utilizator_db("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "email TEXT NOT NULL UNIQUE,"
				+ "nume TEXT NOT NULL UNIQUE,"
				+ "parola TEXT NOT NULL)";

		String s_listings = "CREATE TABLE IF NOT EXISTS listings("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "cazare TEXT NOT NULL,"
				+ "disponibilitate INTEGER NOT NULL,"
				+ "pret REAL NOT NULL,"
				+ "locatie TEXT NOT NULL,"
				+ "descriere TEXT,"
				+ "utilizator_id INTEGER,"
				+ "FOREIGN KEY(utilizator_id) REFERENCES utilizator_db(id))";

		String s_airbnb = "CREATE TABLE IF NOT EXISTS airbnb_listings("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "cazare TEXT NOT NULL,"
				+ "disponibilitate INTEGER NOT NULL,"
				+ "pret REAL NOT NULL,"
				+ "oras TEXT NOT NULL,"
				+ "adresa TEXT,"
				+ "descriere TEXT,"
				+ "numar_camere INTEGER,"
				+ "utilizator_id INTEGER,"
				+ "FOREIGN KEY(utilizator_id) REFERENCES utilizator_db(id))";

		try (Connection conn = DriverManager.getConnection(url);
				Statement stmt = conn.createStatement()) {
			stmt.execute(s_utilizatori);
			stmt.execute(s_listings);
			stmt.execute(s_airbnb);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void adauga_utilizator(String email, String nume, String parola) {
		String s_adauga = "INSERT INTO utilizator_db(email,nume,parola) VALUES(?,?,?)";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement ps = conn.prepareStatement(s_adauga)) {
			ps.setString(1, email);
			ps.setString(2, nume);
			ps.setString(3, parola);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public boolean verifica_parola(String nume, String parola) {
		String parola_gasita = null;
		String s_verifica = "SELECT parola FROM utilizator_db WHERE nume = ?";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement ps = conn.prepareStatement(s_verifica)) {
			ps.setString(1, nume);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				parola_gasita = rs.getString("parola");
			}
			if (parola_gasita != null && parola_gasita.equals(parola)) {
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	public int id_utilizator(String nume) {
		String s_cautare = "SELECT id FROM utilizator_db WHERE nume = ?";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement ps = conn.prepareStatement(s_cautare)) {
			ps.setString(1, nume);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("id");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return -1;
	}

	public void adauga_listing(String cazare, boolean disponibilitate, double pret, String locatie, String descriere, int utilizator_id) {
		String s_adauga = "INSERT INTO listings(cazare,disponibilitate,pret,locatie,descriere,utilizator_id) VALUES(?,?,?,?,?,?)";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement ps = conn.prepareStatement(s_adauga)) {
			ps.setString(1, cazare);
			ps.setInt(2, disponibilitate ? 1 : 0);
			ps.setDouble(3, pret);
			ps.setString(4, locatie);
			ps.setString(5, descriere);
			ps.setInt(6, utilizator_id);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void adauga_listing_airbnb(String cazare, boolean disponibilitate, double pret, String oras, String adresa, String descriere, int numar_camere, int utilizator_id) {
		String s_adauga = "INSERT INTO airbnb_listings(cazare,disponibilitate,pret,oras,adresa,descriere,numar_camere,utilizator_id) VALUES(?,?,?,?,?,?,?,?)";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement ps = conn.prepareStatement(s_adauga)) {
			ps.setString(1, cazare);
			ps.setInt(2, disponibilitate ? 1 : 0);
			ps.setDouble(3, pret);
			ps.setString(4, oras);
			ps.setString(5, adresa);
			ps.setString(6, descriere);
			ps.setInt(7, numar_camere);
			ps.setInt(8, utilizator_id);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public ResultSet toate_listingurile() {
		String s_lista = "SELECT * FROM listings ORDER BY id DESC";
		try {
			Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = conn.prepareStatement(s_lista);
			return ps.executeQuery();
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public ResultSet toate_listingurile_airbnb() {
		String s_lista = "SELECT * FROM airbnb_listings ORDER BY id DESC";
		try {
			Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = conn.prepareStatement(s_lista);
			return ps.executeQuery();
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
}
