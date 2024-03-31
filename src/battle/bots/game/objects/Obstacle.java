package battle.bots.game.objects;

import battle.bots.game.Const;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

public class Obstacle extends UnpositionedGameObject {

    private static final int HITBOX_WIDTH = 1;

    private final Rectangle topHitbox;
    private final Rectangle bottomHitbox;
    private final Rectangle leftHitbox;
    private final Rectangle rightHitbox;

    public Obstacle(Rectangle hitbox) {
        super(hitbox);

        this.topHitbox = new Rectangle(hitbox.x, hitbox.y, hitbox.width, 1);
        this.bottomHitbox = new Rectangle(hitbox.x, hitbox.y + hitbox.height - HITBOX_WIDTH, hitbox.width, 1);
        this.leftHitbox = new Rectangle(hitbox.x, hitbox.y, 1, hitbox.height);
        this.rightHitbox = new Rectangle(hitbox.x + hitbox.width - HITBOX_WIDTH, hitbox.y, hitbox.width, 1);
    }

    public void collide(Bullet bullet, Ellipse2D bulletHitbox) {

    }



    @Override
    public void draw(Graphics g, int x, int y) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, Const.TILE_SIZE, Const.TILE_SIZE);
    }

    @Override
    public void tick() {

    }
}
