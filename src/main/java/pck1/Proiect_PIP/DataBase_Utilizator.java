package pck1.Proiect_PIP;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class DataBase_Utilizator {
	static String url = "jdbc:sqlite:" + Paths.get("database", "PIP_P.db").toAbsolutePath().toString();

	/** * Crearea tabelului folosind un SQLite */ 
	public void creareTabela() {

	    String sql = """
	        CREATE TABLE IF NOT EXISTS utilizator_db (
	            email TEXT UNIQUE,
	            nume TEXT,
	            parola TEXT,
	            numar_telefon TEXT,
	            taguri TEXT
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
	public static void adauga_utilizator(Utilizator util) {
		String s_adauga = "INSERT INTO utilizator_db(email,nume,parola,numar_telefon,taguri) VALUES(?,?,?,?,?);";
		String taguriJoined = String.join(",", util.getTags());
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement ps = conn.prepareStatement(s_adauga)) {
			ps.setString(1, util.getEmail());
			ps.setString(2, util.getNume());
			ps.setString(3, Utilizator.encode(util.getParola()));
			ps.setString(4, util.getNumarTelefon());
			ps.setString(5, taguriJoined);
			ps.executeUpdate();
			System.out.println("Utilizator adaugat!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * @param util - Utilizator 
	 * @return boolean : true - daca parola coincide / false - daca parola nu coincide
	 */
	public static boolean verifica_parola(Utilizator util) {
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
				System.out.println("Utilizator gasit!");
				return true;
			} else {
				System.out.println("Utilizator fara cont!");
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
	public static void update_nume(Utilizator util, String db_nume_nou) {
		String sql = "UPDATE utilizator_db SET nume = ? WHERE email = ?";
		try (Connection conn = DriverManager.getConnection(url); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, db_nume_nou);
			ps.setString(2, util.getEmail());
			ps.executeUpdate();
			System.out.println("Nume schimbat!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/** * Inlocuieste parola utilizatorului 
	 * @param util - Utilizator  
	 * @param parola_noua - String  
	 * 
	 */
	public static void update_parola(Utilizator util, String db_parola_noua) {
		String sql = "UPDATE utilizator_db SET parola = ? WHERE email = ?";
		try (Connection conn = DriverManager.getConnection(url); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, Utilizator.encode(db_parola_noua));
			ps.setString(2, util.getEmail());
			ps.executeUpdate();
			System.out.println("Parola schimbata!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * * Inlocuieste numarul de telefon al utilizatorului 
	 * @param util - Utilizator  
	 * @param numar_nou - String
	 */
	public static void update_numar_telefon(Utilizator util, String db_numar_nou) {
		String sql = "UPDATE utilizator_db SET numar_telefon = ? WHERE email = ?";
		try (Connection conn = DriverManager.getConnection(url); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, db_numar_nou);
			ps.setString(2, util.getEmail());
			ps.executeUpdate();
			System.out.println("Numar de telefon schimbat!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * * Inlocuieste tagurile utilizatorului
	 * @param util - Utilizator
	 * @param tags_noi - List<String>
	 */
	public static void update_tags(Utilizator util, List<String> tags_noi) {
		String sql = "UPDATE utilizator_db SET taguri = ? WHERE email = ?";
		String taguriJoined = String.join(",", tags_noi);
		try(Connection conn = DriverManager.getConnection(url); PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setString(1, taguriJoined);
			ps.setString(2, util.getEmail());
			ps.executeUpdate();
			System.out.println("Taguri schimnate!");
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}