//import java.util.ArrayList;
//import java.util.Stack;
//
//public class Pathfinder {
//    private GameMap map;
//
//    private final int [] directionX = { 0,  0, 1, -1};
//    private final int [] directionY = { 1, -1, 0,  0};
//
//    Pathfinder(GameMap map){
//        this.map = map;
//    }
//
//    // finds the path from the start to the end, returns path in a stack
//    // recursively searches each layer of distance from the start until it reaches the end,
//    // then backtracks and traces the path
//    public Stack<Tile> findPath(ArrayList<Tile>queue){
//
//        // arraylist to store the queue for the next level of recursion
//        ArrayList<Tile> next = new ArrayList<Tile>();
//
//        // base cases
//        if (queue.size() == 0){
//            return null;
//        }
//
//        char consideredTile;
//
//        for (Tile tile : queue){
//            // if the current tile is the end, returns a stack with that tile in it
//            if (map.getMap(tile) == Const.END){
//                Stack<Tile> output = new Stack<Tile>();
//                output.push(tile);
//                return output;
//            }
//
//            // for each direction in the directions array
//            // (stores vectors {x_mod, y_mod}, these are values which the x and y are changed by)
//            for (int i = 0; i < directionX.length; i++){
//
//                // makes sure the new tile is in bounds
//                if (tile.isValid(directionX[i], directionY[i], map.getNumRows(), map.getNumCols())){
//                    consideredTile = map.getMap(tile.x+directionX[i], tile.y+directionY[i]);
//
//                    if (consideredTile != Const.WALL && consideredTile != Const.VISITED){
//                        // adds the considered tile to the next queue (queue for next level of recursion)
//                        next.add(new Tile(tile.x+directionX[i], tile.y+directionY[i]));
//
//                        // updates visuals
//                        if (consideredTile == Const.ALLEY){
//                            map.setMap(tile.x+directionX[i], tile.y+directionY[i],Const.QUEUED);
//                        }
//                    }
//                }
//            }
//
//            // if the current tile isnt the start or the end, sets the current tile to visited
//            if (map.getMap(tile) != Const.START && map.getMap(tile) != Const.END){
//                map.setMap(tile.x,tile.y,Const.VISITED);
//            }
//        }
//
//        // recursively calls itself to get the path from the next level of recursion
//        Stack<Tile> path = findPath(next);
//
//        // if no path was found, returns no path found
//        if (path == null){
//            return path;
//        }
//
//        // finds the latest tile in the path, and finds an adjacent tile from the current queue
//        Tile nextTileInPath = path.peek().adjacent(queue);
//        path.push(nextTileInPath);
//        return path;
//    }
//
//    // generates the path on the map, visually
//    public void generatePath(Stack<Tile> path){
//        generatePath(path, false, false);
//    }
//
//    // generates the path on the map, visually
//    public void generatePath(Stack<Tile> path, boolean printTile, boolean moveStartingTile){
//
//        // base cases
//        if (path == null) {
//            System.out.println("Path not found!! :(((");
//            return;
//        }
//        if (path.empty()){
//            return;
//        }
//
//        Tile nextTile = path.pop();
//
//        if (printTile){
//            System.out.println(nextTile);
//        }
//
//        // sets the tile on the map to visited
//        if (moveStartingTile || (map.getMap(nextTile.x, nextTile.y) != Const.START && map.getMap(nextTile.x, nextTile.y) != Const.END)){
//            map.setMap(nextTile.x, nextTile.y, Const.VISIT);
//        }
//
//
//        if (moveStartingTile){
//            map.setMap(nextTile.x, nextTile.y, Const.START);
//        }
//
//        // recursively traverses the path
//        generatePath(path);
//    }
//    class Tile {
//        int x, y;
//    }
//}
