package pck1;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RegisterPage implements AppColors {

    public static JTextField     campNume;
    public static JTextField     campPrenume;
    public static JTextField     campEmail;
    public static JPasswordField campParola;
    public static JTextField     campTelefon;
    public static List<JCheckBox> checkboxOptiuni = new ArrayList<>();
    private static final int MAX_SELECTATE = 3;
    private static JTextField lastField;

    public static JPanel build(AppNavigator nav) {
        JPanel page = new JPanel(new GridLayout(1, 2, 0, 0));

        // ── Stanga: branding ────────────────────────────────────────────
        JPanel leftSide = new JPanel(new GridBagLayout());
        leftSide.setBackground(GREEN_DARK);
        JPanel brand = new JPanel();
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBackground(GREEN_DARK);
        JLabel logoMare = new JLabel("<html><div style='text-align:center;'><span style='color:white;font-size:40pt;font-weight:bold;'>Travale</span><span style='color:#FFCC00;font-size:40pt;font-weight:bold;'>Ro</span></div></html>");
        logoMare.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel avion = new JLabel("✈", SwingConstants.CENTER);
        avion.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 46));
        avion.setForeground(new Color(180, 255, 180));
        avion.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel slogan = new JLabel("<html><div style='text-align:center;color:#AAFFAA;font-size:13pt;'>Alatura-te comunitatii TravaleRo<br>si descopera lumea.</div></html>");
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);
        brand.add(Box.createVerticalGlue());
        brand.add(logoMare);
        brand.add(Box.createVerticalStrut(8));
        brand.add(avion);
        brand.add(Box.createVerticalStrut(24));
        brand.add(slogan);
        brand.add(Box.createVerticalGlue());
        leftSide.add(brand);
        page.add(leftSide);

        // ── Dreapta: scroll + formular ──────────────────────────────────
        // BorderLayout in loc de GridBagLayout - scroll-ul ocupa tot spatiul
        JPanel rightSide = new JPanel(new BorderLayout());
        rightSide.setBackground(BG_PAGE);

        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(ALB);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(30, 44, 28, 44)));

        JLabel titluForm = new JLabel("Creeaza cont nou");
        titluForm.setFont(new Font("Segoe UI", Font.BOLD, 21));
        titluForm.setForeground(new Color(20, 20, 20));
        titluForm.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel subtitluForm = new JLabel("Completeaza datele de mai jos");
        subtitluForm.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitluForm.setForeground(GRI_TEXT);
        subtitluForm.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(titluForm);
        formCard.add(Box.createVerticalStrut(4));
        formCard.add(subtitluForm);
        formCard.add(Box.createVerticalStrut(22));

        // Rand Nume + Prenume
        JPanel numeRow = new JPanel(new GridLayout(1, 2, 10, 0));
        numeRow.setBackground(ALB);
        numeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        numeRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel numePanel = new JPanel();
        numePanel.setLayout(new BoxLayout(numePanel, BoxLayout.Y_AXIS));
        numePanel.setBackground(ALB);
        JLabel lblNume = new JLabel("Nume");
        lblNume.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNume.setForeground(GRI_TEXT);
        campNume = new JTextField();
        campNume.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campNume.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_C, 1), new EmptyBorder(8, 10, 8, 10)));
        numePanel.add(lblNume);
        numePanel.add(Box.createVerticalStrut(4));
        numePanel.add(campNume);
        JPanel prenumePanel = new JPanel();
        prenumePanel.setLayout(new BoxLayout(prenumePanel, BoxLayout.Y_AXIS));
        prenumePanel.setBackground(ALB);
        JLabel lblPrenume = new JLabel("Prenume");
        lblPrenume.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPrenume.setForeground(GRI_TEXT);
        campPrenume = new JTextField();
        campPrenume.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campPrenume.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_C, 1), new EmptyBorder(8, 10, 8, 10)));
        prenumePanel.add(lblPrenume);
        prenumePanel.add(Box.createVerticalStrut(4));
        prenumePanel.add(campPrenume);
        numeRow.add(numePanel);
        numeRow.add(prenumePanel);
        formCard.add(numeRow);
        formCard.add(Box.createVerticalStrut(14));

        // Email
        formCard.add(buildTextField("Adresa de email", false));
        campEmail = lastField;
        formCard.add(Box.createVerticalStrut(14));

        // Parola
        JPanel parolaPanel = new JPanel();
        parolaPanel.setLayout(new BoxLayout(parolaPanel, BoxLayout.Y_AXIS));
        parolaPanel.setBackground(ALB);
        parolaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblParola = new JLabel("Parola");
        lblParola.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblParola.setForeground(GRI_TEXT);
        lblParola.setAlignmentX(Component.LEFT_ALIGNMENT);
        campParola = new JPasswordField();
        campParola.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campParola.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_C, 1), new EmptyBorder(9, 11, 9, 11)));
        campParola.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campParola.setAlignmentX(Component.LEFT_ALIGNMENT);
        parolaPanel.add(lblParola);
        parolaPanel.add(Box.createVerticalStrut(5));
        parolaPanel.add(campParola);
        formCard.add(parolaPanel);
        formCard.add(Box.createVerticalStrut(14));

        // Telefon
        formCard.add(buildTextField("Numar de telefon", false));
        campTelefon = lastField;
        formCard.add(Box.createVerticalStrut(18));

        // Mini-meniu
        formCard.add(buildMiniMeniu());
        formCard.add(Box.createVerticalStrut(22));

        // Buton
        JButton btnCreaza = new JButton("Creeaza cont");
        btnCreaza.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCreaza.setBackground(GREEN_PRIMARY);
        btnCreaza.setForeground(ALB);
        btnCreaza.setBorder(new EmptyBorder(12, 0, 12, 0));
        btnCreaza.setFocusPainted(false);
        btnCreaza.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCreaza.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnCreaza.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCreaza.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(page, "Cont creat! Te poti autentifica acum.", "TravaleRo - Bun venit!", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        formCard.add(btnCreaza);
        formCard.add(Box.createVerticalStrut(14));

        // Link login
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setBackground(ALB);
        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        bottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblAiCont = new JLabel("Ai deja un cont?");
        lblAiCont.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAiCont.setForeground(GRI_TEXT);
        JButton btnSpreLogin = new JButton("Autentifica-te");
        btnSpreLogin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSpreLogin.setForeground(GREEN_PRIMARY);
        btnSpreLogin.setBackground(ALB);
        btnSpreLogin.setBorder(BorderFactory.createEmptyBorder());
        btnSpreLogin.setFocusPainted(false);
        btnSpreLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSpreLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { nav.showRoot("login"); }
        });
        bottomRow.add(lblAiCont, BorderLayout.WEST);
        bottomRow.add(btnSpreLogin, BorderLayout.EAST);
        formCard.add(bottomRow);

        // ── Wrapper care centreaza formCard in interiorul scroll-ului ──
        // Fara acest wrapper, formCard s-ar intinde pe toata latimea
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(BG_PAGE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        centerWrapper.add(formCard, gbc);

        // Scroll pane pe toata suprafata dreapta - apare bara doar cand e nevoie
        JScrollPane scroll = new JScrollPane(centerWrapper,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_PAGE);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        rightSide.add(scroll, BorderLayout.CENTER);
        page.add(rightSide);
        return page;
    }

    private static JPanel buildTextField(String labelText, boolean esteParola) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ALB);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(GRI_TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(5));
        JTextField camp = esteParola ? new JPasswordField() : new JTextField();
        camp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        camp.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_C, 1), new EmptyBorder(9, 11, 9, 11)));
        camp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        camp.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(camp);
        lastField = camp;
        return panel;
    }

    private static JPanel buildMiniMeniu() {
        checkboxOptiuni.clear();
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(ALB);
        container.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblMeniu = new JLabel("Alege pana la " + MAX_SELECTATE + " optiuni");
        lblMeniu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMeniu.setForeground(new Color(50, 50, 50));
        lblMeniu.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(lblMeniu);
        container.add(Box.createVerticalStrut(8));
        JPanel grid = new JPanel(new GridLayout(3, 3, 6, 6));
        grid.setBackground(new Color(248, 250, 248));
        grid.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_C, 1), new EmptyBorder(10, 12, 10, 12)));
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = 1; i <= 9; i++) {
            JCheckBox cb = new JCheckBox("Optiunea " + i);
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            cb.setForeground(new Color(40, 40, 40));
            cb.setBackground(new Color(248, 250, 248));
            cb.setFocusPainted(false);
            cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            checkboxOptiuni.add(cb);
            grid.add(cb);
        }
        ItemListener limitator = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                long bifate = 0;
                for (JCheckBox c : checkboxOptiuni) { if (c.isSelected()) bifate++; }
                for (JCheckBox c : checkboxOptiuni) { if (!c.isSelected()) c.setEnabled(bifate < MAX_SELECTATE); }
            }
        };
        for (JCheckBox cb : checkboxOptiuni) { cb.addItemListener(limitator); }
        container.add(grid);
        JLabel hint = new JLabel("(maxim " + MAX_SELECTATE + " pot fi selectate simultan)");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(GRI_TEXT);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(Box.createVerticalStrut(5));
        container.add(hint);
        return container;
    }
}
