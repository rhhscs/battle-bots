package battle.bots.game.camera;

import battle.bots.game.Const;
import battle.bots.game.objects.GameObject;
import battle.bots.game.objects.Bot;
import battle.bots.game.objects.UnpositionedGameObject;
import battle.bots.game.player.GameMap;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * A camera used to modify the rendering of the game on the Java Swing Frame.
 * @author Laison Tao
 * @version 1.0 - March 28th 2024
 */
public class Camera {
    private static final double DEFAULT_SCALE = 0.1;

    // how much of the screen the grid should take up (max)
    private static final float FILL_AMOUNT = 0.95f;

    private double scale;
    private int x;
    private int y;
    private Bot tracking;

    /**
     * Constructs a camera with a default scale.
     */
    public Camera() {
        this.scale = DEFAULT_SCALE;
    }

    /**
     * Process the graphic rendering by applying {@link java.awt.geom.AffineTransform}.
     * @param g the graphics context object
     */
    public void process(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(this.scale, this.scale);
        g2.translate(this.x, this.y);
    }

    /**
     * TODO
     */
    public void update() {
        //TODO low priority
    }

    /**
     * Sets the camera to track a certain bot on the map
     * @param bot the bot to track
     */
    public void trackBot(Bot bot) {
        this.tracking = bot;
    }

    /**
     * Sets the scaling factor, taking into account the map and window size
     * @param map the game map
     * @param windowSize the size of the containing {@link battle.bots.game.objects.GamePanel}
     * @param mode the camera mode
     */
    public void setScale(UnpositionedGameObject[][] map, Dimension windowSize, Mode mode) {
        if (this.tracking == null) {
            float scaleX = (float) (windowSize.getWidth() / (map[0].length * Const.TILE_SIZE)) * FILL_AMOUNT;
            float scaleY = (float) (windowSize.getHeight() / (map.length * Const.TILE_SIZE))* FILL_AMOUNT;

            // TODO
            if (mode == Mode.FIT) {
                this.scale = Math.min(scaleX, scaleY);
            } else {
                this.scale = Math.max(scaleX, scaleY);
            }

            this.x = (int) ((windowSize.getWidth() - (map[0].length * Const.TILE_SIZE) * this.scale) / 2);
            this.y = (int) ((windowSize.getHeight() - (map.length * Const.TILE_SIZE) * this.scale) / 2);
        }
    }

    /**
     * Sets the scaling factor of the camera
     * @param scale the scaling factor
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * The camera mode
     * @author Laison Tao
     * @version 1.0 - March 28th 2024
     */
    public enum Mode {
        FIT,
        FILL
    }
}
