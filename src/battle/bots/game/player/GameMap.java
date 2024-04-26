package battle.bots.game.player;


import battle.bots.game.actions.Move;
import battle.bots.game.actions.Move.Direction;
import battle.bots.game.objects.Bot;
import battle.bots.game.objects.GameObject;
import battle.bots.game.player.info.Info;
import battle.bots.game.util.ImmutablePoint;
import battle.bots.game.Const;

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
    //private final Move[][][][] pathfindCache;
    private final Map<ImmutablePoint, Map<ImmutablePoint, Move>> pathfindCache;
    private final Bot bot;
    // The position of the bot as grid coordinates
    private final ImmutablePoint botPosition;


    public GameMap(GameObject[][] map, Bot bot, ImmutablePoint botPosition) {
        this.map = map;
        this.pathfindCache = new HashMap<>();
//        this.pathfindCache = new Move[map.length][map[0].length][map.length][map[0].length];
        this.bot = bot;
        this.botPosition = botPosition;

        constructPathfind();
    }

    public ImmutablePoint getBotPosition() {
        return this.botPosition;
    }

    public Move pathfind(ImmutablePoint dest) {
        ImmutablePoint start = this.botPosition;
        if (this.map[start.getY()][start.getX()] == null || this.map[dest.getY()][dest.getX()] == null) {
            return null;
        }

        if (!this.pathfindCache.containsKey(start) || !this.pathfindCache.get(start).containsKey(dest)) {
            return null;
        }

        return this.pathfindCache.get(start).get(dest);
    }


    private void constructPathfind() {
        for (int y = 0; y < this.map.length; y++){
            for (int x = 0; x < this.map[y].length; x++){
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
        visited.add(tile);

        Direction[] changeInDirection = {Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH};

        while (!q.isEmpty()) {
            ImmutablePoint curr = q.remove();

            int x = curr.getX();
            int y = curr.getY();

            for(int i=0; i<Const.CHANGE_IN_X.length; i++) {

                int new_x = x + Const.CHANGE_IN_X[i];
                int new_y = y + Const.CHANGE_IN_Y[i];
                ImmutablePoint newPoint = new ImmutablePoint(new_x, new_y);

                if (inBounds(new_x, new_y) && this.map[y - 1][x] == null && !visited.contains(newPoint)) {
                    res.put(newPoint, new Move(changeInDirection[i]));
                    q.add(newPoint);
                    visited.add(newPoint);
                }

            }
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


