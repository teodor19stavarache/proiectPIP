package pck1;

import java.awt.EventQueue;

/*
 * Main - punct de intrare al aplicatiei TravaleRo.
 *
 * Singura responsabilitate a acestei clase este sa porneasca
 * fereastra principala (Gui_Aplicatie) pe Event Dispatch Thread,
 * asa cum cere Swing pentru orice cod care creeaza componente UI.
 *
 * Pentru a rula aplicatia: click dreapta pe Main.java -> Run As -> Java Application
 * (sau echivalentul din IDE-ul tau).
 */
public class Main {

    public static void main(String[] args) {
        // EventQueue.invokeLater garanteaza ca UI-ul se construieste
        // pe Event Dispatch Thread - regula obligatorie in Swing.
        EventQueue.invokeLater(() -> {
            try {
                Gui_Aplicatie frame = new Gui_Aplicatie();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}