package battle.bots.game.objects;

import battle.bots.game.Const;
import battle.bots.game.util.Vector;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Obstacle extends UnpositionedGameObject {
    public Obstacle(Rectangle hitbox) {
        super(hitbox);
    }

    public void collide(Bullet bullet) {
        Rectangle bulletHitbox = bullet.getHitbox();

        if (bulletHitbox.intersects(this.getHitbox())) {
            Rectangle intersection = bulletHitbox.intersection(this.getHitbox());
            Vector velocity = bullet.getVelocity();

            int centerX = intersection.x + intersection.width / 2;
            int centerY = intersection.y + intersection.height / 2;

            if (bullet.getX() < centerX) {
                velocity.setX(-Math.abs(velocity.getX()));
            }

            if (bullet.getX() > centerX) {
                velocity.setX(Math.abs(velocity.getX()));
            }

            if (bullet.getY() < centerY) {
                velocity.setY(-Math.abs(velocity.getY()));
            }

            if (bullet.getY() > centerY) {
                velocity.setY(Math.abs(velocity.getY()));
            }
        }

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
