package battle.bots.game.objects;

import battle.bots.game.Const;
import battle.bots.game.player.GameMap;
import battle.bots.game.actions.Action;
import battle.bots.game.actions.Move;
import battle.bots.game.actions.Shoot;
import battle.bots.game.camera.Camera;
import battle.bots.game.util.ImmutablePoint;
import battle.bots.game.util.Pair;
import battle.bots.game.util.Vector;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A Java Swing Component responsible for executing and rendering the game.
 * @author Harry Xu
 * @version 1.0 - March 23rd 2024
 */
public class GamePanel extends JPanel {
    private final UnpositionedGameObject[][] map;
    private final Set<Bullet> bullets;

    private final Timer gameLoop;
    private int currentCycle;
    private final Camera camera;

    /**
     * Constructs a {@link GamePanel} with a list of {@link Bot}s participating in the game.
     * @param bots the list of bots
     */
    public GamePanel(List<Bot> bots) {
        this.camera = new Camera();

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
        this.map = new UnpositionedGameObject[gridHeight][gridWidth];
        this.bullets = new HashSet<>();
        this.currentCycle = 1;

        // TODO remove
        for (int i = 0; i < 10; i++) {
            int x = (int) (Math.random() * gridWidth);
            int y = (int) (Math.random() * gridHeight);

            this.map[y][x] = new Obstacle(new Rectangle(x * Const.TILE_SIZE, y * Const.TILE_SIZE, Const.TILE_SIZE, Const.TILE_SIZE));
        }

        for (int i = 0; i < 10; i++) {
            int x = (int) (Math.random() * gridWidth);
            int y = (int) (Math.random() * gridHeight);
            int gas = (int) (Math.random() * 10) + 1;

            this.map[y][x] = new Gas(new Rectangle(x * Const.TILE_SIZE, y * Const.TILE_SIZE, Const.TILE_SIZE, Const.TILE_SIZE), gas);
        }

        for (Bot bot : bots) {
            // TODO: make position generation random
            int x = (int) (Math.random() * gridWidth);
            int y = (int) (Math.random() * gridHeight);

            this.map[y][x] = bot;

            Rectangle hitbox = bot.getHitbox().getBounds();
            hitbox.x = x;
            hitbox.y = y;
        }

        this.gameLoop = new Timer();
        this.addComponentListener(new ResizeListener());
    }

    /**
     * start
     * Starts the game.
     */
    public void start() {
        this.camera.setScale(this.map, this.getSize(), Camera.Mode.FIT);
        this.gameLoop.schedule(new GameLoopTask(), 0, Const.MS_PER_TICK);
    }

    /**
     * runTick
     * Runs an animation frame (a tick).
     * An animation frame is NOT the same as an update cycle.
     * Animation frames occur more often to create a smoother animation.
     */
    private void runTick() {
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
    private void runUpdate() {
        this.currentCycle++;

        if (this.currentCycle % Const.UPDATES_PER_MOVE == 0) {
            this.updateBots();
        }

        if (this.currentCycle % Const.UPDATES_PER_BULLET_MOVE == 0) {
            this.updateBullets();
        }

        this.checkCollisions();
        this.updateMap();
    }

    /**
     * Updates the state and position of the bots.
     */
    private void updateBots() {
        Map<ImmutablePoint, List<Pair<Bot, ImmutablePoint>>> moveRegistry = new HashMap<>();

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                GameObject currentObj = this.map[y][x];

                if (currentObj == null) {
                    continue;
                }

                if (currentObj instanceof Bot) {
                    Bot bot = (Bot) currentObj;
                    ImmutablePoint point = new ImmutablePoint(x, y);
                    GameMap gameMap = new GameMap(this.map, bot, point);

                    ImmutablePoint newPos = this.handlePlayer(bot, gameMap);

                    moveRegistry.putIfAbsent(newPos, new ArrayList<>());
                    moveRegistry.get(newPos).add(new Pair<>(bot, point));
                }
            }
        }

