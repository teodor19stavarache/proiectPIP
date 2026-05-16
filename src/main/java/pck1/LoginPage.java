package pck1;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import pck1.Proiect_PIP.DataBase_Utilizator;
import pck1.Proiect_PIP.Utilizator;

public class LoginPage implements AppColors {

    public static JPanel build(AppNavigator nav) {
        JPanel page = new JPanel(new GridLayout(1, 2, 0, 0));

        // Stanga - branding verde
        JPanel leftSide = new JPanel(new GridBagLayout());
        leftSide.setBackground(GREEN_PRIMARY);

        JPanel brand = new JPanel();
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBackground(GREEN_PRIMARY);

        JLabel logoMare = new JLabel(
            "<html><div style='text-align:center;'>" +
            "<span style='color:white;font-size:42pt;font-weight:bold;'>Travale</span>" +
            "<span style='color:#FFCC00;font-size:42pt;font-weight:bold;'>Ro</span>" +
            "</div></html>"
        );
        logoMare.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel avion = new JLabel("✈", SwingConstants.CENTER);
        avion.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        avion.setForeground(new Color(200, 255, 200));
        avion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel slogan = new JLabel(
            "<html><div style='text-align:center;color:#CCFFCC;font-size:13pt;'>" +
            "Planifica-ti urmatoarea aventura<br>din inima Iasului spre lume." +
            "</div></html>"
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
        page.add(leftSide);

        // Dreapta - formular
        JPanel rightSide = new JPanel(new GridBagLayout());
        rightSide.setBackground(BG_PAGE);

        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(ALB);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(40, 44, 32, 44)
        ));

        JLabel titluForm = new JLabel("Bine ai revenit");
        titluForm.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titluForm.setForeground(new Color(20, 20, 20));
        titluForm.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitluForm = new JLabel("Conecteaza-te la contul tau TravaleRo");
        subtitluForm.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitluForm.setForeground(GRI_TEXT);
        subtitluForm.setAlignmentX(Component.LEFT_ALIGNMENT);

        formCard.add(titluForm);
        formCard.add(Box.createVerticalStrut(4));
        formCard.add(subtitluForm);
        formCard.add(Box.createVerticalStrut(32));

        // Camp email
        JLabel lblEmail = new JLabel("Adresa de email");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEmail.setForeground(GRI_TEXT);
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(lblEmail);
        formCard.add(Box.createVerticalStrut(5));

        JTextField campEmail = new JTextField();
        campEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        campEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(campEmail);
        formCard.add(Box.createVerticalStrut(18));

        // Camp parola
        JLabel lblParola = new JLabel("Parola");
        lblParola.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblParola.setForeground(GRI_TEXT);
        lblParola.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(lblParola);
        formCard.add(Box.createVerticalStrut(5));

        JPasswordField campParola = new JPasswordField();
        campParola.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campParola.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        campParola.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campParola.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(campParola);
        formCard.add(Box.createVerticalStrut(28));

        // Buton login
        JButton btnLogin = new JButton("Intra in cont");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(GREEN_PRIMARY);
        btnLogin.setForeground(ALB);
        btnLogin.setBorder(new EmptyBorder(12, 0, 12, 0));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        java.awt.event.ActionListener doLogin = e -> {
            String email  = campEmail.getText().trim();
            String parola = new String(campParola.getPassword());
            if (email.isEmpty() || parola.isEmpty()) {
                JOptionPane.showMessageDialog(page,
                    "Introdu emailul si parola.",
                    "Date incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Build a stub user only for password verification
            Utilizator probe = new Utilizator(email, "", parola, "", new ArrayList<>());
            boolean ok;
            try {
                ok = DataBase_Utilizator.verifica_parola(probe);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(page,
                    "Eroare la conectarea la baza de date: " + ex.getMessage(),
                    "Eroare", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!ok) {
                JOptionPane.showMessageDialog(page,
                    "Email sau parola gresita.",
                    "Autentificare esuata", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Load full user record (including saved tags) into Session.
            // Requires getUtilizator(email) - to be added in DataBase_Utilizator.
            Utilizator full = DataBase_Utilizator.getUtilizator(email);
            if (full == null) full = probe;
            Session.login(full);
            nav.showRoot("app");
        };
        btnLogin.addActionListener(doLogin);
        campEmail.addActionListener(doLogin);
        campParola.addActionListener(doLogin);
        formCard.add(btnLogin);
        formCard.add(Box.createVerticalStrut(16));

        // Rand de jos: register stanga, forgot password dreapta
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setBackground(ALB);
        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        bottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnRegister = new JButton("Creeaza cont");
        btnRegister.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRegister.setForeground(GREEN_PRIMARY);
        btnRegister.setBackground(ALB);
        btnRegister.setBorder(BorderFactory.createEmptyBorder());
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> nav.showRoot("register"));

        JButton btnForgot = new JButton("Ai uitat parola?");
        btnForgot.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnForgot.setForeground(GRI_TEXT);
        btnForgot.setBackground(ALB);
        btnForgot.setBorder(BorderFactory.createEmptyBorder());
        btnForgot.setFocusPainted(false);
        btnForgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnForgot.addActionListener(e ->
            JOptionPane.showMessageDialog(page, "Resetare parola - in curand!",
                "TravaleRo", JOptionPane.INFORMATION_MESSAGE));

        bottomRow.add(btnRegister, BorderLayout.WEST);
        bottomRow.add(btnForgot,   BorderLayout.EAST);
        formCard.add(bottomRow);

        formCard.setPreferredSize(new Dimension(380, 420));
        rightSide.add(formCard);
        page.add(rightSide);
        return page;
    }
}
