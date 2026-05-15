package pck1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
//import javax.swing.border.MatteBorder;

public class Sidebar extends JPanel implements AppColors {

    private JTextField locatieField;
    private JTextField dataInceput;
    private JTextField dataFinal;
    private JSpinner   adulti;
    private JSpinner   copii;
    private JPanel     varstePanel;

    public Sidebar(AppNavigator nav) {
        setLayout(new BorderLayout());
        setBackground(SIDEBAR_BG);
        setPreferredSize(new Dimension(222, 0));

        // Sectiunea de cautare
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

        searchSection.add(buildFieldLabel("▾  Destinatie / Locatie"));
        locatieField = buildTextField("ex: Paris, Iasi...");
        searchSection.add(locatieField);
        searchSection.add(Box.createVerticalStrut(8));

        searchSection.add(buildFieldLabel("▾  Data inceput"));
        dataInceput = buildTextField("zz/ll/aaaa");
        searchSection.add(dataInceput);
        searchSection.add(Box.createVerticalStrut(8));

        searchSection.add(buildFieldLabel("▾  Data final"));
        dataFinal = buildTextField("zz/ll/aaaa");
        searchSection.add(dataFinal);
        searchSection.add(Box.createVerticalStrut(8));

        // Adulti
        searchSection.add(buildFieldLabel("▾  Adulti"));
        JPanel adultiRow = buildSpinnerRow();
        JLabel adultiIcon = new JLabel("👤");
        adultiIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        adultiRow.add(adultiIcon, BorderLayout.WEST);
        adulti = new JSpinner(new SpinnerNumberModel(2, 1, 20, 1));
        styleSpinner(adulti);
        adultiRow.add(adulti, BorderLayout.CENTER);
        searchSection.add(adultiRow);
        searchSection.add(Box.createVerticalStrut(8));

        // Copii
        searchSection.add(buildFieldLabel("▾  Copii (sub 18 ani)"));
        JPanel copiiRow = buildSpinnerRow();
        JLabel copiiIcon = new JLabel("👦");
        copiiIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        copiiRow.add(copiiIcon, BorderLayout.WEST);
        copii = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        styleSpinner(copii);
        copiiRow.add(copii, BorderLayout.CENTER);
        searchSection.add(copiiRow);
        searchSection.add(Box.createVerticalStrut(8));

        // Varste copii (panel dinamic)
        searchSection.add(buildFieldLabel("▾  Varste copii"));
        varstePanel = new JPanel();
        varstePanel.setLayout(new BoxLayout(varstePanel, BoxLayout.Y_AXIS));
        varstePanel.setBackground(SIDEBAR_BG);
        varstePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchSection.add(varstePanel);
        searchSection.add(Box.createVerticalStrut(4));

        copii.addChangeListener(e -> updateVarsteCopiiForms());
        updateVarsteCopiiForms();

        searchSection.add(Box.createVerticalStrut(12));

        // Buton Cauta
        JButton btnCauta = new JButton("  Cauta sejur  >");
        btnCauta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCauta.setBackground(GREEN_PRIMARY);
        btnCauta.setForeground(ALB);
        btnCauta.setBorder(new EmptyBorder(10, 0, 10, 0));
        btnCauta.setFocusPainted(false);
        btnCauta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCauta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnCauta.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCauta.setOpaque(true);
        btnCauta.addActionListener(e -> {
            nav.clearSidebarSelection();
            nav.showPage("search");
        });
        searchSection.add(btnCauta);

        add(searchSection, BorderLayout.CENTER);

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

    private void updateVarsteCopiiForms() {
        varstePanel.removeAll();
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
                    new EmptyBorder(3, 8, 3, 8)
                ));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                row.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel lblCopil = new JLabel("Copil " + i + ":");
                lblCopil.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblCopil.setForeground(new Color(140, 190, 140));
                row.add(lblCopil, BorderLayout.WEST);

                JSpinner spVarsta = new JSpinner(new SpinnerNumberModel(5, 0, 17, 1));
                styleSpinner(spVarsta);
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
            new EmptyBorder(6, 8, 6, 8)
        ));
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
            new EmptyBorder(4, 8, 4, 8)
        ));
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
            ? new Color(80, 220, 100)
            : new Color(255, 180, 40);
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
