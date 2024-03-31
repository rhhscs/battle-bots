package battle.bots.game.objects;

import battle.bots.game.Const;
import battle.bots.game.util.Angle;
import battle.bots.game.util.Vector;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet extends PositionedGameObject {
    public static final int SIZE = Const.TILE_SIZE / 2;
    public static final double RADIUS = SIZE / 2.0;

    private static final double BULLET_SPEED = 300.0 * Const.S_PER_BULLET_MOVE;
    private final Vector velocity;

    public Bullet(Rectangle hitbox, double x, double y, Angle angle) {
        super(hitbox, x, y);

        this.velocity = new Vector(BULLET_SPEED, angle);
    }

    public Vector getVelocity() {
        return this.velocity;
    }

    /**
     * Updates the state of the bullet
     *
     *
     */
    public void update() {
        this.setX(this.getX() + this.velocity.getX());
        this.setY(this.getY() + this.velocity.getY());

        int topLeftX = (int) (this.getX() - RADIUS);
        int topLeftY = (int) (this.getY() - RADIUS);

        this.getHitbox().x = topLeftX;
        this.getHitbox().y = topLeftY;
    }

    /**
     * Draws the {@link GameObject}.
     * @param g the {@link Graphics} object
     */
    @Override
    public void draw(Graphics g) {
        // TODO
        g.setColor(Color.BLACK);
        g.fillOval((int) (this.getX() - RADIUS), (int) (this.getY() - RADIUS), SIZE, SIZE);
    }

    /**
     * Advances the sprite image
     */
    @Override
    public void tick() {

    }
}
