package pck1;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class Sidebar extends JPanel implements AppColors {

    private static final Path HOTEL_INPUT = Paths.get("input", "hotel_input.txt");

    private JTextField locatieField;
    // Afisaj date (read-only) + valoarea retinuta
    private JTextField displayInceput;
    private JTextField displayFinal;
    private LocalDate  dateInceput;
    private LocalDate  dateFinal;

    private JSpinner   adulti;
    private JSpinner   copii;
    private JPanel     varstePanel;
    private final List<JSpinner> varsteSpinners = new ArrayList<>();

    public Sidebar(AppNavigator nav) {
        setLayout(new BorderLayout());
        setBackground(SIDEBAR_BG);
        setPreferredSize(new Dimension(222, 0));

        // Valori default: check-in peste 7 zile, check-out peste 14
        dateInceput = LocalDate.now().plusDays(7);
        dateFinal   = LocalDate.now().plusDays(14);

        JPanel searchSection = new JPanel();
        searchSection.setLayout(new BoxLayout(searchSection, BoxLayout.Y_AXIS));
        searchSection.setBackground(SIDEBAR_BG);
        searchSection.setBorder(new EmptyBorder(14, 12, 10, 12));

        JLabel searchLabel = new JLabel("CAUTA SEJUR");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 9));
        searchLabel.setForeground(new Color(100, 160, 100));
        searchLabel.setBorder(new EmptyBorder(0, 4, 10, 0));
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchSection.add(searchLabel);

        searchSection.add(buildFieldLabel("Destinatie / Locatie"));
        locatieField = buildTextField("ex: Paris, Iasi...");
        searchSection.add(locatieField);
        searchSection.add(Box.createVerticalStrut(8));

        // Autocomplete din istoricul sesiunii (non-focusabil = nu fura focus)
        JPopupMenu destPopup = new JPopupMenu();
        destPopup.setFocusable(false);
        locatieField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE
                 || e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    destPopup.setVisible(false);
                    return;
                }
                String typed = locatieField.getText().trim();
                if (typed.equals("ex: Paris, Iasi...") || typed.isEmpty()) {
                    destPopup.setVisible(false); return;
                }
                destPopup.removeAll();
                java.util.List<String> matches = Session.getHistorySuggestions(typed);
                if (matches.isEmpty()) { destPopup.setVisible(false); return; }
                for (String city : matches) {
                    JMenuItem item = new JMenuItem("> " + city);
                    item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    item.setFocusable(false);
                    item.addActionListener(ev -> {
                        locatieField.setText(city);
                        locatieField.setForeground(ALB);
                        destPopup.setVisible(false);
                    });
                    destPopup.add(item);
                }
                destPopup.show(locatieField, 0, locatieField.getHeight());
                locatieField.requestFocusInWindow();
            }
        });

        // --- Calendar pickers ---
        searchSection.add(buildFieldLabel("Data inceput"));
        displayInceput = buildDateDisplayField(dateInceput);
        JPanel rowIn = buildCalendarRow(displayInceput, true);
        searchSection.add(rowIn);
        searchSection.add(Box.createVerticalStrut(8));

        searchSection.add(buildFieldLabel("Data final"));
        displayFinal = buildDateDisplayField(dateFinal);
        JPanel rowFin = buildCalendarRow(displayFinal, false);
        searchSection.add(rowFin);
        searchSection.add(Box.createVerticalStrut(8));

        // Adulti
        searchSection.add(buildFieldLabel("Adulti"));
        JPanel adultiRow = buildSpinnerRow();
        JLabel adultiIcon = new JLabel("Ad.");
        adultiIcon.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        adultiRow.add(adultiIcon, BorderLayout.WEST);
        adulti = new JSpinner(new SpinnerNumberModel(2, 1, 20, 1));
        styleSpinner(adulti);
        adultiRow.add(adulti, BorderLayout.CENTER);
        searchSection.add(adultiRow);
        searchSection.add(Box.createVerticalStrut(8));

        // Copii
        searchSection.add(buildFieldLabel("Copii (sub 18 ani)"));
        JPanel copiiRow = buildSpinnerRow();
        JLabel copiiIcon = new JLabel("Co.");
        copiiIcon.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        copiiRow.add(copiiIcon, BorderLayout.WEST);
        copii = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        styleSpinner(copii);
        copiiRow.add(copii, BorderLayout.CENTER);
        searchSection.add(copiiRow);
        searchSection.add(Box.createVerticalStrut(8));

        searchSection.add(buildFieldLabel("Varste copii"));
        varstePanel = new JPanel();
        varstePanel.setLayout(new BoxLayout(varstePanel, BoxLayout.Y_AXIS));
        varstePanel.setBackground(SIDEBAR_BG);
        varstePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchSection.add(varstePanel);
        searchSection.add(Box.createVerticalStrut(4));

        copii.addChangeListener(e -> updateVarsteCopiiForms());
        updateVarsteCopiiForms();

        JScrollPane fieldsScroll = new JScrollPane(searchSection);
        fieldsScroll.setBorder(null);
        fieldsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        fieldsScroll.getViewport().setBackground(SIDEBAR_BG);
        fieldsScroll.getVerticalScrollBar().setUnitIncrement(10);

        JButton btnCauta = new JButton("  Cauta sejur  >");
        btnCauta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCauta.setBackground(GREEN_PRIMARY);
        btnCauta.setForeground(ALB);
        btnCauta.setBorder(new EmptyBorder(12, 0, 12, 0));
        btnCauta.setFocusPainted(false);
        btnCauta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCauta.setOpaque(true);
        btnCauta.addActionListener(e -> {
            String city = locatieField.getText().trim();
            if (city.isEmpty() || city.equals("ex: Paris, Iasi...")) {
                JOptionPane.showMessageDialog(this,
                    "Introdu o destinatie inainte de a cauta.",
                    "Cautare", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!dateFinal.isAfter(dateInceput)) {
                JOptionPane.showMessageDialog(this,
                    "Data de sfarsit trebuie sa fie dupa data de inceput.",
                    "Date invalide", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int nrAdulti = (Integer) adulti.getValue();
            int nrCopii  = (Integer) copii.getValue();
            String childrenAges = collectChildrenAges();
            writeHotelInput(city, dateInceput.toString(), dateFinal.toString(),
                            nrAdulti, nrCopii, childrenAges);
            String tags = "istorie,natura,cultura";
            if (Session.isLoggedIn()) {
                List<String> userTags = Session.getUser().getTags();
                if (userTags != null && !userTags.isEmpty()) {
                    tags = String.join(",", userTags);
                }
            }
            Session.addToHistory(city);
            nav.clearSidebarSelection();
            nav.runSearch(city, tags);
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(SIDEBAR_BG);
        centerPanel.add(fieldsScroll, BorderLayout.CENTER);
        centerPanel.add(btnCauta,    BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // Rezervari recente
        JPanel rezSection = new JPanel();
        rezSection.setLayout(new BoxLayout(rezSection, BoxLayout.Y_AXIS));
        rezSection.setBackground(SIDEBAR_BG);
        rezSection.setBorder(new EmptyBorder(0, 0, 12, 0));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(20, 60, 20));
        sep.setMaximumSize(new Dimension(222, 1));
        rezSection.add(sep);
        rezSection.add(Box.createVerticalStrut(10));

        JLabel secLabel = new JLabel("  REZERVARI RECENTE");
        secLabel.setFont(new Font("Segoe UI", Font.BOLD, 9));
        secLabel.setForeground(new Color(100, 160, 100));
        secLabel.setBorder(new EmptyBorder(0, 12, 6, 0));
        rezSection.add(secLabel);

        rezSection.add(createSidebarCard("Hotel Traian",  "15-18 Mai", "Confirmata"));
        rezSection.add(Box.createVerticalStrut(4));
        rezSection.add(createSidebarCard("Copou View",    "2-5 Iun",   "Asteptare"));
        rezSection.add(Box.createVerticalStrut(4));
        rezSection.add(createSidebarCard("Casa Pogor",    "10 Iul",    "Confirmata"));

        add(rezSection, BorderLayout.SOUTH);
    }

    // ── Calendar picker ──────────────────────────────────────────────────────

    /** Rand cu afisaj data (read-only) + buton calendar */
    private JPanel buildCalendarRow(JTextField display, boolean isStart) {
        JPanel row = new JPanel(new BorderLayout(2, 0));
        row.setBackground(SIDEBAR_BG);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCal = new JButton("Cal");
        btnCal.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnCal.setBackground(new Color(30, 70, 30));
        btnCal.setForeground(ALB);
        btnCal.setBorder(new EmptyBorder(4, 6, 4, 6));
        btnCal.setFocusPainted(false);
        btnCal.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCal.addActionListener(e -> {
            LocalDate initial = isStart ? dateInceput : dateFinal;
            LocalDate chosen  = showCalendarDialog(SwingUtilities.getWindowAncestor(this), initial);
            if (chosen != null) {
                if (isStart) {
                    dateInceput = chosen;
                    displayInceput.setText(formatDate(chosen));
                } else {
                    dateFinal = chosen;
                    displayFinal.setText(formatDate(chosen));
                }
            }
        });

        row.add(display,  BorderLayout.CENTER);
        row.add(btnCal,   BorderLayout.EAST);
        return row;
    }

    /** Afisaj read-only pentru data selectata */
    private JTextField buildDateDisplayField(LocalDate date) {
        JTextField field = new JTextField(formatDate(date));
        field.setEditable(false);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setForeground(ALB);
        field.setBackground(new Color(18, 42, 18));
        field.setCaretColor(ALB);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 80, 40), 1),
            new EmptyBorder(6, 8, 6, 8)));
        return field;
    }

    /** Deschide un dialog cu calendar vizual, returneaza data aleasa sau null */
    private LocalDate showCalendarDialog(Window owner, LocalDate initial) {
        LocalDate[] result = {null};
        JDialog dlg = new JDialog(owner, "Alege data", Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.setResizable(false);

        LocalDate[] view = {initial.withDayOfMonth(1)}; // prima zi a lunii afisate

        JPanel root = new JPanel(new BorderLayout(0, 6));
        root.setBackground(new Color(20, 48, 20));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Navigare luna ---
        JPanel navRow = new JPanel(new BorderLayout());
        navRow.setBackground(new Color(20, 48, 20));

        JButton btnPrev = navBtn("<");
        JButton btnNext = navBtn(">");
        JLabel  lblLuna = new JLabel("", SwingConstants.CENTER);
        lblLuna.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLuna.setForeground(ALB);

        JPanel calGrid = new JPanel(new GridLayout(0, 7, 2, 2));
        calGrid.setBackground(new Color(20, 48, 20));

        // Closure pentru redesenare
        Runnable[] refresh = {null};
        refresh[0] = () -> {
            String luna = view[0].getMonth().getDisplayName(TextStyle.FULL, new Locale("ro"));
            lblLuna.setText(luna.substring(0, 1).toUpperCase() + luna.substring(1)
                            + "  " + view[0].getYear());
            calGrid.removeAll();

            // Antet zile
            String[] zile = {"Lu", "Ma", "Mi", "Jo", "Vi", "Sa", "Du"};
            for (String z : zile) {
                JLabel h = new JLabel(z, SwingConstants.CENTER);
                h.setFont(new Font("Segoe UI", Font.BOLD, 11));
                h.setForeground(new Color(100, 180, 100));
                calGrid.add(h);
            }

            // Zile goale pana la prima zi a lunii (Monday=1)
            int firstDow = view[0].getDayOfWeek().getValue(); // 1=Lun ... 7=Dum
            for (int i = 1; i < firstDow; i++) calGrid.add(new JLabel());

            int daysInMonth = view[0].lengthOfMonth();
            LocalDate today = LocalDate.now();
            for (int d = 1; d <= daysInMonth; d++) {
                LocalDate day = view[0].withDayOfMonth(d);
                JButton btn = new JButton(String.valueOf(d));
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                btn.setFocusPainted(false);
                btn.setBorder(new EmptyBorder(4, 2, 4, 2));
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                if (day.equals(initial)) {
                    btn.setBackground(GREEN_PRIMARY);
                    btn.setForeground(ALB);
                } else if (day.equals(today)) {
                    btn.setBackground(new Color(255, 200, 50));
                    btn.setForeground(new Color(20, 20, 20));
                } else {
                    btn.setBackground(new Color(30, 65, 30));
                    btn.setForeground(ALB);
                }

                btn.addActionListener(e -> {
                    result[0] = day;
                    dlg.dispose();
                });
                calGrid.add(btn);
            }
            calGrid.revalidate();
            calGrid.repaint();
        };

        JButton btnPrevYear = navBtn("«");
        JButton btnNextYear = navBtn("»");

        btnPrevYear.addActionListener(e -> { view[0] = view[0].minusYears(1);  refresh[0].run(); });
        btnPrev.addActionListener(    e -> { view[0] = view[0].minusMonths(1); refresh[0].run(); });
        btnNext.addActionListener(    e -> { view[0] = view[0].plusMonths(1);  refresh[0].run(); });
        btnNextYear.addActionListener(e -> { view[0] = view[0].plusYears(1);   refresh[0].run(); });

        // Click pe an -> dialog rapid pentru introducere an
        lblLuna.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLuna.setToolTipText("Click pentru a alege anul");
        lblLuna.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                String input = JOptionPane.showInputDialog(dlg,
                    "Introdu anul:", String.valueOf(view[0].getYear()));
                if (input != null) {
                    try {
                        int yr = Integer.parseInt(input.trim());
                        if (yr >= 2020 && yr <= 2050) {
                            view[0] = view[0].withYear(yr);
                            refresh[0].run();
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
        });

        JPanel leftNav  = new JPanel(new FlowLayout(FlowLayout.LEFT,  2, 0));
        JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        leftNav.setBackground(new Color(20, 48, 20));
        rightNav.setBackground(new Color(20, 48, 20));
        leftNav.add(btnPrevYear);
        leftNav.add(btnPrev);
        rightNav.add(btnNext);
        rightNav.add(btnNextYear);

        navRow.add(leftNav,  BorderLayout.WEST);
        navRow.add(lblLuna,  BorderLayout.CENTER);
        navRow.add(rightNav, BorderLayout.EAST);

        root.add(navRow,  BorderLayout.NORTH);
        root.add(calGrid, BorderLayout.CENTER);

        refresh[0].run();
        dlg.setContentPane(root);
        dlg.pack();
        dlg.setLocationRelativeTo(owner);
        dlg.setVisible(true); // blocat pana inchide
        return result[0];
    }

    private JButton navBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(new Color(0, 80, 30));
        b.setForeground(ALB);
        b.setBorder(new EmptyBorder(4, 12, 4, 12));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private static String formatDate(LocalDate d) {
        return String.format("%02d/%02d/%d", d.getDayOfMonth(), d.getMonthValue(), d.getYear());
    }

    // ── Restul metodelor ─────────────────────────────────────────────────────

    private String collectChildrenAges() {
        if (varsteSpinners.isEmpty()) return "";
        List<String> ages = new ArrayList<>();
        for (JSpinner sp : varsteSpinners) ages.add(String.valueOf(sp.getValue()));
        return String.join(",", ages);
    }

    private void writeHotelInput(String city, String checkIn, String checkOut,
                                  int adults, int children, String childrenAges) {
        try {
            Files.createDirectories(HOTEL_INPUT.getParent());
            try (PrintWriter w = new PrintWriter(
                    Files.newBufferedWriter(HOTEL_INPUT, StandardCharsets.UTF_8))) {
                w.println("city=" + city);
                w.println("check_in=" + checkIn);
                w.println("check_out=" + checkOut);
                w.println("adults=" + adults);
                w.println("children=" + children);
                if (children > 0 && !childrenAges.isBlank())
                    w.println("children_ages=" + childrenAges);
                w.println("currency=EUR");
            }
        } catch (IOException ex) {
            System.err.println("[Sidebar] Could not write hotel_input.txt: " + ex.getMessage());
        }
    }

    private void updateVarsteCopiiForms() {
        varstePanel.removeAll();
        varsteSpinners.clear();
        int nrCopii = (Integer) copii.getValue();
        if (nrCopii == 0) {
            JLabel lblNone = new JLabel("Niciun copil selectat");
            lblNone.setFont(new Font("Segoe UI", Font.ITALIC, 10));
            lblNone.setForeground(new Color(80, 120, 80));
            lblNone.setBorder(new EmptyBorder(2, 4, 4, 0));
            varstePanel.add(lblNone);
        } else {
            for (int i = 1; i <= nrCopii; i++) {
                JPanel row = new JPanel(new BorderLayout(6, 0));
                row.setBackground(new Color(14, 34, 14));
                row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(35, 70, 35), 1),
                    new EmptyBorder(3, 8, 3, 8)));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                row.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel lblCopil = new JLabel("Copil " + i + ":");
                lblCopil.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblCopil.setForeground(new Color(140, 190, 140));
                row.add(lblCopil, BorderLayout.WEST);

                JSpinner spVarsta = new JSpinner(new SpinnerNumberModel(5, 0, 17, 1));
                styleSpinner(spVarsta);
                varsteSpinners.add(spVarsta);
                JLabel lblAni = new JLabel(" ani");
                lblAni.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblAni.setForeground(new Color(120, 160, 120));

                JPanel rightPart = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                rightPart.setBackground(new Color(14, 34, 14));
                rightPart.add(spVarsta);
                rightPart.add(lblAni);
                row.add(rightPart, BorderLayout.EAST);

                varstePanel.add(row);
                if (i < nrCopii) varstePanel.add(Box.createVerticalStrut(3));
            }
        }
        varstePanel.revalidate();
        varstePanel.repaint();
    }

    private JLabel buildFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(140, 190, 140));
        lbl.setBorder(new EmptyBorder(0, 2, 3, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField buildTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setForeground(new Color(120, 160, 120));
        field.setBackground(new Color(18, 42, 18));
        field.setCaretColor(GREEN_LIGHT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 80, 40), 1),
            new EmptyBorder(6, 8, 6, 8)));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(ALB);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(120, 160, 120));
                }
            }
        });
        return field;
    }

    private JPanel buildSpinnerRow() {
        JPanel row = new JPanel(new BorderLayout(6, 0));
        row.setBackground(new Color(18, 42, 18));
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 80, 40), 1),
            new EmptyBorder(4, 8, 4, 8)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        return row;
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Segoe UI", Font.BOLD, 13));
        spinner.setBackground(new Color(18, 42, 18));
        spinner.setForeground(ALB);
        spinner.setBorder(BorderFactory.createEmptyBorder());
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(new Color(18, 42, 18));
            tf.setForeground(ALB);
            tf.setFont(new Font("Segoe UI", Font.BOLD, 13));
            tf.setBorder(new EmptyBorder(0, 4, 0, 0));
        }
    }

    private JPanel createSidebarCard(String hotel, String date, String status) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(SIDEBAR_SEC);
        card.setBorder(new EmptyBorder(8, 16, 8, 16));
        card.setMaximumSize(new Dimension(205, 72));

        JLabel lblHotel = new JLabel(hotel);
        lblHotel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblHotel.setForeground(ALB);

        JLabel lblDate = new JLabel(date);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDate.setForeground(new Color(150, 200, 150));

        Color sColor = status.equals("Confirmata")
            ? new Color(80, 220, 100) : new Color(255, 180, 40);
        JLabel lblStatus = new JLabel("● " + status);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblStatus.setForeground(sColor);

        card.add(lblHotel);
        card.add(Box.createVerticalStrut(2));
        card.add(lblDate);
        card.add(Box.createVerticalStrut(2));
        card.add(lblStatus);
        return card;
    }
}
