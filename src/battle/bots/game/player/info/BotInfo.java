package battle.bots.game.player.info;

import battle.bots.game.objects.Bot;
import battle.bots.game.player.Coordinate;

public class BotInfo {
    private final Bot bot;
    private final Coordinate coordinate;

    public BotInfo(Bot bot, Coordinate coordinate) {
        this.bot = bot;
        this.coordinate = coordinate;
    }

    public int getBotHealth() {
        return this.bot.getHealth();
    }

    public int getBotGas() {
        return this.bot.getGas();
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }
}
