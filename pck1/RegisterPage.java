package pck1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RegisterPage implements AppColors {

    public static JPanel build(AppNavigator nav) {
        JPanel page = new JPanel(new GridLayout(1, 2, 0, 0));

        // Stanga branding
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
            "<html><div style='text-align:center;color:#AAFFAA;font-size:13pt;'>" +
            "Alatura-te comunitatii TravaleRo<br>si descopera lumea." +
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
            new EmptyBorder(32, 44, 28, 44)
        ));

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
        formCard.add(Box.createVerticalStrut(24));

        // Rand Nume + Prenume pe acelasi rand
        JPanel numeRow = new JPanel(new GridLayout(1, 2, 10, 0));
        numeRow.setBackground(ALB);
        numeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        numeRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel numePanel = new JPanel();
        numePanel.setLayout(new BoxLayout(numePanel, BoxLayout.Y_AXIS));
        numePanel.setBackground(ALB);
        JLabel lblNume = new JLabel("Nume");
        lblNume.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNume.setForeground(GRI_TEXT);
        JTextField campNume = new JTextField();
        campNume.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campNume.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        numePanel.add(lblNume);
        numePanel.add(Box.createVerticalStrut(4));
        numePanel.add(campNume);
        numeRow.add(numePanel);

        JPanel prenumePanel = new JPanel();
        prenumePanel.setLayout(new BoxLayout(prenumePanel, BoxLayout.Y_AXIS));
        prenumePanel.setBackground(ALB);
        JLabel lblPrenume = new JLabel("Prenume");
        lblPrenume.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPrenume.setForeground(GRI_TEXT);
        JTextField campPrenume = new JTextField();
        campPrenume.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campPrenume.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        prenumePanel.add(lblPrenume);
        prenumePanel.add(Box.createVerticalStrut(4));
        prenumePanel.add(campPrenume);
        numeRow.add(prenumePanel);

        formCard.add(numeRow);
        formCard.add(Box.createVerticalStrut(14));
        formCard.add(buildFormField("Adresa de email", false));
        formCard.add(Box.createVerticalStrut(14));
        formCard.add(buildFormField("Parola", true));
        formCard.add(Box.createVerticalStrut(14));
        formCard.add(buildFormField("Oras resedinta", false));
        formCard.add(Box.createVerticalStrut(26));

        JButton btnCreaza = new JButton("Creeaza cont");
        btnCreaza.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCreaza.setBackground(GREEN_PRIMARY);
        btnCreaza.setForeground(ALB);
        btnCreaza.setBorder(new EmptyBorder(12, 0, 12, 0));
        btnCreaza.setFocusPainted(false);
        btnCreaza.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCreaza.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnCreaza.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCreaza.addActionListener(e ->
            JOptionPane.showMessageDialog(page,
                "Cont creat! Te poti autentifica acum.",
                "TravaleRo - Bun venit!", JOptionPane.INFORMATION_MESSAGE));
        formCard.add(btnCreaza);
        formCard.add(Box.createVerticalStrut(14));

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
        formCard.add(bottomRow);

        formCard.setPreferredSize(new Dimension(390, 510));
        rightSide.add(formCard);
        page.add(rightSide);
        return page;
    }

    // Helper: label + camp text sau parola
    private static JPanel buildFormField(String labelText, boolean esteParola) {
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
        camp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(9, 11, 9, 11)
        ));
        camp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        camp.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(camp);
        return panel;
    }
}
