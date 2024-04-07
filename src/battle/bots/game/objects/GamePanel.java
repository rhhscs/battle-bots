package battle.bots.game.objects;

import battle.bots.game.Const;
import battle.bots.game.GameMap;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel {
    private final UnpositionedGameObject[][] map;
    private final Set<Bullet> bullets;

    private final Timer gameLoop;

    private int currentCycle;

    private final Camera camera;

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

        for (Bot bot : bots) {
            // TODO: make position generation random
            int x = (int) (Math.random() * gridWidth);
            int y = (int) (Math.random() * gridHeight);

            x = y = 10;

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

    public void updateBots() {
        Map<ImmutablePoint, List<Pair<Bot, ImmutablePoint>>> moveRegistry = new HashMap<>();

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                GameObject currentObj = this.map[y][x];

                if (currentObj == null) {
                    continue;
                }

                if (currentObj instanceof Bot) {
                    Bot bot = (Bot) currentObj;
                    Point point = new Point(x, y);
                    // TODO: consider replacing the point with an immutable point
                    GameMap gameMap = new GameMap(this.map, bot, point);

                    ImmutablePoint newPos = this.handlePlayer(bot, gameMap);

                    moveRegistry.putIfAbsent(newPos, new ArrayList<>());
                    moveRegistry.get(newPos).add(new Pair<>(bot, new ImmutablePoint(point)));
                }
            }
        }

        for (Map.Entry<ImmutablePoint, List<Pair<Bot, ImmutablePoint>>> entry : moveRegistry.entrySet()) {
            ImmutablePoint position = entry.getKey();

            // Randomly resolve location conflicts
            if (!this.positionIsVacant(position)) {
                // TODO: maybe visual indicator if the player does an invalid move
                continue;
            }

            // Get random player
            List<Pair<Bot, ImmutablePoint>> bots = entry.getValue();
            Pair<Bot, ImmutablePoint> botInfo;

            if (bots.size() > 1) {
                int index = (int) (Math.random() * bots.size());
                botInfo = bots.get(index);
            } else {
                botInfo = bots.get(0);
            }

            ImmutablePoint prevPosition = botInfo.getSecond();
            Bot bot = botInfo.getFirst();

            this.map[prevPosition.getY()][prevPosition.getX()] = null;
            this.map[position.getY()][position.getX()] = bot;

            // Update player hitbox
            bot.getHitbox().x = position.getX() * Const.TILE_SIZE;
            bot.getHitbox().y = position.getY() * Const.TILE_SIZE;
        }
    }

    public void updateBullets() {
        for (Bullet bullet : this.bullets) {
            bullet.update();
        }
    }

    public void checkCollisions() {
        for (Bullet bullet : this.bullets) {
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

    public void updateMap() {
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

    public boolean positionIsValid(ImmutablePoint point) {
        if (point == null) {
            throw new NullPointerException("Parameter `point` cannot be null.");
        }

        return point.getX() >= 0 &&
                point.getY() >= 0 &&
                point.getY() < this.map.length &&
                point.getX() < this.map[point.getY()].length;
    }

    public boolean positionIsVacant(ImmutablePoint point) {
        boolean positionIsValid = this.positionIsValid(point);

        if (!positionIsValid) {
            return false;
        }

        return !(this.map[point.getY()][point.getX()] instanceof Obstacle);
    }

    public ImmutablePoint handlePlayer(Bot bot, GameMap gameMap) {
        Action action = bot.nextAction(gameMap);
        Point position = gameMap.getPosition();

        if (action == null) {
            return new ImmutablePoint(position);
        }

        if (action instanceof Move) {
            Move move = (Move) action;

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

            int gridX = (int) position.getX();
            int gridY = (int) position.getY();

            int x = gridX * Const.TILE_SIZE;
            int y = gridY * Const.TILE_SIZE;

            int centerX = (int) (x + Bullet.RADIUS);
            int centerY = (int) (y + Bullet.RADIUS);

            Rectangle bulletHitbox = new Rectangle(x, y, Bullet.SIZE, Bullet.SIZE);

            this.bullets.add(new Bullet(bulletHitbox, centerX, centerY, shoot.getAngle()));
        }

		return new ImmutablePoint(position);
    }

    /**
     * Paints the game using the {@link Graphics} objects
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
            camera.setScale(map, getSize(), Camera.Mode.FIT);
        }
    }
}
