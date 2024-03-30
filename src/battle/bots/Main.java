package battle.bots;

import battle.bots.ui.BattleBotsApplication;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            BattleBotsApplication battleBots = new BattleBotsApplication();
            battleBots.start();
        } catch (IOException e) {
            System.out.println("An error occurred while opening the application");
        }
    }
}