        // Moves the player
        for (Map.Entry<ImmutablePoint, List<Pair<Bot, ImmutablePoint>>> entry : moveRegistry.entrySet()) {
            ImmutablePoint position = entry.getKey();

            // Randomly resolve location conflicts
            if (!this.positionIsVacant(position)) {
                // TODO: maybe visual indicator if the player does an invalid move
                continue;
            }

            // Get random player
            List<Pair<Bot, ImmutablePoint>> bots = entry.getValue();
            Pair<Bot, ImmutablePoint> botInfo = null;

            if (bots.size() > 1) {
                // If a player has not moved, then they retain priority over the spot
                for (Pair<Bot, ImmutablePoint> bot : bots) {
                    if (bot.getSecond().equals(position)) {
                        botInfo = bot;
                        break;
                    }
                }

                if (botInfo == null) {
                    int index = (int) (Math.random() * bots.size());
                    botInfo = bots.get(index);
                }
            } else {
                botInfo = bots.get(0);
            }

            ImmutablePoint prevPosition = botInfo.getSecond();
            Bot bot = botInfo.getFirst();

            UnpositionedGameObject currentObject = this.map[position.getY()][position.getX()];

            // Gas collection
            if (currentObject instanceof Gas) {
                Gas gas = (Gas) currentObject;

                bot.setGas(bot.getGas() + gas.getGas());
            }

            this.map[prevPosition.getY()][prevPosition.getX()] = null;
            this.map[position.getY()][position.getX()] = bot;

            // Update player hitbox
            bot.getHitbox().x = position.getX() * Const.TILE_SIZE;
            bot.getHitbox().y = position.getY() * Const.TILE_SIZE;
        }
    }

    /**
     * Updates the state of all the bullets within the game.
     */
    private void updateBullets() {
        for (Bullet bullet : this.bullets) {
            bullet.update();
        }
    }

    /**
     * Checks for collisions between bullets and other {@link PositionedGameObject}s.
     */
    private void checkCollisions() {
        Iterator<Bullet> bulletIterator = this.bullets.iterator();

        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();

            double bulletX = bullet.getX();
            double bulletY = bullet.getY();

            int topGridX = (int) ((bulletX - Bullet.SIZE) / Const.TILE_SIZE);
            int topGridY = (int) ((bulletY - Bullet.SIZE) / Const.TILE_SIZE);

            int bottomGridX = (int) ((bulletX + Bullet.SIZE) / Const.TILE_SIZE);
            int bottomGridY = (int) ((bulletY + Bullet.SIZE) / Const.TILE_SIZE);

            for (int y = topGridY; y <= bottomGridY; y++) {
                for (int x = topGridX; x <= bottomGridX; x++) {
                    if (!this.positionIsValid(new ImmutablePoint(x, y))) {
                        continue;
                    }

                    UnpositionedGameObject gameObject = this.map[y][x];

                    if (gameObject instanceof Obstacle) {
                        Obstacle obstacle = (Obstacle) gameObject;

                        obstacle.bounce(bullet);
                    } else if (gameObject instanceof Bot) {
                        Bot bot = (Bot) gameObject;

                        if (bot.getHitbox().intersects(bullet.getHitbox())) {
                            if (bot.getHealth() > 0) {
                                bot.setHealth(bot.getHealth() - 1); // TODO: dynamic damage?
                                bot.startHurtAnimation();
                                bulletIterator.remove();
                            }
                        }
                    }
                }
            }

            Vector velocity = bullet.getVelocity();

            // Wall Bouncing
            if (bullet.getX() - Bullet.RADIUS < 0) {
                bullet.markBounce();
                velocity.setX(Math.abs(velocity.getX()));
            }

            if (bullet.getX() + Bullet.RADIUS > Const.TILE_SIZE * this.map[0].length) {
                bullet.markBounce();
                velocity.setX(-Math.abs(velocity.getX()));
            }

            if (bullet.getY() - Bullet.RADIUS < 0) {
                bullet.markBounce();
                velocity.setY(Math.abs(velocity.getY()));
            }

            if (bullet.getY() + Bullet.RADIUS > Const.TILE_SIZE * this.map.length) {
                bullet.markBounce();
                velocity.setY(-Math.abs(velocity.getY()));
            }
        }
    }

    /**
     * Checks the state of all game objects and removes any needed.
     */
    private void updateMap() {
        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                GameObject currentObj = this.map[y][x];

                if (currentObj == null) {
                    continue;
                }

                if (currentObj instanceof Bot) {
                    Bot bot = (Bot) currentObj;

                    if (bot.getHealth() <= 0) {
                        this.map[y][x] = null;
                    }
                }
            }
        }

        this.bullets.removeIf(curr -> curr.getState() == Bullet.State.DEAD);
    }

    /**
     * Checks is a position is valid (i.e. it is within bounds).
     * @param point the point to check
     * @return if the position is valid
     * @throws NullPointerException if the {@code point} parameter is null
     */
    private boolean positionIsValid(ImmutablePoint point) {
        if (point == null) {
            throw new NullPointerException("Parameter `point` cannot be null.");
        }

        return point.getX() >= 0 &&
                point.getY() >= 0 &&
                point.getY() < this.map.length &&
                point.getX() < this.map[point.getY()].length;
    }

    /**
     * Checks is a position is valid (i.e. it is within bounds) and vacant ().
     * @param point the point to check
     * @return if the position is valid
     * @throws NullPointerException if the {@code point} parameter is null
     */
    private boolean positionIsVacant(ImmutablePoint point) {
        boolean positionIsValid = this.positionIsValid(point);

        if (!positionIsValid) {
            return false;
        }

        return !(this.map[point.getY()][point.getX()] instanceof Obstacle);
    }

    /**
     * Handles the {@link Action} returned by the {@link Bot#nextAction(GameMap)} method.
     * @param bot the bot returning the {@link Action}
     * @param gameMap a {@link GameMap} instance which holds game map metadata
     */
    private ImmutablePoint handlePlayer(Bot bot, GameMap gameMap) {
        Action action = bot.nextAction(gameMap);
        ImmutablePoint position = gameMap.getPosition();

        if (action == null) {
            return position;
        }

        if (action instanceof Move) {
            Move move = (Move) action;

            // Handling gas
            if (bot.getGas() > 0) {
                bot.setGas(bot.getGas() - 1);
            } else if ((bot.getGas() == 0) && (Math.random() <= Const.OUT_OF_FUEL_PENALTY)) {
                return position;
            }

            switch (move.getDirection()) {
                case NORTH: {
                    return position.translateY(-1);
                }
                case SOUTH: {
                    return position.translateY(1);
                }
                case EAST: {
                    return position.translateX(-1);
                }
                case WEST: {
                    return position.translateX(1);
                }
                default: {
                    throw new IllegalStateException("Move direction must be either NORTH, SOUTH, EAST, or WEST");
                }
            }
        } else if (action instanceof Shoot) {
            Shoot shoot = (Shoot) action;

            // Generates the bullets along the velocity vector, but outside the player's hitbox
            int gridX = position.getX();
            int gridY = position.getY();

            int x = gridX * Const.TILE_SIZE;
            int y = gridY * Const.TILE_SIZE;

            int centerX = (int) (x + Bullet.RADIUS);
            int centerY = (int) (y + Bullet.RADIUS);

            Vector bulletVelocity = new Vector(Bullet.BULLET_SPEED, shoot.getAngle());
            double distance = Const.TILE_SIZE / 2.0 + Bullet.RADIUS;

            if (Math.abs(bulletVelocity.getX()) >= Math.abs(bulletVelocity.getY())) {
                bulletVelocity.scale(distance / Math.abs(bulletVelocity.getX()));
            } else {
                bulletVelocity.scale(distance / Math.abs(bulletVelocity.getY()));
            }

            int translateX = (int) bulletVelocity.getX();
            int translateY = (int) bulletVelocity.getY();

            Rectangle bulletHitbox = new Rectangle(x + translateX, y + translateY, Bullet.SIZE, Bullet.SIZE);
            this.bullets.add(new Bullet(bulletHitbox, centerX + translateX, centerY + translateY, shoot.getAngle()));
        }

        if ((bot.getGas() > 0) && (Math.random() <= Const.IDLING_PENALTY)) {
            bot.setGas(bot.getGas() - 1);
        }

		return position;
    }

    /**
     * Paints the game using the {@link Graphics} objects.
     * @param g the graphics object
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.camera.process(g);

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
                UnpositionedGameObject currentObject = this.map[y][x];

                if (currentObject == null) {
                    continue;
                }

                int xCoord = x * Const.TILE_SIZE;
                int yCoord = y * Const.TILE_SIZE;

                currentObject.draw(g, xCoord, yCoord);
            }
        }

        for (Bullet bullet : this.bullets) {
            bullet.draw(g);
        }
    }

    /**
     * Task responsible for animating (ticks) and running update cycles.
     * @author Harry Xu
     * @version 1.0 - March 23rd 2024
     */
    public class GameLoopTask extends TimerTask {
        private int ticks;

        /** Constructs a {@link GameLoopTask}. */
        public GameLoopTask() {
            this.ticks = 0;
        }

        /** Runs the game loop. */
        @Override
        public void run() {
            this.ticks++;

            if (this.ticks % Const.TICKS_PER_UPDATE == 0) {
                runUpdate();
            }

            runTick();
        }
    }

    /**
     * Sets the camera scale to match the new window size.
     * @author Laison Tao
     * @version 1.0 - March 28rd 2024
     */
    private class ResizeListener extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            camera.setScale(map, getSize(), Camera.Mode.FIT);
        }
    }
}
