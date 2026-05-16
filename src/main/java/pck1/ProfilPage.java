package pck1;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import pck1.Proiect_PIP.DataBase_Utilizator;
import pck1.Proiect_PIP.Utilizator;

/**
 * User profile page.
 *
 * Shows the data of the currently logged-in user (from {@link Session}),
 * and lets the user re-pick their travel preference categories via the
 * {@link CategoryPicker}. Saving calls
 * {@link DataBase_Utilizator#update_tags(Utilizator, List)}.
 */
public class ProfilPage implements AppColors {

    private static final int REQUIRED_CATEGORIES = 3;

    public static JPanel build() {
        Utilizator user = Session.getUser();

        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BG_PAGE);

        // ----- Header verde -----
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(GREEN_PRIMARY);
        header.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        headerLeft.setBackground(GREEN_PRIMARY);

        JPanel avatarBox = new JPanel(new GridBagLayout());
        avatarBox.setBackground(GREEN_DARK);
        avatarBox.setPreferredSize(new Dimension(70, 70));
        avatarBox.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 2));
        JLabel initiale = new JLabel(initialsOf(user));
        initiale.setFont(new Font("Segoe UI", Font.BOLD, 26));
        initiale.setForeground(ALB);
        avatarBox.add(initiale);
        headerLeft.add(avatarBox);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setBackground(GREEN_PRIMARY);

        JLabel lblNume = new JLabel(user != null ? user.getNume() : "Vizitator");
        lblNume.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblNume.setForeground(ALB);

        JLabel lblEmail = new JLabel(user != null ? user.getEmail() : "—");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEmail.setForeground(new Color(200, 255, 200));

        JLabel lblTelefon = new JLabel(
            user != null && user.getNumarTelefon() != null && !user.getNumarTelefon().isEmpty()
                ? "Tel: " + user.getNumarTelefon() : "");
        lblTelefon.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTelefon.setForeground(new Color(170, 240, 170));

        namePanel.add(lblNume);
        namePanel.add(Box.createVerticalStrut(3));
        namePanel.add(lblEmail);
        namePanel.add(Box.createVerticalStrut(4));
        namePanel.add(lblTelefon);
        headerLeft.add(namePanel);

        header.add(headerLeft, BorderLayout.CENTER);
        page.add(header, BorderLayout.NORTH);

        // ----- Continut scrollabil -----
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(18, 22, 22, 22));

        // Statistici (placeholder)
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 12, 0));
        statsRow.setBackground(BG_PAGE);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsRow.add(createStatCard("0",  "Rezervari active", GREEN_PRIMARY));
        statsRow.add(createStatCard("0",  "Calatorii totale", GREEN_DARK));
        statsRow.add(createStatCard("—",  "Rating mediu",     ORANGE_PRICE));
        statsRow.add(createStatCard("0",  "Orase vizitate",   ORANGE_STAR));
        content.add(statsRow);
        content.add(Box.createVerticalStrut(18));

        // Informatii cont (din DB)
        content.add(buildSection("Informatii cont", new String[][]{
            {"Nume complet", user != null ? user.getNume()         : "—"},
            {"Email",        user != null ? user.getEmail()        : "—"},
            {"Telefon",      user != null ? user.getNumarTelefon() : "—"},
        }));
        content.add(Box.createVerticalStrut(14));

        // Preferinte calatorie cu editor
        content.add(buildPreferencesSection(user));
        content.add(Box.createVerticalStrut(22));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_PAGE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    // =====================================================================
    // Preferinte de calatorie (editor cu CategoryPicker)
    // =====================================================================
    private static JPanel buildPreferencesSection(Utilizator user) {
        JPanel sect = new JPanel();
        sect.setLayout(new BoxLayout(sect, BoxLayout.Y_AXIS));
        sect.setBackground(ALB);
        sect.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(14, 16, 14, 16)
        ));
        sect.setAlignmentX(Component.LEFT_ALIGNMENT);
        sect.setMaximumSize(new Dimension(Integer.MAX_VALUE, 460));

        // Header rand: titlu stanga, buton "Editeaza" dreapta
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(ALB);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titlu = new JLabel("Preferinte de calatorie");
        titlu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titlu.setForeground(new Color(15, 15, 15));
        row.add(titlu, BorderLayout.WEST);

        JButton btnEdit = new JButton("Editeaza");
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEdit.setForeground(GREEN_PRIMARY);
        btnEdit.setBackground(ALB);
        btnEdit.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GREEN_PRIMARY, 1),
            new EmptyBorder(6, 14, 6, 14)
        ));
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        row.add(btnEdit, BorderLayout.EAST);

        sect.add(row);
        sect.add(Box.createVerticalStrut(10));

        // Display mode: chip-uri ne-editabile cu preferintele curente
        JPanel chipsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        chipsRow.setBackground(ALB);
        chipsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        chipsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        List<String> current = user != null ? user.getTags() : List.of();
        if (current == null || current.isEmpty()) {
            JLabel none = new JLabel("Nicio preferinta setata.");
            none.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            none.setForeground(GRI_TEXT);
            chipsRow.add(none);
        } else {
            for (String t : current) chipsRow.add(makeChip(t));
        }
        sect.add(chipsRow);

        // Edit mode: CategoryPicker pre-completat cu selectia curenta
        CategoryPicker picker = new CategoryPicker(REQUIRED_CATEGORIES,
            current != null ? current : List.of());
        picker.setVisible(false);
        picker.setAlignmentX(Component.LEFT_ALIGNMENT);
        picker.setBorder(new EmptyBorder(8, 0, 0, 0));
        sect.add(picker);

        JPanel saveRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
        saveRow.setBackground(ALB);
        saveRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton btnCancel = new JButton("Renunta");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCancel.setBackground(ALB);
        btnCancel.setForeground(GRI_TEXT);
        btnCancel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(6, 14, 6, 14)));
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton btnSave = new JButton("Salveaza preferintele");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.setBackground(GREEN_PRIMARY);
        btnSave.setForeground(ALB);
        btnSave.setBorder(new EmptyBorder(7, 16, 7, 16));
        btnSave.setFocusPainted(false);
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSave.setEnabled(picker.isComplete());

        picker.setOnSelectionChanged(() -> btnSave.setEnabled(picker.isComplete()));

        saveRow.add(btnCancel);
        saveRow.add(btnSave);
        saveRow.setVisible(false);
        sect.add(saveRow);

        // Toggle edit mode
        btnEdit.addActionListener(ev -> {
            boolean enterEdit = !picker.isVisible();
            picker.setVisible(enterEdit);
            saveRow.setVisible(enterEdit);
            chipsRow.setVisible(!enterEdit);
            btnEdit.setText(enterEdit ? "Anuleaza" : "Editeaza");
            sect.revalidate();
            sect.repaint();
        });
        btnCancel.addActionListener(ev -> {
            picker.setVisible(false);
            saveRow.setVisible(false);
            chipsRow.setVisible(true);
            btnEdit.setText("Editeaza");
            sect.revalidate();
            sect.repaint();
        });

        btnSave.addActionListener(ev -> {
            if (user == null) {
                JOptionPane.showMessageDialog(sect,
                    "Nu esti autentificat - preferintele nu pot fi salvate.",
                    "Eroare", JOptionPane.WARNING_MESSAGE);
                return;
            }
            List<String> newTags = picker.getSelectedTags();
            try {
                DataBase_Utilizator.update_tags(user, newTags);
                // Update in-memory user so the sidebar picks it up on next search
                Utilizator refreshed = new Utilizator(
                    user.getEmail(), user.getNume(), user.getParola(),
                    user.getNumarTelefon(), newTags);
                Session.login(refreshed);

                chipsRow.removeAll();
                for (String t : newTags) chipsRow.add(makeChip(t));
                chipsRow.revalidate();
                chipsRow.repaint();

                picker.setVisible(false);
                saveRow.setVisible(false);
                chipsRow.setVisible(true);
                btnEdit.setText("Editeaza");

                JOptionPane.showMessageDialog(sect,
                    "Preferintele au fost actualizate.",
                    "TravaleRo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(sect,
                    "Eroare la salvare: " + ex.getMessage(),
                    "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        });

        return sect;
    }

    private static JLabel makeChip(String tag) {
        JLabel chip = new JLabel(" " + capitalize(tag) + " ");
        chip.setFont(new Font("Segoe UI", Font.BOLD, 11));
        chip.setForeground(ALB);
        chip.setBackground(GREEN_PRIMARY);
        chip.setOpaque(true);
        chip.setBorder(new EmptyBorder(3, 8, 3, 8));
        return chip;
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static String initialsOf(Utilizator user) {
        if (user == null || user.getNume() == null || user.getNume().isEmpty()) return "??";
        String[] parts = user.getNume().trim().split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, 1).toUpperCase();
        return ("" + parts[0].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase();
    }

    private static JPanel buildSection(String titluSectiune, String[][] randuri) {
        JPanel sect = new JPanel(new BorderLayout());
        sect.setBackground(ALB);
        sect.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(14, 16, 6, 16)
        ));
        sect.setAlignmentX(Component.LEFT_ALIGNMENT);
        sect.setMaximumSize(new Dimension(Integer.MAX_VALUE, 999));

        JLabel titlu = new JLabel(titluSectiune);
        titlu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titlu.setForeground(new Color(15, 15, 15));
        titlu.setBorder(new EmptyBorder(0, 0, 8, 0));
        sect.add(titlu, BorderLayout.NORTH);

        JPanel rows = new JPanel();
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setBackground(ALB);

        for (String[] r : randuri) {
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(ALB);
            row.setBorder(new MatteBorder(0, 0, 1, 0, new Color(245, 245, 245)));

            JLabel lblKey = new JLabel(r[0]);
            lblKey.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblKey.setForeground(GRI_TEXT);
            lblKey.setBorder(new EmptyBorder(9, 0, 9, 0));
            lblKey.setPreferredSize(new Dimension(180, 36));

            JLabel lblVal = new JLabel(r[1] != null ? r[1] : "—");
            lblVal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblVal.setForeground(new Color(15, 15, 15));

            row.add(lblKey, BorderLayout.WEST);
            row.add(lblVal, BorderLayout.CENTER);
            rows.add(row);
        }

        sect.add(rows, BorderLayout.CENTER);
        return sect;
    }

    private static JPanel createStatCard(String valoare, String eticheta, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        JPanel accentLine = new JPanel();
        accentLine.setBackground(accentColor);
        accentLine.setPreferredSize(new Dimension(0, 4));
        card.add(accentLine, BorderLayout.NORTH);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(ALB);
        inner.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel lblV = new JLabel(valoare);
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblV.setForeground(accentColor);
        lblV.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblE = new JLabel(eticheta);
        lblE.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblE.setForeground(GRI_TEXT);
        lblE.setAlignmentX(Component.LEFT_ALIGNMENT);

        inner.add(lblV);
        inner.add(Box.createVerticalStrut(2));
        inner.add(lblE);
        card.add(inner, BorderLayout.CENTER);
        return card;
    }
}
