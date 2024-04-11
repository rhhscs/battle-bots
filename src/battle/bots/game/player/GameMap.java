package battle.bots.game.player;


import battle.bots.game.actions.Move;
import battle.bots.game.actions.Move.Direction;
import battle.bots.game.objects.Bot;
import battle.bots.game.objects.GameObject;
import battle.bots.game.player.info.Info;
import battle.bots.game.util.ImmutablePoint;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * An object which provides information about the map to the player.
 * @author Harry Xu
 * @version 1.0 - March 24th 2024
 */
public class GameMap {
    private final GameObject[][] map;
    private final Map<ImmutablePoint, Map<ImmutablePoint, Move>> pathfindCache;
    private final Bot bot;
    private final ImmutablePoint botPosition;


    public GameMap(GameObject[][] map, Bot bot, ImmutablePoint botPosition) {
        this.map = map;
        this.pathfindCache = new HashMap<>();
        this.bot = bot;
        this.botPosition = botPosition;
    }

    public ImmutablePoint getPosition() {
        return this.botPosition;
    }

    public Move pathfind(ImmutablePoint start, ImmutablePoint dest) {
        if (this.map[start.getY()][start.getX()] == null || this.map[dest.getY()][dest.getX()] == null) {
            return null;
        }

        if (!this.pathfindCache.containsKey(start) || !this.pathfindCache.get(start).containsKey(dest)) {
            return null;
        }

        return this.pathfindCache.get(start).get(dest);
    }


    private void constructPathfind() {
        for (int y = 0; y <= this.map.length; y++){
            for (int x = 0; x <= this.map[y].length; x++){
                if (this.map[y][x] != null) {
                    constructPathfindSingle(new ImmutablePoint(x, y));
                }
            }
        }
    }
    private void constructPathfindSingle(ImmutablePoint tile) {
        Map<ImmutablePoint, Move> res = new HashMap<>();
        Queue<ImmutablePoint> q = new LinkedList<>();
        HashSet<ImmutablePoint> visited = new HashSet<>();
        q.add(tile);

        while (!q.isEmpty()) {
            ImmutablePoint curr = q.remove();

            int x = curr.getX();
            int y = curr.getY();

            if (inBounds(x, y - 1) && this.map[y - 1][x] != null) {
                ImmutablePoint temp = new ImmutablePoint(x, y);
                res.put(temp, new Move(Direction.NORTH));
                q.add(temp);
            }

            if (inBounds(x, y + 1) && this.map[y + 1][x] != null) {
                ImmutablePoint temp = new ImmutablePoint(x, y + 1);
                res.put(temp, new Move(Direction.SOUTH));
                q.add(temp);
            }

            if (inBounds(x - 1, y) && this.map[y][x - 1] != null) {
                ImmutablePoint temp = new ImmutablePoint(x - 1, y);
                res.put(temp, new Move(Direction.WEST));
                q.add(temp);
            }

            if (inBounds(x + 1, y) && this.map[y][x + 1] != null) {
                ImmutablePoint temp = new ImmutablePoint(x + 1, y);
                res.put(temp, new Move(Direction.EAST));
                q.add(temp);
            }

            visited.add(curr);
        }

        this.pathfindCache.put(tile, res);
    }
    
    private boolean inBounds(int x, int y){
        return false;
    }

    public List<Info> getObjects() {
        // TODO: implement
        return null;
    }
}


