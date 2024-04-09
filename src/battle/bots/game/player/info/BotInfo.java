package battle.bots.game.player.info;

import battle.bots.game.objects.Bot;
import battle.bots.game.player.Coordinate;

public class BotInfo extends Info {
    private final Bot bot;

    public BotInfo(Coordinate coordinate, Bot bot) {
        super(coordinate);
        this.bot = bot;
    }

    public int getBotHealth() {
        return this.bot.getHealth();
    }

    public int getBotGas() {
        return this.bot.getGas();
    }
}
