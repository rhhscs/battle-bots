package battle.bots;

import battle.bots.game.util.Angle;
import battle.bots.game.util.Vector;
import battle.bots.ui.BattleBotsApplication;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println(new Vector(100, new Angle(45, Angle.Unit.DEGREE)));
        try {
            BattleBotsApplication battleBots = new BattleBotsApplication();
            battleBots.start();
        } catch (IOException e) {
            System.out.println("An error occurred while opening the application");
        }
    }
}
