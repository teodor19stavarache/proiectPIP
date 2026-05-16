package pck1;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Loading screen for the search flow.
 *
 * Two modes:
 *   - reset(onComplete)         : fixed-duration demo animation (~2.5s).
 *   - startIndeterminate()      : keeps animating while real work runs in
 *                                 the background. The progress bar grows
 *                                 asymptotically toward 95% so the user
 *                                 sees progress even though the true
 *                                 duration is unknown.
 *     markComplete(onComplete)  : called by the background worker when the
 *                                 scrapers finish; jumps to 100% and runs
 *                                 onComplete after a short flourish.
 */
public class LoadingScreen extends JPanel implements AppColors {

    private JProgressBar progressBar;
    private JLabel       loadingLabel;
    private Timer        progressTimer;
    private Timer        dotsTimer;
    private int          progress = 0;
    private int          dots     = 0;
    private Runnable     onComplete;
    private boolean      indeterminate = false;

    private static final String[] MESAJE = {
        "Se incarca destinatiile",
        "Se cauta atractii si restaurante",
        "Se filtreaza rezultatele",
        "Aproape gata"
    };

    public LoadingScreen() {
        setLayout(new GridBagLayout());
        setBackground(SIDEBAR_BG);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(SIDEBAR_BG);
        center.setBorder(new EmptyBorder(0, 60, 0, 60));

        JLabel logo = new JLabel(
            "<html><div style='text-align:center;'>" +
            "<span style='color:white;font-size:52pt;font-weight:bold;'>Travale</span>" +
            "<span style='color:#FFCC00;font-size:52pt;font-weight:bold;'>Ro</span>" +
            "</div></html>"
        );
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel avion = new JLabel("✈", SwingConstants.CENTER);
        avion.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        avion.setForeground(GREEN_PRIMARY);
        avion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel slogan = new JLabel(
            "<html><div style='text-align:center;color:#6aaa6a;font-size:12pt;'>" +
            "Planifica-ti urmatoarea aventura</div></html>"
        );
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel linie = new JPanel();
        linie.setBackground(GREEN_DARK);
        linie.setMaximumSize(new Dimension(300, 2));
        linie.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setForeground(GREEN_PRIMARY);
        progressBar.setBackground(new Color(20, 50, 20));
        progressBar.setBorder(BorderFactory.createLineBorder(new Color(30, 70, 30), 1));
        progressBar.setPreferredSize(new Dimension(360, 6));
        progressBar.setMaximumSize(new Dimension(360, 6));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        loadingLabel = new JLabel("Se incarca destinatiile...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loadingLabel.setForeground(new Color(100, 160, 100));
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel versiune = new JLabel("v1.0  ·  TravaleRo © 2025", SwingConstants.CENTER);
        versiune.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        versiune.setForeground(new Color(50, 90, 50));
        versiune.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(Box.createVerticalGlue());
        center.add(logo);
        center.add(Box.createVerticalStrut(4));
        center.add(avion);
        center.add(Box.createVerticalStrut(28));
        center.add(linie);
        center.add(Box.createVerticalStrut(28));
        center.add(progressBar);
        center.add(Box.createVerticalStrut(14));
        center.add(loadingLabel);
        center.add(Box.createVerticalStrut(20));
        center.add(versiune);
        center.add(Box.createVerticalGlue());

        add(center);

        // Fixed-duration timer (used only by reset()).
        progressTimer = new Timer(25, (ActionEvent e) -> {
            progress += 1;
            progressBar.setValue(progress);

            if (progress == 25) loadingLabel.setText(MESAJE[1] + "...");
            if (progress == 55) loadingLabel.setText(MESAJE[2] + "...");
            if (progress == 80) loadingLabel.setText(MESAJE[3] + "...");

            if (progress >= 100) {
                progressTimer.stop();
                dotsTimer.stop();
                loadingLabel.setText("Gata! ✓");
                loadingLabel.setForeground(GREEN_PRIMARY);
                Timer finalTimer = new Timer(400, ev -> {
                    if (onComplete != null) onComplete.run();
                });
                finalTimer.setRepeats(false);
                finalTimer.start();
            }
        });

        dotsTimer = new Timer(400, (ActionEvent e) -> {
            if (progress >= 100) return;
            dots = (dots + 1) % 4;
            loadingLabel.setText(getCurrentMesaj() + ".".repeat(dots));
        });
    }

    /** Demo / fixed-duration animation (~2.5s). Kept for backwards compatibility. */
    public void reset(Runnable onComplete) {
        this.onComplete    = onComplete;
        this.indeterminate = false;
        resetState();
        progressTimer.start();
        dotsTimer.start();
    }

    /**
     * Starts an indeterminate animation. The progress bar advances slowly
     * and asymptotically toward 95% so the user perceives motion regardless
     * of how long the real work takes. Call {@link #markComplete(Runnable)}
     * from the background worker to finish.
     */
    public void startIndeterminate() {
        this.indeterminate = true;
        resetState();

        // Replace the fixed timer with one that decelerates as it approaches 95.
        progressTimer.stop();
        progressTimer = new Timer(120, (ActionEvent e) -> {
            // Asymptotic growth: each tick takes ~5% of the remaining gap.
            int remaining = 95 - progress;
            int step      = Math.max(1, remaining / 20);
            progress      = Math.min(95, progress + step);
            progressBar.setValue(progress);

            if (progress >= 25 && progress < 55) loadingLabel.setText(MESAJE[1] + "...");
            if (progress >= 55 && progress < 80) loadingLabel.setText(MESAJE[2] + "...");
            if (progress >= 80)                  loadingLabel.setText(MESAJE[3] + "...");
        });
        progressTimer.start();
        dotsTimer.start();
    }

    /**
     * Snaps the progress bar to 100%, shows a brief success state, then
     * fires onComplete on the EDT.
     */
    public void markComplete(Runnable onComplete) {
        this.onComplete = onComplete;
        if (progressTimer != null) progressTimer.stop();
        dotsTimer.stop();
        progress = 100;
        progressBar.setValue(100);
        loadingLabel.setText("Gata! ✓");
        loadingLabel.setForeground(GREEN_PRIMARY);
        Timer finalTimer = new Timer(400, ev -> {
            if (this.onComplete != null) this.onComplete.run();
        });
        finalTimer.setRepeats(false);
        finalTimer.start();
    }

    /**
     * Aborts the loading animation and displays an error message.
     * Useful when the background scraper fails.
     */
    public void markFailed(String message, Runnable onComplete) {
        this.onComplete = onComplete;
        if (progressTimer != null) progressTimer.stop();
        dotsTimer.stop();
        loadingLabel.setText(message);
        loadingLabel.setForeground(new Color(220, 90, 90));
        Timer finalTimer = new Timer(1200, ev -> {
            if (this.onComplete != null) this.onComplete.run();
        });
        finalTimer.setRepeats(false);
        finalTimer.start();
    }

    private void resetState() {
        progress = 0;
        dots     = 0;
        progressBar.setValue(0);
        loadingLabel.setText(MESAJE[0] + "...");
        loadingLabel.setForeground(new Color(100, 160, 100));
        if (progressTimer != null && progressTimer.isRunning()) progressTimer.stop();
        if (dotsTimer.isRunning())                              dotsTimer.stop();
    }

    private String getCurrentMesaj() {
        if (progress < 25) return MESAJE[0];
        if (progress < 55) return MESAJE[1];
        if (progress < 80) return MESAJE[2];
        return MESAJE[3];
    }
}
