package battle.bots.game.objects;

import battle.bots.game.Const;
import battle.bots.game.GameObject;
import battle.bots.game.util.Angle;
import battle.bots.game.util.Vector;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet extends PositionedGameObject {
    private static final double BULLET_SPEED = 50.0 * 1000.0 / Const.MS_PER_UPDATE;
    private final Vector velocity;

    public Bullet(Rectangle hitbox, int x, int y, Angle angle) {
        super(hitbox, x, y);

        this.velocity = new Vector(BULLET_SPEED, angle);
    }

    /**
     * Updates the state of the bullet
     *
     *
     */
    public void update() {
        this.setX((int) (this.getX() + this.velocity.getX()));
        this.setY((int) (this.getY() + this.velocity.getY()));
    }

    /**
     * Draws the {@link GameObject}.
     * @param g the {@link Graphics} object
     */
    @Override
    public void draw(Graphics g) {
        // TODO
        g.setColor(Color.BLACK);
        g.fillOval(this.getX(), this.getY(), Const.TILE_SIZE, Const.TILE_SIZE);
    }

    /**
     * Advances the sprite image
     */
    @Override
    public void tick() {

    }
}
