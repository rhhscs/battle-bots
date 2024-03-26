package battle.bots.game;


import battle.bots.game.actions.Move;
import battle.bots.game.objects.GameObject;

import java.awt.Point;
import java.util.List;

/**
 * An object which provides information about the map to the player.
 * @author Harry Xu
 * @version 1.0 - March 24th 2024
 */
public class Map {
    private final GameObject[][] map;
    private final Bot bot;
    private final Point botPosition;

    public Map(GameObject[][] map, Bot bot, Point botPosition) {
        this.map = map;
        this.bot = bot;
        this.botPosition = botPosition;
    }

    public Point getPosition() {
        return this.botPosition;
    }

    public Move pathfind(Point dest) {
        return this.pathfind(dest.x, dest.y);
    }

    public Move pathfind(int x, int y) {
        // TODO: implement pathfinding
        return null;
    }

    public List<GameObject> getObjects() {
        // TODO: implement
        return null;
    }
}
