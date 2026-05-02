package pck1.Proiect_PIP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataBase_Utilizator {
	String url = "jdbc:sqlite:database/PIP_P.db";

	/** * Crearea tabelului folosind un SQLite */ 
	public void creareTabela() {

	    String sql = """
	        CREATE TABLE IF NOT EXISTS utilizator_db (
	            email TEXT UNIQUE,
	            nume TEXT,
	            parola TEXT,
	            numar_telefon TEXT
	        );
	        """;

	    try(Connection conn = DriverManager.getConnection(url);
	        PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.execute();

	        System.out.println("Tabela creata!");

	    } catch(Exception e) {
	        System.out.println(e);
	    }
	}

	/**
	 * * Adauga un utilizator in baza de date * 
	 * @param util - Utilizator
	 */
	public void adauga_utilizator(Utilizator util) {
		String s_adauga = "INSERT INTO utilizator_db(email,nume,parola,numar_telefon) VALUES(?,?,?,?);";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement ps = conn.prepareStatement(s_adauga)) {
			ps.setString(1, util.getEmail());
			ps.setString(2, util.getNume());
			ps.setString(3, util.getParola());
			ps.setString(4, util.getNumarTelefon());
			ps.executeUpdate();
			System.out.println("Utilizator adaugat!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * @param util - Utilizator 
	 * @return boolean : true -
	 * daca parola coincide/false - daca parola nu coincide
	 */
	public boolean verifica_parola(Utilizator util) {
		String parola_gasita = null;
		String s_verifica = "SELECT parola FROM utilizator_db WHERE email = ?";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement ps = conn.prepareStatement(s_verifica)) {
			ps.setString(1, util.getEmail());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				parola_gasita = rs.getString("parola");
			}
			if (parola_gasita != null && Utilizator.decode(parola_gasita).equals(util.getParola())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	/** * Inlocuieste numele utilizatorului 
	* @param util - Utilizator 
	* @param nume_nou - String  
	*/
	public void updateNume(Utilizator util, String db_nume_nou) {
		String sql = "UPDATE utilizator_db SET nume = ? WHERE email = ?";
		try (Connection conn = DriverManager.getConnection(url); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, db_nume_nou);
			ps.setString(2, util.getEmail());
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/** * Inlocuieste parola utilizatorului 
	 * @param util - Utilizator  
	 * @param parola_noua - String  
	 * */
	public void updateParola(Utilizator util, String db_parola_noua) {
		String sql = "UPDATE utilizator_db SET parola = ? WHERE email = ?";
		try (Connection conn = DriverManager.getConnection(url); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, Utilizator.encode(db_parola_noua));
			ps.setString(2, util.getEmail());
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * * Inlocuieste numarul de telefon al utilizatorului 
	 * @param util - Utilizator  
	 * @param numar_nou - String
	 */
	public void updateNumarTelefon(Utilizator util, String db_numar_nou) {
		String sql = "UPDATE utilizator_db SET numar_telefon = ? WHERE email = ?";
		try (Connection conn = DriverManager.getConnection(url); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, db_numar_nou);
			ps.setString(2, util.getEmail());
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}