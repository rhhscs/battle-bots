package battle.bots.game;


import battle.bots.game.actions.Move;
import battle.bots.game.actions.Move.Direction;
import battle.bots.game.objects.Bot;
import battle.bots.game.objects.GameObject;

import java.awt.Point;
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
    private final Map<Point, Map<Point, Move>> pathfindCache = new HashMap<>();
    private final Bot bot;
    private final Point botPosition;


    public GameMap(GameObject[][] map, Bot bot, Point botPosition) {
        this.map = map;
        this.bot = bot;
        this.botPosition = botPosition;
    }

    public Point getPosition() {
        return this.botPosition;
    }

    public Move pathfind(Point start, Point dest) {
        if (map[start.y][start.x] == null || map[dest.y][dest.x] == null)
            return null;
        if (!pathfindCache.containsKey(start) || !pathfindCache.get(start).containsKey(dest))
            return null;
        return pathfindCache.get(start).get(dest);
    }


    private void constructPathfind() {
        for (int y = 0; y <= map.length; y++){
            for (int x = 0; x <= map[y].length; x++){
                if (map[y][x] != null) {
                    constructPathfindSingle(new Point(x, y));
                }
            }
        }
    }
    private void constructPathfindSingle(Point tile) {
        Map<Point, Move> res = new HashMap<>();
        Queue<Point> q = new LinkedList<>();
        HashSet<Point> visited = new HashSet<>();
        q.add(tile);
        while (!q.isEmpty()) {
            Point curr = q.remove();
            if (inBounds(curr.y - 1, curr.x) && map[curr.y - 1][curr.x] != null) {
                Point temp = new Point(curr.x, curr.y - 1);
                res.put(temp, new Move(Direction.NORTH));
                q.add(temp);
            }
            if (inBounds(curr.y + 1, curr.x) && map[curr.y + 1][curr.x] != null) {
                Point temp = new Point(curr.x, curr.y + 1);
                res.put(temp, new Move(Direction.SOUTH));
                q.add(temp);
            }
            if (inBounds(curr.y, curr.x - 1) && map[curr.y][curr.x - 1] != null) {
                Point temp = new Point(curr.x - 1, curr.y);
                res.put(temp, new Move(Direction.WEST));
                q.add(temp);
            }
            if (inBounds(curr.y, curr.x + 1) && map[curr.y][curr.x + 1] != null) {
                Point temp = new Point(curr.x + 1, curr.y);
                res.put(temp, new Move(Direction.EAST));
                q.add(temp);
            }

            visited.add(curr);
        }
        pathfindCache.put(tile, res);
    }
    
    private boolean inBounds(int x, int y){
        return false;
    }

    public List<GameObject> getObjects() {
        // TODO: implement
        return null;
    }
}


