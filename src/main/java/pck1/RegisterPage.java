package pck1;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import pck1.Proiect_PIP.DataBase_Utilizator;
import pck1.Proiect_PIP.Utilizator;

/**
 * Two-step registration:
 *   Step 1 - personal data (name, email, password, phone)
 *   Step 2 - pick exactly 3 travel-interest categories
 *
 * On submit, an Utilizator is built and inserted via DataBase_Utilizator.
 */
public class RegisterPage implements AppColors {

    private static final int REQUIRED_CATEGORIES = 3;

    public static JPanel build(AppNavigator nav) {
        CardLayout steps     = new CardLayout();
        JPanel     stepsHost = new JPanel(steps);

        // Shared fields between the two steps
        JTextField     campNume     = makeField();
        JTextField     campPrenume  = makeField();
        JTextField     campEmail    = makeField();
        JPasswordField campParola   = makePasswordField();
        JTextField     campTelefon  = makeField();

        CategoryPicker picker = new CategoryPicker(REQUIRED_CATEGORIES);

        stepsHost.add(buildStep1(nav, stepsHost, steps,
                                  campNume, campPrenume, campEmail,
                                  campParola, campTelefon), "step1");
        stepsHost.add(buildStep2(nav, stepsHost, steps, picker,
                                  campNume, campPrenume, campEmail,
                                  campParola, campTelefon), "step2");

        steps.show(stepsHost, "step1");
        return stepsHost;
    }

