package pck1.Proiect_PIP;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DataBase_Utilizator {

    private static final String DB_PATH = "database/PIP_P.db";

    private static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite driver not found", e);
        }
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        initTable(conn);
        return conn;
    }

    private static void initTable(Connection conn) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS utilizatori (
                id           INTEGER PRIMARY KEY AUTOINCREMENT,
                email        TEXT NOT NULL UNIQUE,
                nume         TEXT NOT NULL,
                parola       TEXT NOT NULL,
                numarTelefon TEXT,
                tags         TEXT
            )
            """;
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }

    private static String tagsToString(List<String> tags) {
        if (tags == null || tags.isEmpty()) return "";
        return String.join(",", tags);
    }

    private static List<String> stringToTags(String raw) {
        if (raw == null || raw.isBlank()) return new ArrayList<>();
        return Arrays.stream(raw.split(","))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .collect(Collectors.toList());
    }

    public static boolean adauga_utilizator(Utilizator u) {
        String sql = "INSERT INTO utilizatori(email, nume, parola, numarTelefon, tags) VALUES(?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getNume());
            ps.setString(3, u.getParola());
            ps.setString(4, u.getNumarTelefon());
            ps.setString(5, tagsToString(u.getTags()));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[DB] adauga_utilizator error: " + e.getMessage());
            throw new RuntimeException("Nu s-a putut crea contul: " + e.getMessage(), e);
        }
    }

    public static boolean verifica_parola(Utilizator u) {
        String sql = "SELECT parola FROM utilizatori WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getEmail());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("parola").equals(u.getParola());
            }
        } catch (SQLException e) {
            System.err.println("[DB] verifica_parola error: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return false;
    }

    public static Utilizator getUtilizator(String email) {
        String sql = "SELECT * FROM utilizatori WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Utilizator(
                    rs.getString("email"),
                    rs.getString("nume"),
                    rs.getString("parola"),
                    rs.getString("numarTelefon"),
                    stringToTags(rs.getString("tags"))
                );
            }
        } catch (SQLException e) {
            System.err.println("[DB] getUtilizator error: " + e.getMessage());
        }
        return null;
    }

    public static boolean update_tags(Utilizator u, List<String> newTags) {
        String sql = "UPDATE utilizatori SET tags = ? WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tagsToString(newTags));
            ps.setString(2, u.getEmail());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[DB] update_tags error: " + e.getMessage());
            return false;
        }
    }
}
