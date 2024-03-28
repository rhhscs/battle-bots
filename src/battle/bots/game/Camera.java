package battle.bots.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import battle.bots.game.objects.Obstacle;

public class Camera {
    float scale = 0.1f;
    int x = 0, y = 0;
    Bot tracking = null;
    final static Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
    

    void process(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(scale, scale);
        g2.translate(-x, -y);
    }

    void update(){
        //TODO low priority
    }

    void trackBot(Bot b) {
        tracking = b;
    }
    void setScale(GameMap m) {
        float scaleX = (float) (screenDims.getWidth() / (m.getWidth() * Const.TILE_SIZE));
        float scaleY = (float) (screenDims.getHeight() / (m.getHeight() * Const.TILE_SIZE));

        scale = Math.min(scaleX, scaleY);
    }
}
