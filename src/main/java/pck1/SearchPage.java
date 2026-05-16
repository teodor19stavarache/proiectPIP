package pck1;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Displays search results produced by the Python scrapers.
 *
 * Sources (relative to project root):
 *   hotelAPI/output/atractii_filtered.json  -> attractions + restaurants (overpassAPI.py)
 *   hotelAPI/output/hotels_filtered.json    -> hotels (hotelAPI2.py)
 *
 * Display rules:
 *   - 5 hotels
 *   - 5 attractions distributed across the user's chosen categories (2+2+1
 *     when 3 categories are picked)
 *   - 3 restaurants
 *
 * If the JSON files are missing or empty, a friendly placeholder is shown.
 */
public class SearchPage implements AppColors {

    private static final Path ATRACTII_JSON =
        Paths.get("..", "hotelAPI", "output", "atractii_filtered.json");
    private static final Path HOTELS_JSON   =
        Paths.get("..", "hotelAPI", "output", "hotels_filtered.json");

    public static JPanel build(AppNavigator nav) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BG_PAGE);

        // Header
        JPanel headerWrap = new JPanel();
        headerWrap.setLayout(new BoxLayout(headerWrap, BoxLayout.Y_AXIS));
        headerWrap.setBackground(GREEN_DARK);

        JPanel header = new JPanel(new BorderLayout(10, 0));
        header.setBackground(GREEN_DARK);
        header.setBorder(new EmptyBorder(10, 16, 10, 16));

        JButton btnBack = new JButton("< Inapoi");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnBack.setForeground(ALB);
        btnBack.setBackground(GREEN_PRIMARY);
        btnBack.setBorder(new EmptyBorder(6, 14, 6, 14));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> nav.showPage("home"));
        header.add(btnBack, BorderLayout.WEST);

        JLabel lblHeader = new JLabel("Rezultate cautare");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblHeader.setForeground(ALB);
        header.add(lblHeader, BorderLayout.CENTER);

        // Search bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        searchBar.setBackground(new Color(0, 80, 30));

        JLabel lblOras = new JLabel("Destinatie:");
        lblOras.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblOras.setForeground(ALB);
        searchBar.add(lblOras);

        JTextField campOras = new JTextField(18);
        campOras.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campOras.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 60, 20), 1),
            new EmptyBorder(5, 10, 5, 10)));
        campOras.setToolTipText("ex: Cluj, Brasov, Bucuresti...");
        searchBar.add(campOras);

        JButton btnCauta = new JButton("Cauta >");
        btnCauta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCauta.setBackground(new Color(255, 160, 0));
        btnCauta.setForeground(new Color(20, 20, 20));
        btnCauta.setFocusPainted(false);
        btnCauta.setBorder(new EmptyBorder(7, 20, 7, 20));
        btnCauta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCauta.addActionListener(e -> {
            String city = campOras.getText().trim();
            if (city.isEmpty()) {
                JOptionPane.showMessageDialog(page, "Introdu o destinatie.", "Atentie",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            List<String> userTags2 = Session.isLoggedIn()
                ? Session.getUser().getTags() : List.of("istorie", "natura", "cultura");
            String tags = String.join(",", userTags2);
            // Actualizează și hotel_input.txt cu noul oraș (păstrând datele din sidebar)
            ScraperService.updateHotelCity(city);
            nav.runSearch(city, tags);
        });
        searchBar.add(btnCauta);

        headerWrap.add(header);
        headerWrap.add(searchBar);
        page.add(headerWrap, BorderLayout.NORTH);

        // Continut
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(16, 20, 20, 20));

        // 1) Cazari (5 hoteluri)
        content.add(UIHelpers.createSectionTitle("Cazari gasite"));
        content.add(Box.createVerticalStrut(10));
        List<JSONObject> hotels = loadHotels();
        if (hotels.isEmpty()) {
            content.add(buildEmptyPlaceholder(
                "Niciun hotel disponibil. Apasa 'Cauta sejur' pentru a rula scraperul."));
        } else {
            int shown = 0;
            for (JSONObject h : hotels) {
                if (shown >= 5) break;
                content.add(buildHotelCard(h, nav));
                content.add(Box.createVerticalStrut(10));
                shown++;
            }
        }

        // 2) Atractii: 5 total, distribuite pe categoriile alese (2+2+1)
        content.add(Box.createVerticalStrut(16));
        content.add(UIHelpers.createSectionTitle("Atractii in zona"));
        content.add(Box.createVerticalStrut(10));

        AttractionData ad = loadAttractions();
        List<String> userTags = Session.isLoggedIn()
            ? Session.getUser().getTags() : List.of();
        List<JSONObject> attractionPicks = pickDistributed(
            ad.attractionsByCategory, userTags, 5);

        if (attractionPicks.isEmpty()) {
            content.add(buildEmptyPlaceholder(
                "Nicio atractie disponibila inca pentru categoriile alese."));
        } else {
            for (JSONObject a : attractionPicks) {
                content.add(buildAttractionCard(a, nav));
                content.add(Box.createVerticalStrut(10));
            }
        }

        // 3) Restaurante (3)
        content.add(Box.createVerticalStrut(16));
        content.add(UIHelpers.createSectionTitle("Restaurante recomandate"));
        content.add(Box.createVerticalStrut(10));

        if (ad.restaurants.isEmpty()) {
            content.add(buildEmptyPlaceholder("Niciun restaurant disponibil."));
        } else {
            int shown = 0;
            for (JSONObject r : ad.restaurants) {
                if (shown >= 3) break;
                content.add(buildAttractionCard(r, nav));
                content.add(Box.createVerticalStrut(10));
                shown++;
            }
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_PAGE);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.getVerticalScrollBar().setBlockIncrement(60);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    // =====================================================================
    // Distribution: 5 attractions across user's chosen categories (2+2+1)
    // =====================================================================
    private static List<JSONObject> pickDistributed(
            Map<String, List<JSONObject>> byCategory,
            List<String> userCategories,
            int total) {

        List<JSONObject> result = new ArrayList<>();

        // Use the user's chosen categories first; if none configured,
        // fall back to whatever categories the scraper returned.
        List<String> categories = new ArrayList<>();
        if (userCategories != null) {
            for (String c : userCategories) {
                if (byCategory.containsKey(c) && !byCategory.get(c).isEmpty()) {
                    categories.add(c);
                }
            }
        }
        if (categories.isEmpty()) {
            for (Map.Entry<String, List<JSONObject>> e : byCategory.entrySet()) {
                if (!e.getValue().isEmpty()) categories.add(e.getKey());
            }
        }
        if (categories.isEmpty()) return result;

        // Compute quota per category (e.g. 5 across 3 -> 2,2,1)
        int n = categories.size();
        int base   = total / n;
        int extras = total % n;

        Map<String, Integer> cursor = new LinkedHashMap<>();
        for (String c : categories) cursor.put(c, 0);

        for (int i = 0; i < n && result.size() < total; i++) {
            String c = categories.get(i);
            int quota = base + (i < extras ? 1 : 0);
            List<JSONObject> bucket = byCategory.get(c);
            for (int j = 0; j < quota && cursor.get(c) < bucket.size(); j++) {
                result.add(bucket.get(cursor.get(c)));
                cursor.put(c, cursor.get(c) + 1);
            }
        }

        // Top-up round-robin if some buckets were small
        outer:
        while (result.size() < total) {
            boolean addedAny = false;
            for (String c : categories) {
                List<JSONObject> bucket = byCategory.get(c);
                int idx = cursor.get(c);
                if (idx < bucket.size()) {
                    result.add(bucket.get(idx));
                    cursor.put(c, idx + 1);
                    addedAny = true;
                    if (result.size() >= total) break outer;
                }
            }
            if (!addedAny) break;
        }
        return result;
    }

    // =====================================================================
    // JSON loaders
    // =====================================================================
    private static class AttractionData {
        final Map<String, List<JSONObject>> attractionsByCategory = new LinkedHashMap<>();
        final List<JSONObject>              restaurants           = new ArrayList<>();
    }

    private static AttractionData loadAttractions() {
        AttractionData data = new AttractionData();
        if (!Files.exists(ATRACTII_JSON)) return data;
        try {
            String text = Files.readString(ATRACTII_JSON, StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(text);

            JSONArray atr = root.optJSONArray("attractions");
            if (atr != null) {
                for (int i = 0; i < atr.length(); i++) {
                    JSONObject o = atr.getJSONObject(i);
                    String osmCategory = o.optString("category", "");
                    String userCat     = mapOsmCategoryToUserTag(osmCategory);
                    data.attractionsByCategory
                        .computeIfAbsent(userCat, k -> new ArrayList<>())
                        .add(o);
                }
            }

            JSONArray rest = root.optJSONArray("restaurants");
            if (rest != null) {
                for (int i = 0; i < rest.length(); i++) {
                    data.restaurants.add(rest.getJSONObject(i));
                }
            }
        } catch (IOException | JSONException e) {
            System.err.println("[SearchPage] Could not parse atractii_filtered.json: " + e);
        }
        return data;
    }

    /**
     * Maps a raw OSM category string (returned by the scraper as the
     * "category" field) to one of the 10 user-facing tag keys defined in
     * overpassAPI.py's TAG_DICTIONARY.
     */
    private static String mapOsmCategoryToUserTag(String osm) {
        if (osm == null || osm.isEmpty()) return "alte";
        String c = osm.toLowerCase();
        if (c.equals("museum") || c.equals("archaeological_site") || c.equals("castle")
         || c.equals("ruins") || c.equals("memorial") || c.equals("fort"))
            return "istorie";
        if (c.equals("park") || c.equals("nature_reserve") || c.equals("garden")
         || c.equals("viewpoint") || c.equals("peak") || c.equals("waterfall")
         || c.equals("cave_entrance"))
            return "natura";
        if (c.equals("theme_park") || c.equals("water_park") || c.equals("climbing")
         || c.equals("miniature_golf") || c.equals("escape_game") || c.equals("trampoline_park"))
            return "aventura";
        if (c.equals("gallery") || c.equals("theatre") || c.equals("cinema")
         || c.equals("arts_centre") || c.equals("library") || c.equals("community_centre"))
            return "cultura";
        if (c.equals("place_of_worship") || c.equals("chapel") || c.equals("monastery")
         || c.equals("wayside_shrine"))
            return "religie";
        if (c.equals("artwork") || c.equals("attraction") || c.equals("tower")
         || c.equals("building") || c.equals("monument"))
            return "arhitectura";
        if (c.equals("amusement_arcade") || c.equals("bowling_alley") || c.equals("nightclub"))
            return "distractie";
        if (c.equals("sports_centre") || c.equals("stadium") || c.equals("swimming_pool")
         || c.equals("cycling") || c.equals("tennis"))
            return "sport";
        if (c.equals("mall") || c.equals("department_store") || c.equals("supermarket")
         || c.equals("marketplace"))
            return "cumparaturi";
        if (c.equals("spa") || c.equals("sauna") || c.equals("massage") || c.equals("beach"))
            return "relaxare";
        return "alte";
    }

    private static List<JSONObject> loadHotels() {
        List<JSONObject> list = new ArrayList<>();
        if (!Files.exists(HOTELS_JSON)) return list;
        try {
            String text = Files.readString(HOTELS_JSON, StandardCharsets.UTF_8);
            JSONArray arr = new JSONArray(text);
            for (int i = 0; i < arr.length(); i++) {
                list.add(arr.getJSONObject(i));
            }
        } catch (IOException | JSONException e) {
            System.err.println("[SearchPage] Could not parse hotels_filtered.json: " + e);
        }
        return Collections.unmodifiableList(list);
    }

    // =====================================================================
    // Card builders
    // =====================================================================
    private static JPanel buildHotelCard(JSONObject h, AppNavigator nav) {
        String name      = h.optString("name", "Hotel necunoscut");
        String hotelClass = h.optString("hotel_class", "");
        String rate      = h.optString("rate_per_night", "N/A");
        String rating    = String.valueOf(h.opt("rating") != null ? h.opt("rating") : "N/A");
        String imageUrl  = h.optString("image", "N/A");
        return UIHelpers.createHotelCard(name, hotelClass, rate, rating, imageUrl, nav);
    }

    private static JPanel buildAttractionCard(JSONObject a, AppNavigator nav) {
        String name     = a.optString("name", "Atractie");
        String category = a.optString("category", "—");
        String addr     = a.optString("addr_street", "N/A");
        String imageUrl = a.optString("imagine", "N/A");
        String desc     = a.optString("descriere_wiki", "N/A");
        return UIHelpers.createPlaceCard(name, category, addr, desc, imageUrl);
    }

    private static JPanel buildEmptyPlaceholder(String message) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(ALB);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(14, 16, 14, 16)));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel l = new JLabel(message);
        l.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        l.setForeground(GRI_TEXT);
        p.add(l);
        return p;
    }
}
