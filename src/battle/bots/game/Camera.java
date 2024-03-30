package battle.bots.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Camera {
    private static final double DEFAULT_SCALE = 0.1;
    private static final float FILL_AMOUNT = 0.95f; // how much of the screen the grid should take up (max)

    private double scale;
    private int x;
    private int y;
    private Bot tracking;

    public Camera() {
        this.scale = DEFAULT_SCALE;
    }

    public void process(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(this.scale, this.scale);
        g2.translate(this.x, this.y);
    }

    public void update() {
        //TODO low priority
    }

    public void trackBot(Bot bot) {
        this.tracking = bot;
    }

    void setScale(GameObject[][] map, Dimension windowSize, Mode mode) {
        if (this.tracking == null) {
            float scaleX = (float) (windowSize.getWidth() / (map[0].length * Const.TILE_SIZE)) * FILL_AMOUNT;
            float scaleY = (float) (windowSize.getHeight() / (map.length * Const.TILE_SIZE))* FILL_AMOUNT;

            if (mode == Mode.FIT) {
                this.scale = Math.min(scaleX, scaleY);
            } else {
                this.scale = Math.max(scaleX, scaleY);
            }

            this.x = (int) ((windowSize.getWidth() - (map[0].length * Const.TILE_SIZE) * scale) / 2);
            this.y = (int) ((windowSize.getHeight() - (map.length * Const.TILE_SIZE) * scale) / 2);
        }
    }

    void setScale(float s) {
        scale = s;
    }

    public enum Mode {
        FIT,
        FILL
    }
}
