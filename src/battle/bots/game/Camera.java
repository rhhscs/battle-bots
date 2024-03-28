package battle.bots.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import battle.bots.game.objects.GameObject;
import battle.bots.game.objects.Obstacle;

public class Camera {
    float scale = 0.1f;
    int x = 0, y = 0;
    Bot tracking = null;
    private final float fillAmount = 0.95f; // how much of the screen the grid should take up (max)
    

    void process(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(scale, scale);
        g2.translate(x, y);
    }

    void update(){
        //TODO low priority
    }

    void trackBot(Bot b) {
        tracking = b;
    }
    void setScale(GameObject[][] map, Dimension windowSize, Mode mode) {
        if (tracking == null) {
            float scaleX = (float) (windowSize.getWidth() / (map[0].length * Const.TILE_SIZE))*fillAmount;
            float scaleY = (float) (windowSize.getHeight() / (map.length * Const.TILE_SIZE))*fillAmount;

            if (mode == Mode.FIT){
                scale = Math.min(scaleX, scaleY);
            } else {
                scale = Math.max(scaleX, scaleY);
            }

            x = (int) ((windowSize.getWidth() - (map[0].length * Const.TILE_SIZE)*scale)/2);
            y = (int) ((windowSize.getHeight() - (map.length * Const.TILE_SIZE)*scale)/2);
        }
    }

    void setScale(float s) {
        scale = s;
    }
}

enum Mode {
    FIT,
    FILL
}
