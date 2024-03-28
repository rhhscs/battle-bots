package battle.bots.game;

import battle.bots.game.actions.Action;
import battle.bots.game.actions.Move;
import battle.bots.game.actions.Shoot;
import battle.bots.game.objects.Bullet;
import battle.bots.game.objects.GameObject;
import battle.bots.game.objects.Obstacle;
import battle.bots.game.util.ImmutablePoint;
import org.w3c.dom.css.Rect;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel {
    private final GameObject[][] map;
    private final Set<Bullet> bullets;

    private final Timer gameLoop;

    private int currentCycle;

    private Camera camera = new Camera();

    public GamePanel(List<Bot> bots) {
        // Game map dimensions
        int gridHeight = (int) Math.floor(
                Math.sqrt(
                        bots.size() * Const.TILES_PER_PLAYER 
                )
        );

        if (gridHeight < Const.MIN_HEIGHT) {
            gridHeight = Const.MIN_HEIGHT;
        }

        if (gridHeight > Const.MAX_HEIGHT) {
            gridHeight = Const.MAX_HEIGHT;
        }

        int gridWidth = (int)(Const.TILE_SIZE / Const.TILE_ASPECT_RATIO);
        this.map = new GameObject[gridHeight][gridWidth];
        this.bullets = new HashSet<>();
        this.currentCycle = 1;

        //TODO remove
        for (int i = 0; i < 20; i++) {
            map[(int)(Math.random()*gridHeight)][(int)(Math.random()*gridWidth)] = new Obstacle();
        }

        for (Bot bot : bots) {
            // TODO: make position generation random
            bot.setHitbox(new Rectangle(0, 0, Const.TILE_SIZE, Const.TILE_SIZE));
        }

        this.gameLoop = new Timer();
        this.addComponentListener(new ResizeListener());
    }

    /**
     * start
     * Starts the game.
     */
    public void start() {
        camera.setScale(map, this.getSize(), Mode.FIT);
        this.gameLoop.schedule(new GameLoopTask(), 0, Const.MS_PER_TICK);
    }

    /**
     * runTick
     * Runs an animation frame (a tick).
     * An animation frame is NOT the same as an update cycle.
     * Animation frames occur more often to create a smoother animation.
     */
    public void runTick() {
        for (GameObject[] gameObjects : this.map) {
            for (GameObject currentObject : gameObjects) {
                if (currentObject != null) {
                    currentObject.tick();
                }
            }
        }

        repaint();
    }

    /**
     * runUpdate
     * Runs an update cycle on the map
     */
    public void runUpdate() {
        Map<ImmutablePoint, List<Bot>> moveRegistry = new HashMap<>();

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                GameObject currentObj = this.map[y][x];

                if (currentObj == null) {
                    continue;
                }

                if (currentObj instanceof Bot) {
                    Bot bot = (Bot) currentObj;
                    GameMap gameMap = new GameMap(this.map, bot, new Point(x, y));

                    ImmutablePoint newPos = this.handlePlayer(bot, gameMap);

                    moveRegistry.putIfAbsent(newPos, new ArrayList<>());
                    moveRegistry.get(newPos).add(bot);
                }
            }
        }

        for (Map.Entry<ImmutablePoint, List<Bot>> entry : moveRegistry.entrySet()) {

        }
    }

    public boolean positionIsValid(ImmutablePoint point) {
        if (point == null) {
            throw new NullPointerException("Parameter `point` cannot be null.");
        }

		// TODO
		return false;
    }

    public ImmutablePoint handlePlayer(Bot bot, GameMap gameMap) {
        Action action = bot.update(gameMap);
        Point position = gameMap.getPosition();

        if (action == null) {
            return new ImmutablePoint(position);
        }

        if (action instanceof Move) {
            Move move = (Move) action;

            // TODO
            switch (move.getDirection()) {
                case NORTH: {
                    return new ImmutablePoint(position.x, position.y - 1);
                }
                case SOUTH: {
                    return new ImmutablePoint(position.x, position.y + 1);
                }
                case EAST: {
                    return new ImmutablePoint(position.x - 1, position.y);
                }
                case WEST: {
                    return new ImmutablePoint(position.x + 1, position.y);
                }
                default: {
                    throw new IllegalStateException("Move direction must be either NORTH, SOUTH, EAST, or WEST");
                }
            }
        } else if (action instanceof Shoot) {
            Shoot shoot = (Shoot) action;

            // TODO: spawn bullet
        }

		// TODO: should return the position of the player after the move
		return null;
    }

    /**
     * Paints the game using the {@link Graphics} objects
     * @param g the graphics object
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        camera.process(g);

        // Draw background tiles
        g.setColor(Color.BLUE);

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                int xCoord = x * Const.TILE_SIZE;
                int yCoord = y * Const.TILE_SIZE;

                g.drawRect(xCoord, yCoord, Const.TILE_SIZE, Const.TILE_SIZE);
            }
        }

        // Draw game objects
        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                GameObject currentObject = this.map[y][x];

                if (currentObject == null) {
                    continue;
                }

                int xCoord = x * Const.TILE_SIZE;
                int yCoord = y * Const.TILE_SIZE;

                currentObject.draw(g, xCoord, yCoord);
            }
        }
        
    }

    /**
     * Task responsible for animating (ticks) and running update cycles .
     * @author Harry Xu
     * @version 1.0 - March 23rd 2024
     */
    public class GameLoopTask extends TimerTask {
        private int ticks;

        public GameLoopTask() {
            this.ticks = 0;
        }

        @Override
        public void run() {
            this.ticks++;


            if (this.ticks % Const.TICKS_PER_UPDATE == 0) {
                runUpdate();
            }

            runTick();
        }
    }

    private class ResizeListener extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            camera.setScale(map, getSize(), Mode.FIT);
        }
    }
}
