package pck1;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        PythonDependencyManager.ensureDependencies();

        SwingUtilities.invokeLater(() -> {
            Gui_Aplicatie app = new Gui_Aplicatie();
            app.setVisible(true);
        });
    }
}
