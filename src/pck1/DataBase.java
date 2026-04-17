package pck1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataBase {

	String url = "jdbc:mysql://localhost:3306/test_db";
    String user = "root";
    String parola = "";
    
    public void adauga_utilizator(String email,String nume,String parola) {
    	String s_adauga = "INSERT INTO utilizator_db(email,nume,parola) VALUES(?,?,?);";
    	try(Connection conn = DriverManager.getConnection(url,user,parola);
    			PreparedStatement ps = conn.prepareStatement(s_adauga)){
    		ps.setString(1, email);
    		ps.setString(2, nume);
    		ps.setString(3, parola);
    		
    	}
    	catch(Exception e) {
    		System.out.println(e);
    	}
    }
    /**
     * 
     * @param nume - String 
     * @param parola - String 
     * @return boolean : true - daca parola coincide/false - daca parola nu coincide
     */
    public boolean verifica_parola(String nume,String parola) {
    	String parola_gasita = null;
    	String s_verifica = "SELECT parola FROM utilizator_db WHERE nume = ?";
    	try(Connection conn = DriverManager.getConnection(url,user,parola);
    			PreparedStatement ps = conn.prepareStatement(s_verifica)){
    		
    		ps.setString(1,nume);
    		
    		ResultSet rs = ps.executeQuery();
    		if(rs.next()) {
    			parola_gasita =  rs.getString("parola");
    		}
    		
    		if(parola_gasita == parola) {
    			return true;
    		}
    		else {
    			return false;
    		}
    	}
    	catch (Exception e) {
		System.out.println(e);
	}
    	return false;
    }
}
