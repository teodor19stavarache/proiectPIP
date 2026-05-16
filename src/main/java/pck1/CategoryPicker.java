package pck1;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Reusable category picker for travel preferences. Shows the 10 tag
 * categories from overpassAPI.py as clickable cards arranged in a grid;
 * the user selects exactly {@code requiredCount} of them.
 *
 * Used in two places:
 *   - RegisterPage : onboarding step, requires 3 picks
 *   - ProfilPage   : edit existing preferences
 *
 * Selection state is exposed via {@link #getSelectedTags()}. A live counter
 * label is rendered above the grid so the user always knows how many more
 * picks are needed. A callback can be registered with
 * {@link #setOnSelectionChanged(Runnable)} to toggle a submit button.
 */
public class CategoryPicker extends JPanel implements AppColors {

    /** Display metadata for each tag — must match the Python TAG_DICTIONARY keys. */
    private static final Map<String, CategoryMeta> CATEGORIES = new LinkedHashMap<>();
    static {
        CATEGORIES.put("istorie",     new CategoryMeta("🏛️", "Istorie",      "Castele, muzee, ruine",       new Color(150,  90,  40)));
        CATEGORIES.put("natura",      new CategoryMeta("🌲", "Natura",       "Parcuri, cascade, varfuri",    new Color( 40, 140,  80)));
        CATEGORIES.put("aventura",    new CategoryMeta("🎢", "Aventura",     "Parcuri tematice, climbing",  new Color(220, 110,  40)));
        CATEGORIES.put("cultura",     new CategoryMeta("🎭", "Cultura",      "Teatre, galerii, biblioteci", new Color(120,  60, 170)));
        CATEGORIES.put("religie",     new CategoryMeta("⛪", "Religie",      "Biserici, manastiri, capele", new Color(110, 100, 180)));
        CATEGORIES.put("arhitectura", new CategoryMeta("🏰", "Arhitectura",  "Monumente, turnuri, cladiri", new Color(160, 120,  50)));
        CATEGORIES.put("distractie",  new CategoryMeta("🎉", "Distractie",   "Cluburi, bowling, escape",    new Color(220,  60, 140)));
        CATEGORIES.put("sport",       new CategoryMeta("⚽", "Sport",         "Stadioane, piscine, tenis",   new Color( 50, 130, 200)));
        CATEGORIES.put("cumparaturi", new CategoryMeta("🛍️", "Cumparaturi", "Mall-uri, piete, magazine",   new Color(230, 140,  40)));
        CATEGORIES.put("relaxare",    new CategoryMeta("💆", "Relaxare",     "Spa, plaja, masaj",            new Color( 70, 180, 180)));
    }

    private final int requiredCount;
    private final List<String> selected = new ArrayList<>();
    private final Map<String, CardPanel> cards = new LinkedHashMap<>();
    private final JLabel counterLabel;
    private Runnable onSelectionChanged;

    public CategoryPicker(int requiredCount) {
        this(requiredCount, List.of());
    }

    public CategoryPicker(int requiredCount, List<String> preselected) {
        this.requiredCount = requiredCount;
        setLayout(new BorderLayout(0, 12));
        setBackground(ALB);

        // Counter label across the top
        counterLabel = new JLabel();
        counterLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        counterLabel.setForeground(GREEN_DARK);
        counterLabel.setBorder(new EmptyBorder(0, 4, 0, 0));
        add(counterLabel, BorderLayout.NORTH);

        // 2 rows × 5 cols grid of cards
        JPanel grid = new JPanel(new GridLayout(2, 5, 10, 10));
        grid.setBackground(ALB);
        for (Map.Entry<String, CategoryMeta> e : CATEGORIES.entrySet()) {
            CardPanel card = new CardPanel(e.getKey(), e.getValue());
            cards.put(e.getKey(), card);
            grid.add(card);
        }
        add(grid, BorderLayout.CENTER);

        // Pre-select tags if any (used by ProfilPage to show current prefs)
        for (String tag : preselected) {
            if (cards.containsKey(tag) && selected.size() < requiredCount) {
                cards.get(tag).setSelected(true);
                selected.add(tag);
            }
        }
        refreshCounter();
    }

    /** Returns the currently selected tag keys (e.g. "istorie", "natura"). */
    public List<String> getSelectedTags() {
        return new ArrayList<>(selected);
    }

    /** Returns true iff the user has selected exactly the required count. */
    public boolean isComplete() {
        return selected.size() == requiredCount;
    }

    /** Registers a callback invoked whenever the selection changes. */
    public void setOnSelectionChanged(Runnable callback) {
        this.onSelectionChanged = callback;
    }

    private void toggle(String tag) {
        CardPanel card = cards.get(tag);
        if (selected.contains(tag)) {
            selected.remove(tag);
            card.setSelected(false);
        } else if (selected.size() < requiredCount) {
            selected.add(tag);
            card.setSelected(true);
        } else {
            // Already at limit — briefly flash the counter to signal it
            Color orig = counterLabel.getForeground();
            counterLabel.setForeground(new Color(200, 60, 60));
            Timer flash = new Timer(300, ev -> counterLabel.setForeground(orig));
            flash.setRepeats(false);
            flash.start();
            return;
        }
        refreshCounter();
        if (onSelectionChanged != null) onSelectionChanged.run();
    }

    private void refreshCounter() {
        counterLabel.setText(
            String.format("Alege exact %d categorii  ·  %d / %d selectate",
                          requiredCount, selected.size(), requiredCount));
    }

    /** A single category card — clickable, toggles selected state. */
    private class CardPanel extends JPanel {
        private final String tag;
        private final CategoryMeta meta;
        private boolean isSelected = false;

        CardPanel(String tag, CategoryMeta meta) {
            this.tag  = tag;
            this.meta = meta;
            setLayout(new BorderLayout());
            setBackground(ALB);
            setBorder(new LineBorder(BORDER_C, 1, true));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(140, 110));

            JPanel inner = new JPanel();
            inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
            inner.setOpaque(false);
            inner.setBorder(new EmptyBorder(10, 12, 10, 12));

            JLabel icon = new JLabel(meta.icon);
            icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
            icon.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel name = new JLabel(meta.displayName);
            name.setFont(new Font("Segoe UI", Font.BOLD, 13));
            name.setForeground(new Color(20, 20, 20));
            name.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel desc = new JLabel("<html><div style='color:#777;font-size:9pt;'>"
                                   + meta.description + "</div></html>");
            desc.setAlignmentX(Component.LEFT_ALIGNMENT);

            inner.add(icon);
            inner.add(Box.createVerticalStrut(4));
            inner.add(name);
            inner.add(Box.createVerticalStrut(2));
            inner.add(desc);
            add(inner, BorderLayout.CENTER);

            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { toggle(tag); }
                @Override public void mouseEntered(MouseEvent e) {
                    if (!isSelected) setBorder(new LineBorder(meta.accent, 1, true));
                }
                @Override public void mouseExited(MouseEvent e) {
                    if (!isSelected) setBorder(new LineBorder(BORDER_C, 1, true));
                }
            });
        }

        void setSelected(boolean sel) {
            this.isSelected = sel;
            if (sel) {
                // Blend accent 15% + white 85% — Swing ignores alpha on opaque panels
                int r = (meta.accent.getRed()   + 255 * 5) / 6;
                int g = (meta.accent.getGreen() + 255 * 5) / 6;
                int b = (meta.accent.getBlue()  + 255 * 5) / 6;
                setBackground(new Color(r, g, b));
                setBorder(new LineBorder(meta.accent, 2, true));
            } else {
                setBackground(ALB);
                setBorder(new LineBorder(BORDER_C, 1, true));
            }
            repaint();
        }
    }

    /** Display metadata for one category. */
    private static class CategoryMeta {
        final String icon;
        final String displayName;
        final String description;
        final Color  accent;
        CategoryMeta(String icon, String displayName, String description, Color accent) {
            this.icon = icon;
            this.displayName = displayName;
            this.description = description;
            this.accent = accent;
        }
    }

    /** Returns the canonical list of category keys. */
    public static List<String> allTagKeys() {
        return new ArrayList<>(CATEGORIES.keySet());
    }
}
