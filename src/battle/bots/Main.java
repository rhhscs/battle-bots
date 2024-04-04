package battle.bots;

import battle.bots.ui.BattleBotsApplication;

import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException |
                 IllegalAccessException ex) {
            ex.printStackTrace();
        }
        try {
            BattleBotsApplication battleBots = new BattleBotsApplication();
            battleBots.start();
        } catch (IOException e) {
            System.out.println("An error occurred while opening the application");
        }
    }
}
