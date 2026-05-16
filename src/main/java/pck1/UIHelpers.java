package pck1;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class UIHelpers implements AppColors {

    // =========================================================================
    // Card hotel: imagine din URL, pret, buton Rezerva
    // =========================================================================
    public static JPanel createHotelCard(String name, String hotelClass, String price,
                                          String rating, String imageUrl, AppNavigator nav) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_PAGE);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        // Zona imagine
        JLabel imgLbl = buildImageLabel(imageUrl, "🏨", new Color(60, 130, 180), 160, 170);
        card.add(imgLbl, BorderLayout.WEST);

        // Info centru
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(ALB);
        info.setBorder(new EmptyBorder(14, 16, 14, 10));

        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblName.setForeground(new Color(15, 15, 15));
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblClass = new JLabel("📍 " + (hotelClass.isEmpty() ? "Cazare" : hotelClass));
        lblClass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblClass.setForeground(new Color(0, 100, 180));
        lblClass.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel ratingRow = buildRatingRow(rating, "—");

        info.add(lblName);
        info.add(Box.createVerticalStrut(4));
        info.add(lblClass);
        info.add(Box.createVerticalStrut(6));
        info.add(ratingRow);
        card.add(info, BorderLayout.CENTER);

        // Panou dreapta: pret + buton
        JPanel right = buildPricePanel(name, hotelClass, "", rating, price, nav);
        card.add(right, BorderLayout.EAST);

        wrapper.add(card, BorderLayout.CENTER);
        wrapper.add(buildShadow(), BorderLayout.SOUTH);
        return wrapper;
    }

    // =========================================================================
    // Card atractie / restaurant: imagine din URL, fara pret, fara buton Rezerva
    // =========================================================================
    public static JPanel createPlaceCard(String name, String category,
                                          String address, String description,
                                          String imageUrl) {
        boolean hasDesc = description != null && !description.isBlank() && !description.equals("N/A");
        int cardH = hasDesc ? 190 : 150;

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_PAGE);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardH));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        // Zona imagine
        JLabel imgLbl = buildImageLabel(imageUrl, "🏛️", new Color(40, 140, 80), 160, cardH);
        card.add(imgLbl, BorderLayout.WEST);

        // Info centru
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(ALB);
        info.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblName.setForeground(new Color(15, 15, 15));
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblCat = new JLabel("🏷 " + category);
        lblCat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCat.setForeground(new Color(80, 130, 80));
        lblCat.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblAddr = new JLabel("📍 " + (address.equals("N/A") ? "Adresa nedisponibila" : address));
        lblAddr.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAddr.setForeground(GRI_TEXT);
        lblAddr.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(lblName);
        info.add(Box.createVerticalStrut(4));
        info.add(lblCat);
        info.add(Box.createVerticalStrut(3));
        info.add(lblAddr);

        if (hasDesc) {
            info.add(Box.createVerticalStrut(7));
            JLabel lblDesc = new JLabel(
                "<html><body style='width:420px; color:#555; font-size:11pt;'>"
                + description + "</body></html>");
            lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
            info.add(lblDesc);
        }

        card.add(info, BorderLayout.CENTER);
        wrapper.add(card, BorderLayout.CENTER);
        wrapper.add(buildShadow(), BorderLayout.SOUTH);
        return wrapper;
    }

    // =========================================================================
    // Card generic (folosit in HomePage cu culori hardcodate, fara URL imagine)
    // =========================================================================
    public static JPanel createListingCard(String titlu, String locatie, String descriere,
                                           String rating, String nrRec, String pret,
                                           boolean topAles, Color imgColor, AppNavigator nav) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_PAGE);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 162));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        JPanel imgZone = new JPanel(new BorderLayout());
        imgZone.setPreferredSize(new Dimension(160, 162));
        imgZone.setBackground(imgColor);
        JLabel imgIcon = new JLabel("🏨", SwingConstants.CENTER);
        imgIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        imgZone.add(imgIcon, BorderLayout.CENTER);
        JLabel badge = new JLabel(topAles ? "  ★ Top ales  " : "  Proprietate  ", SwingConstants.CENTER);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(ALB);
        badge.setBackground(topAles ? GREEN_DARK : new Color(0, 0, 0, 80));
        badge.setOpaque(true);
        badge.setPreferredSize(new Dimension(160, 22));
        imgZone.add(badge, BorderLayout.NORTH);
        card.add(imgZone, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(ALB);
        info.setBorder(new EmptyBorder(14, 16, 14, 10));

        JLabel lblT = new JLabel(titlu);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblT.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblL = new JLabel("📍 " + locatie);
        lblL.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblL.setForeground(new Color(0, 100, 180));
        lblL.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel ratingRow = buildRatingRow(rating, nrRec);
        JLabel lblD = new JLabel("<html><body style='width:300px;color:#6b6b6b'>" + descriere + "</body></html>");
        lblD.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblD.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(lblT);
        info.add(Box.createVerticalStrut(3));
        info.add(lblL);
        info.add(Box.createVerticalStrut(5));
        info.add(ratingRow);
        info.add(Box.createVerticalStrut(6));
        info.add(lblD);
        card.add(info, BorderLayout.CENTER);
        card.add(buildPricePanel(titlu, locatie, descriere, rating, pret, nav), BorderLayout.EAST);

        wrapper.add(card, BorderLayout.CENTER);
        wrapper.add(buildShadow(), BorderLayout.SOUTH);
        return wrapper;
    }

    // =========================================================================
    // Header sectiune
    // =========================================================================
    public static JPanel createSectionTitle(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(GREEN_PRIMARY);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        panel.setBorder(new EmptyBorder(0, 14, 0, 0));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setForeground(ALB);
        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================================
    // Helpers private
    // =========================================================================

    /** Construieste un JLabel cu imagine incarcata async din URL sau placeholder colorat. */
    private static JLabel buildImageLabel(String imageUrl, String placeholderEmoji,
                                           Color placeholderColor, int w, int h) {
        JLabel lbl = new JLabel(placeholderEmoji, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        lbl.setForeground(new Color(255, 255, 255, 180));
        lbl.setBackground(placeholderColor);
        lbl.setOpaque(true);
        lbl.setPreferredSize(new Dimension(w, h));

        if (imageUrl != null && !imageUrl.equals("N/A") && !imageUrl.isBlank()) {
            new SwingWorker<ImageIcon, Void>() {
                @Override
                protected ImageIcon doInBackground() throws Exception {
                    HttpURLConnection conn = (HttpURLConnection) new URL(imageUrl).openConnection();
                    conn.setRequestProperty("User-Agent", "TravaleRo/1.0 (university project)");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(12000);
                    conn.connect();
                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) return null;
                    BufferedImage img = ImageIO.read(conn.getInputStream());
                    if (img == null) return null;
                    // Scalare cu crop centrat
                    double scaleX = (double) w / img.getWidth();
                    double scaleY = (double) h / img.getHeight();
                    double scale  = Math.max(scaleX, scaleY);
                    int sw = (int) (img.getWidth()  * scale);
                    int sh = (int) (img.getHeight() * scale);
                    int ox = (sw - w) / 2;
                    int oy = (sh - h) / 2;
                    BufferedImage cropped = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = cropped.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                       RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.drawImage(img.getScaledInstance(sw, sh, Image.SCALE_SMOOTH), -ox, -oy, null);
                    g.dispose();
                    return new ImageIcon(cropped);
                }
                @Override
                protected void done() {
                    try {
                        ImageIcon icon = get();
                        if (icon != null) {
                            lbl.setIcon(icon);
                            lbl.setText(null);
                            lbl.repaint();
                        }
                    } catch (Exception e) {
                        System.err.println("[UIHelpers] Image load failed for: " + imageUrl + " — " + e.getMessage());
                    }
                }
            }.execute();
        }
        return lbl;
    }

    private static JPanel buildRatingRow(String rating, String nrRec) {
        double ratingVal;
        try { ratingVal = Double.parseDouble(rating); } catch (NumberFormatException ex) { ratingVal = 0; }
        int stele = (int) Math.min(5, Math.max(0, Math.round(ratingVal)));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row.setBackground(ALB);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (ratingVal > 0) {
            JLabel scoreBox = new JLabel(" " + rating + " ");
            scoreBox.setFont(new Font("Segoe UI", Font.BOLD, 12));
            scoreBox.setForeground(ALB);
            scoreBox.setBackground(GREEN_DARK);
            scoreBox.setOpaque(true);
            row.add(scoreBox);
        }

        JLabel steleLabel = new JLabel("★".repeat(stele) + "☆".repeat(5 - stele));
        steleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        steleLabel.setForeground(ORANGE_STAR);
        row.add(steleLabel);

        if (!nrRec.equals("—")) {
            JLabel recLabel = new JLabel("· " + nrRec + " recenzii");
            recLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            recLabel.setForeground(GRI_TEXT);
            row.add(recLabel);
        }
        return row;
    }

    private static JPanel buildPricePanel(String titlu, String locatie, String descriere,
                                           String rating, String pret, AppNavigator nav) {
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(new Color(250, 252, 250));
        right.setPreferredSize(new Dimension(140, 0));
        right.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 1, 0, 0, new Color(230, 230, 230)),
            new EmptyBorder(14, 14, 14, 16)
        ));

        JLabel lblDisp = new JLabel("Disponibil");
        lblDisp.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDisp.setForeground(GREEN_PRIMARY);
        lblDisp.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel pretPanel = new JPanel();
        pretPanel.setLayout(new BoxLayout(pretPanel, BoxLayout.Y_AXIS));
        pretPanel.setBackground(new Color(250, 252, 250));

        // Afiseaza pretul curat (ex: "91 EUR" → "91€")
        String[] parts = pret.trim().split("\\s+");
        String suma = parts[0];
        String moneda = parts.length > 1 ? parts[1] : "€";
        String simbol = moneda.equals("EUR") ? "€" : moneda.equals("RON") ? " RON" : moneda;

        JLabel lblP = new JLabel(suma + simbol);
        lblP.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblP.setForeground(new Color(15, 15, 15));
        lblP.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JLabel lblN = new JLabel("/ noapte");
        lblN.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblN.setForeground(GRI_TEXT);
        lblN.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JLabel lblTaxe = new JLabel("Taxe incluse");
        lblTaxe.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblTaxe.setForeground(new Color(150, 150, 150));
        lblTaxe.setAlignmentX(Component.RIGHT_ALIGNMENT);

        pretPanel.add(Box.createVerticalGlue());
        pretPanel.add(lblP);
        pretPanel.add(lblN);
        pretPanel.add(Box.createVerticalStrut(2));
        pretPanel.add(lblTaxe);
        pretPanel.add(Box.createVerticalGlue());

        JButton btnRez = new JButton("Rezerva");
        btnRez.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRez.setBackground(GREEN_PRIMARY);
        btnRez.setForeground(ALB);
        btnRez.setBorder(new EmptyBorder(9, 0, 9, 0));
        btnRez.setFocusPainted(false);
        btnRez.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRez.addActionListener(e ->
            nav.navigateToDetail(titlu, locatie, descriere, rating, "—", pret));

        right.add(lblDisp,   BorderLayout.NORTH);
        right.add(pretPanel, BorderLayout.CENTER);
        right.add(btnRez,    BorderLayout.SOUTH);
        return right;
    }

    private static JPanel buildShadow() {
        JPanel shadow = new JPanel();
        shadow.setBackground(new Color(210, 210, 210));
        shadow.setPreferredSize(new Dimension(0, 2));
        return shadow;
    }
}
