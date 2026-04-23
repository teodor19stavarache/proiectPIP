package pck1;

import java.sql.ResultSet;

public class Main {

	public static void main(String[] args) {
		DataBase db = new DataBase();

		db.adauga_utilizator("ana@mail.com", "ana", "1234");

		boolean login_ok = db.verifica_parola("ana", "1234");
		System.out.println("Login ok: " + login_ok);

		int utilizator_id = db.id_utilizator("ana");
		if (utilizator_id != -1) {
			db.adauga_listing("Apartament central", true, 250.0, "Iasi - Centru", "2 camere, aproape de Palas", utilizator_id);
			db.adauga_listing_airbnb("Studio modern", true, 180.0, "Iasi", "Strada Stefan cel Mare", "Potrivit pentru 2 persoane", 1, utilizator_id);
		}

		try {
			ResultSet rs_listings = db.toate_listingurile();
			System.out.println("LISTINGS:");
			while (rs_listings != null && rs_listings.next()) {
				System.out.println(
					rs_listings.getInt("id") + " | "
					+ rs_listings.getString("cazare") + " | "
					+ rs_listings.getDouble("pret") + " | "
					+ rs_listings.getString("locatie")
				);
			}

			ResultSet rs_airbnb = db.toate_listingurile_airbnb();
			System.out.println("AIRBNB LISTINGS:");
			while (rs_airbnb != null && rs_airbnb.next()) {
				System.out.println(
					rs_airbnb.getInt("id") + " | "
					+ rs_airbnb.getString("cazare") + " | "
					+ rs_airbnb.getDouble("pret") + " | "
					+ rs_airbnb.getString("oras")
				);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