    // =====================================================================
    // STEP 1 - personal info form
    // =====================================================================
    private static JPanel buildStep1(AppNavigator nav, JPanel stepsHost, CardLayout steps,
                                      JTextField campNume, JTextField campPrenume,
                                      JTextField campEmail, JPasswordField campParola,
                                      JTextField campTelefon) {
        JPanel page = new JPanel(new GridLayout(1, 2, 0, 0));

        page.add(buildBrandingPane(
            "Alatura-te comunitatii TravaleRo<br>si descopera lumea."));

        JPanel rightSide = new JPanel(new GridBagLayout());
        rightSide.setBackground(BG_PAGE);

        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(ALB);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(28, 44, 26, 44)
        ));

        formCard.add(makeTitle("Creeaza cont nou"));
        formCard.add(Box.createVerticalStrut(4));
        formCard.add(makeSubtitle("Pasul 1 din 2  ·  Date personale"));
        formCard.add(Box.createVerticalStrut(20));

        JPanel numeRow = new JPanel(new GridLayout(1, 2, 10, 0));
        numeRow.setBackground(ALB);
        numeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        numeRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        numeRow.add(wrapField("Nume",    campNume));
        numeRow.add(wrapField("Prenume", campPrenume));
        formCard.add(numeRow);
        formCard.add(Box.createVerticalStrut(12));

        formCard.add(wrapField("Adresa de email",  campEmail));
        formCard.add(Box.createVerticalStrut(12));
        formCard.add(wrapField("Parola",           campParola));
        formCard.add(Box.createVerticalStrut(12));
        formCard.add(wrapField("Numar de telefon", campTelefon));
        formCard.add(Box.createVerticalStrut(22));

        JButton btnNext = new JButton("Continua  >");
        styleGreenButton(btnNext);
        btnNext.addActionListener(e -> {
            if (campNume.getText().trim().isEmpty()
             || campPrenume.getText().trim().isEmpty()
             || campEmail.getText().trim().isEmpty()
             || campParola.getPassword().length == 0) {
                JOptionPane.showMessageDialog(page,
                    "Completeaza nume, prenume, email si parola pentru a continua.",
                    "Date incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            steps.show(stepsHost, "step2");
        });
        formCard.add(btnNext);
        formCard.add(Box.createVerticalStrut(12));

        formCard.add(buildBottomLoginLink(nav));

        formCard.setPreferredSize(new Dimension(400, 540));
        rightSide.add(formCard);
        page.add(rightSide);
        return page;
    }

    // =====================================================================
    // STEP 2 - category picker
    // =====================================================================
    private static JPanel buildStep2(AppNavigator nav, JPanel stepsHost, CardLayout steps,
                                      CategoryPicker picker,
                                      JTextField campNume, JTextField campPrenume,
                                      JTextField campEmail, JPasswordField campParola,
                                      JTextField campTelefon) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BG_PAGE);
        page.setBorder(new EmptyBorder(28, 60, 28, 60));

        JPanel head = new JPanel();
        head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));
        head.setBackground(BG_PAGE);

        JLabel title = makeTitle("Alege-ti pasiunile de calator");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel sub   = makeSubtitle("Pasul 2 din 2  ·  Selecteaza exact "
                                     + REQUIRED_CATEGORIES + " categorii");
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        head.add(title);
        head.add(Box.createVerticalStrut(6));
        head.add(sub);
        head.add(Box.createVerticalStrut(18));
        page.add(head, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BG_PAGE);
        picker.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(20, 22, 20, 22)
        ));
        center.add(picker);
        page.add(center, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        footer.setBackground(BG_PAGE);

        JButton btnBack = new JButton("<  Inapoi");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnBack.setBackground(ALB);
        btnBack.setForeground(GREEN_DARK);
        btnBack.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(8, 18, 8, 18)));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> steps.show(stepsHost, "step1"));

        JButton btnSubmit = new JButton("Creeaza cont");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setBackground(GREEN_PRIMARY);
        btnSubmit.setForeground(ALB);
        btnSubmit.setBorder(new EmptyBorder(12, 24, 12, 24));
        btnSubmit.setFocusPainted(false);
        btnSubmit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSubmit.setEnabled(false);

        picker.setOnSelectionChanged(() -> btnSubmit.setEnabled(picker.isComplete()));

        btnSubmit.addActionListener(e -> {
            List<String> tags = picker.getSelectedTags();
            String numeComplet = campNume.getText().trim() + " "
                               + campPrenume.getText().trim();
            Utilizator u = new Utilizator(
                campEmail.getText().trim(),
                numeComplet,
                new String(campParola.getPassword()),
                campTelefon.getText().trim(),
                tags
            );
            try {
                DataBase_Utilizator.adauga_utilizator(u);
                JOptionPane.showMessageDialog(page,
                    "Cont creat cu succes! Te poti autentifica acum.",
                    "TravaleRo - Bun venit!", JOptionPane.INFORMATION_MESSAGE);
                nav.showRoot("login");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(page,
                    "Eroare la salvarea contului: " + ex.getMessage(),
                    "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        });

        footer.add(btnBack);
        footer.add(btnSubmit);
        page.add(footer, BorderLayout.SOUTH);
        return page;
    }

    // =====================================================================
    // helpers
    // =====================================================================
    private static JPanel buildBrandingPane(String sloganHtml) {
        JPanel leftSide = new JPanel(new GridBagLayout());
        leftSide.setBackground(GREEN_DARK);

        JPanel brand = new JPanel();
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBackground(GREEN_DARK);

        JLabel logoMare = new JLabel(
            "<html><div style='text-align:center;'>" +
            "<span style='color:white;font-size:40pt;font-weight:bold;'>Travale</span>" +
            "<span style='color:#FFCC00;font-size:40pt;font-weight:bold;'>Ro</span>" +
            "</div></html>"
        );
        logoMare.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel avion = new JLabel("✈", SwingConstants.CENTER);
        avion.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 46));
        avion.setForeground(new Color(180, 255, 180));
        avion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel slogan = new JLabel(
            "<html><div style='text-align:center;color:#AAFFAA;font-size:13pt;'>"
            + sloganHtml + "</div></html>"
        );
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        brand.add(Box.createVerticalGlue());
        brand.add(logoMare);
        brand.add(Box.createVerticalStrut(8));
        brand.add(avion);
        brand.add(Box.createVerticalStrut(24));
        brand.add(slogan);
        brand.add(Box.createVerticalGlue());
        leftSide.add(brand);
        return leftSide;
    }

    private static JPanel buildBottomLoginLink(AppNavigator nav) {
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
        btnSpreLogin.addActionListener(e -> nav.showRoot("login"));

        bottomRow.add(lblAiCont,    BorderLayout.WEST);
        bottomRow.add(btnSpreLogin, BorderLayout.EAST);
        return bottomRow;
    }

    private static JPanel wrapField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ALB);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(GRI_TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lbl);
        panel.add(Box.createVerticalStrut(4));
        panel.add(field);
        return panel;
    }

    private static JTextField makeField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(8, 10, 8, 10)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }

    private static JPasswordField makePasswordField() {
        JPasswordField f = new JPasswordField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(8, 10, 8, 10)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }

    private static JLabel makeTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 21));
        l.setForeground(new Color(20, 20, 20));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private static JLabel makeSubtitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(GRI_TEXT);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private static void styleGreenButton(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(GREEN_PRIMARY);
        b.setForeground(ALB);
        b.setBorder(new EmptyBorder(12, 24, 12, 24));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}
