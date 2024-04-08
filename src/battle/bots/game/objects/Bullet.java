package battle.bots.game.objects;

import battle.bots.game.Const;
import battle.bots.game.util.Angle;
import battle.bots.game.util.Vector;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

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

    public Bullet(Rectangle hitbox, double x, double y, Angle angle) {
        super(hitbox, x, y);

        this.velocity = new Vector(BULLET_SPEED, angle);
        this.lastBouncedObject = null;
        this.numBounces = 0;

        this.state = State.ALIVE;
        Timer timer = new Timer();
        timer.schedule(new BulletLifespanTimerTask(), DEFAULT_LIFESPAN);
    }

    public State getState() {
        return this.state;
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
     * Increments this bullet's total bounce counter if it hits a new object. Marks it for removal if exceeds maximum number of bounces.
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
     * Increments this bullet's total bounce counter, marking it for removal if it exceeds the maximum number of bounces.
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

    }

    public enum State {
        ALIVE,
        DEAD,
    }

    private class BulletLifespanTimerTask extends TimerTask {
        /**
         * The action to be performed by this timer task.
         */
        @Override
        public void run() {
            state = State.DEAD;
        }
    }
}
