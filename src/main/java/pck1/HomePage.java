package pck1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class HomePage implements AppColors {

    private static final Object[][] SLIDES = {
        {"🏰", "Castelul Bran", "Brașov", "Castelul lui Dracula — unul dintre cele mai vizitate monumente din Romania.",        new Color(120,  50,  30)},
        {"🏛️", "Palatul Parlamentului", "București", "A doua cea mai mare cladire administrativa din lume, simbol al capitalei.", new Color( 60,  80, 150)},
        {"🦆", "Delta Dunării", "Tulcea", "Rezervatie a biosferei UNESCO — 5.800 km² de canale, lacuri si paduri virgine.",   new Color( 30, 130, 100)},
        {"⛪", "Mănăstirea Voroneț", "Suceava", "Capela Sixtina a Estului — frescele albastre unice din secolul al XV-lea.",   new Color( 60,  80, 170)},
        {"⛰️", "Cheile Bicazului", "Neamț",  "Canion spectaculos cu pereti stancos de pana la 300m — paradis al alpinistilor.",new Color( 80, 100,  50)},
        {"🧂", "Salina Turda", "Cluj",   "Mina de sare transformata in parc de agrement la 120m adancime sub pamant.",        new Color( 90,  50, 120)},
    };

    public static JPanel build(AppNavigator nav) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BG_PAGE);

        // ── Hero banner ──────────────────────────────────────────────────────
        JPanel hero = buildHero();
        page.add(hero, BorderLayout.NORTH);

        // ── Continut principal ────────────────────────────────────────────────
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(20, 28, 28, 28));

        // Slideshow
        content.add(buildSlideshowSection());
        content.add(Box.createVerticalStrut(28));

        // Cum functioneaza
        content.add(buildHowItWorks(nav));
        content.add(Box.createVerticalStrut(20));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_PAGE);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    // ── Hero ─────────────────────────────────────────────────────────────────
    private static JPanel buildHero() {
        JPanel hero = new JPanel(new BorderLayout());
        hero.setBackground(GREEN_DARK);
        hero.setBorder(new EmptyBorder(22, 28, 22, 28));

        JLabel title = new JLabel("Bun venit pe TravaleRo ✈");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(ALB);

        JLabel sub = new JLabel("Descopera cele mai frumoase destinatii din Romania");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(new Color(180, 230, 180));
        sub.setBorder(new EmptyBorder(6, 0, 0, 0));

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setOpaque(false);
        texts.add(title);
        texts.add(sub);
        hero.add(texts, BorderLayout.CENTER);
        return hero;
    }

    // ── Slideshow ─────────────────────────────────────────────────────────────
    private static JPanel buildSlideshowSection() {
        JPanel section = new JPanel(new BorderLayout(0, 10));
        section.setOpaque(false);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));

        JLabel sectionTitle = new JLabel("Destinatii populare din Romania");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(GREEN_DARK);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 6, 0));
        section.add(sectionTitle, BorderLayout.NORTH);

        // Slide panel (CardLayout)
        CardLayout cl = new CardLayout();
        JPanel slides = new JPanel(cl);
        slides.setPreferredSize(new Dimension(0, 240));

        for (int i = 0; i < SLIDES.length; i++) {
            slides.add(buildSlide(SLIDES[i]), String.valueOf(i));
        }

        // Dot indicators
        JPanel dots = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        dots.setOpaque(false);
        JLabel[] dotLabels = new JLabel[SLIDES.length];
        for (int i = 0; i < SLIDES.length; i++) {
            JLabel dot = new JLabel("●");
            dot.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            dot.setForeground(i == 0 ? GREEN_PRIMARY : new Color(200, 200, 200));
            dotLabels[i] = dot;
            dots.add(dot);
        }

        JPanel slideWrap = new JPanel(new BorderLayout(0, 4));
        slideWrap.setOpaque(false);
        slideWrap.add(slides, BorderLayout.CENTER);
        slideWrap.add(dots,   BorderLayout.SOUTH);
        section.add(slideWrap, BorderLayout.CENTER);

        // Auto-advance timer every 3.5 seconds
        int[] idx = {0};
        Timer timer = new Timer(3500, e -> {
            dotLabels[idx[0]].setForeground(new Color(200, 200, 200));
            idx[0] = (idx[0] + 1) % SLIDES.length;
            dotLabels[idx[0]].setForeground(GREEN_PRIMARY);
            cl.show(slides, String.valueOf(idx[0]));
        });
        timer.start();

        return section;
    }

    private static JPanel buildSlide(Object[] data) {
        String emoji  = (String) data[0];
        String name   = (String) data[1];
        String city   = (String) data[2];
        String desc   = (String) data[3];
        Color  accent = (Color)  data[4];

        JPanel slide = new JPanel(new BorderLayout());
        slide.setBackground(accent);
        slide.setBorder(BorderFactory.createLineBorder(accent.darker(), 1));

        // Emoji zona stanga
        JLabel emojiLbl = new JLabel(emoji, SwingConstants.CENTER);
        emojiLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
        emojiLbl.setPreferredSize(new Dimension(180, 0));
        emojiLbl.setOpaque(true);
        emojiLbl.setBackground(accent.darker());
        slide.add(emojiLbl, BorderLayout.WEST);

        // Info zona dreapta
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameLbl.setForeground(ALB);
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel cityLbl = new JLabel("📍 " + city);
        cityLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cityLbl.setForeground(new Color(220, 240, 220));
        cityLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        cityLbl.setBorder(new EmptyBorder(4, 0, 12, 0));

        JLabel descLbl = new JLabel("<html><div style='width:320px;color:#e8f8e8;font-size:12pt;'>"
                                   + desc + "</div></html>");
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(nameLbl);
        info.add(cityLbl);
        info.add(descLbl);
        slide.add(info, BorderLayout.CENTER);

        return slide;
    }

    // ── Cum functioneaza ──────────────────────────────────────────────────────
    private static JPanel buildHowItWorks(AppNavigator nav) {
        JPanel section = new JPanel(new BorderLayout(0, 14));
        section.setOpaque(false);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Cum functioneaza TravaleRo?");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(GREEN_DARK);
        section.add(title, BorderLayout.NORTH);

        JPanel steps = new JPanel(new GridLayout(1, 3, 16, 0));
        steps.setOpaque(false);

        steps.add(buildStep("1", "🔍", "Cauta destinatia",
            "Introdu orasul dorit in bara din stanga si apasa 'Cauta sejur'. " +
            "TravaleRo va gasi in timp real hoteluri, atractii si restaurante."));

        steps.add(buildStep("2", "📋", "Exploreaza rezultatele",
            "Vezi hoteluri filtrate dupa preferintele tale de calatorie, " +
            "atractii grupate pe categorii si restaurante recomandate."));

        steps.add(buildStep("3", "🏨", "Rezerva si planifica",
            "Salveaza preferintele, vizualizeaza detalii complete si rezerva " +
            "cazarea aleasa direct din aplicatie."));

        section.add(steps, BorderLayout.CENTER);

        // Hint sidebar
        JPanel hint = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        hint.setOpaque(false);
        hint.setBorder(new MatteBorder(1, 0, 0, 0, new Color(210, 210, 210)));
        hint.add(Box.createVerticalStrut(12));
        JLabel hintLbl = new JLabel(
            "💡  Foloseste bara din stanga pentru a introduce destinatia, datele de calatorie si numarul de persoane.");
        hintLbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hintLbl.setForeground(GRI_TEXT);
        hint.add(hintLbl);
        section.add(hint, BorderLayout.SOUTH);

        return section;
    }

    private static JPanel buildStep(String nr, String icon, String stepTitle, String desc) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(18, 18, 18, 18)));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);

        JLabel nrLbl = new JLabel(nr);
        nrLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nrLbl.setForeground(ALB);
        nrLbl.setBackground(GREEN_PRIMARY);
        nrLbl.setOpaque(true);
        nrLbl.setBorder(new EmptyBorder(2, 8, 2, 8));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        top.add(nrLbl);
        top.add(iconLbl);
        card.add(top, BorderLayout.NORTH);

        JLabel titleLbl = new JLabel(stepTitle);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLbl.setForeground(new Color(30, 30, 30));
        titleLbl.setBorder(new EmptyBorder(2, 0, 6, 0));
        card.add(titleLbl, BorderLayout.CENTER);

        JLabel descLbl = new JLabel("<html><div style='width:180px;color:#555;font-size:11pt;'>" + desc + "</div></html>");
        card.add(descLbl, BorderLayout.SOUTH);

        return card;
    }
}
