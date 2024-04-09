package battle.bots.game.objects;

import battle.bots.game.Const;
import battle.bots.game.util.Angle;
import battle.bots.game.util.Vector;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A bullet which travels across the map at a constant velocity
 * @author Harry Xu
 * @version 1.0 - March 29th 2024
 */
public class Bullet extends PositionedGameObject {
    public static final int DEFAULT_LIFESPAN = Const.MS_PER_BULLET_MOVE * 100;
    public static final int SIZE = Const.TILE_SIZE / 2;
    public static final double RADIUS = SIZE / 2.0;
    public static final int DEFAULT_MAX_BOUNCES = 2;
    public static final double BULLET_SPEED = 300.0 * Const.S_PER_BULLET_MOVE;

    private final Vector velocity;
    private State state;
    private GameObject lastBouncedObject;
    private int numBounces;

    /**
     * Constructs a {@link Bullet} with a hitbox, position, and angle of movement
     * @param hitbox the hitbox of the bullet
     * @param x the x coordinate of the bullet
     * @param y the y coordinate of the bullet
     * @param angle the angle of movement of the bullet.
     *              The angle is from the positive x-angle, and travels <b>clockwise</b> around
     */
    public Bullet(Rectangle hitbox, double x, double y, Angle angle) {
        super(hitbox, x, y);

        this.velocity = new Vector(BULLET_SPEED, angle);
        this.lastBouncedObject = null;
        this.numBounces = 0;
        this.state = State.ALIVE;

        Timer timer = new Timer();
        timer.schedule(new BulletLifespanTimerTask(), DEFAULT_LIFESPAN);
    }

    /**
     * Gets the state of the bullet, either {@link State#ALIVE} or {@link State#DEAD}.
     * @return the state of the bullet
     */
    public State getState() {
        return this.state;
    }

    /**
     * Gets the velocity vector of the bullet.
     * @return the velocity of the bullet
     */
    public Vector getVelocity() {
        return this.velocity;
    }

    /** Updates the state of the bullet. */
    public void update() {
        this.setX(this.getX() + this.velocity.getX());
        this.setY(this.getY() + this.velocity.getY());

        int topLeftX = (int) (this.getX() - RADIUS);
        int topLeftY = (int) (this.getY() - RADIUS);

        this.getHitbox().x = topLeftX;
        this.getHitbox().y = topLeftY;
    }

    /**
     * Increments this bullet's total bounce counter if it hits a new object.
     * Marks it for removal if exceeds maximum number of bounces ({@link Bullet#DEFAULT_MAX_BOUNCES}).
     * @param gameObject The object the bullet is bouncing off of.
     */
    public void markBounce(GameObject gameObject) {
        if (gameObject == null || gameObject.equals(this.lastBouncedObject)) {
            return;
        }

        this.numBounces++;
        this.lastBouncedObject = gameObject;

        if (this.numBounces > DEFAULT_MAX_BOUNCES) {
            this.state = State.DEAD;
        }
    }

    /**
     * Increments this bullet's total bounce counter,
     * marking it for removal if it exceeds the maximum number of bounces ({@link Bullet#DEFAULT_MAX_BOUNCES}).
     */
    public void markBounce() {
        this.numBounces++;
        this.lastBouncedObject = null;

        if (this.numBounces > DEFAULT_MAX_BOUNCES) {
            this.state = State.DEAD;
        }
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
        // TODO
    }

    /**
     * The state of the bullet.
     * @author Harry Xu
     * @version 1.0 - March 29th 2024
     */
    public enum State {
        ALIVE,
        DEAD,
    }

    /**
     * A {@link TimerTask} run after {@link Bullet#DEFAULT_LIFESPAN} has elapsed.
     * @author Harry Xu
     * @version 1.0 - March 29th 2024
     */
    private class BulletLifespanTimerTask extends TimerTask {
        /**
         * The action to be performed by this timer task.
         * Marks the bullet as {@link State#DEAD}.
         */
        @Override
        public void run() {
            state = State.DEAD;
        }
    }
}
