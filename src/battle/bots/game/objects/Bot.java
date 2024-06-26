package battle.bots.game.objects;

import battle.bots.game.Const;
import battle.bots.game.player.GameMap;
import battle.bots.game.actions.Action;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Abstract base class for all user developed bots.
 * Contains one abstract method {@link Bot#update(GameMap)} which should be overriden.
 * This method returns an {@link Action}, indicating the next move by the bot.
 * @author Harry Xu
 * @version 1.0 - March 24th 2024
 */
public abstract class Bot extends PositionedGameObject {
    private static final int DAMAGE_ANIMATION_LENGTH = Const.MS_PER_TICK * 2;
    private static final int DEFAULT_HEALTH = 10;
    private static final int DEFAULT_GAS = 10;

    public static final int MIN_HEALTH = 0;
    public static final int MIN_GAS = 0;

    private final Timer damageAnimationTimer;
    private SpriteState spriteState;
    private int currentSprite;
    private Image[] sprites;

    private String name;
    private int health;
    private int gas;

    private final Color color;

    /**
     * Constructs a {@link Bot}.
     */
    public Bot() {
        super(new Rectangle(0, 0, Const.TILE_SIZE, Const.TILE_SIZE), 0, 0);
        this.health = DEFAULT_HEALTH;
        this.gas = DEFAULT_GAS;
        this.color = new Color((float) Math.random(), (float)Math.random(), (float)Math.random());
        this.spriteState = SpriteState.NORMAL;
        this.damageAnimationTimer = new Timer();
    }

    /**
     * Sets the name of the player.
     * @param name the new name of the player
     * @throws NullPointerException if {@code name} is {@code null}
     */
    void setName(String name) {
        if (name == null) {
            throw new NullPointerException("Parameter `name` cannot be null.");
        }

        this.name = name;
    }

    /**
     * Sets the health of the player.
     * @param health the new health of the player
     * @throws IllegalArgumentException if {@code health} is below {@link Bot#MIN_HEALTH}
     */
    void setHealth(int health) {
        if (health < MIN_HEALTH) {
            throw new IllegalArgumentException("Parameter `health` (" + health + ") cannot be below the minimum health (" + MIN_HEALTH + ").");
        }

        this.health = health;
    }

    /**
     * Sets the gas of the player.
     * @param gas the new health of the player
     * @throws IllegalArgumentException if {@code health} is below {@link Bot#MIN_HEALTH}
     */
    void setGas(int gas) {
        if (gas < MIN_GAS) {
            throw new IllegalArgumentException("Parameter `health` (" + gas + ") cannot be below the minimum health (" + MIN_GAS + ").");
        }

        this.gas = gas;
    }

    /**
     * Sets the state of the bot's sprite.
     * @param spriteState the new state of the bot
     */
    synchronized void setSpriteState(SpriteState spriteState) {
        this.spriteState = spriteState;
    }

    /**
     * Starts the hurt animation of the bot.
     */
    void startHurtAnimation() {
        if (this.spriteState == SpriteState.HURT) {
            this.damageAnimationTimer.purge();
        }

        this.spriteState = SpriteState.HURT;

        DamageAnimationTimerTask task = new DamageAnimationTimerTask();
        this.damageAnimationTimer.schedule(task, DAMAGE_ANIMATION_LENGTH);
    }

    /**
     * Gets the name of the player.
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the health of the player.
     * @return the health
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Gets the gas of the player.
     * @return the gas
     */
    public int getGas() {
        return this.gas;
    }

    /**
     * Called by the game to update the state of the player.
     * Wraps a call to {@link #update(GameMap)} and provides additionally game-related functionality
     * @param gameMap a {@link GameMap} object which provides map-related functionality
     * @return an {@link Action} representing the action of the player
     */
    public Action nextAction(GameMap gameMap) {
        return this.update(gameMap);
    }

    /**
     * Updates the state of the player.
     * @param gameMap a {@link GameMap} object which provides map-related functionality
     * @return an {@link Action} representing the action of the player
     */
    public abstract Action update(GameMap gameMap);

    /**
     * Draws the {@link GameObject} at its location.
     * @param g the {@link Graphics} object
     */
    @Override
    public void draw(Graphics g) {
        if (this.spriteState == SpriteState.NORMAL) {
            g.setColor(this.color);
        } else if (this.spriteState == SpriteState.HURT) {
            g.setColor(Color.RED);
        }

        g.fillRect((int) this.getX(), (int) this.getY(), Const.TILE_SIZE, Const.TILE_SIZE);
//        Image sprite = this.sprites[this.currentSprite];
//        g.drawImage(sprite, x, y, null);
    }

    /**
     * Advances the sprite image.
     */
    @Override
    public void tick() {
        // TODO
        // this.currentSprite = (this.currentSprite + 1) % this.sprites.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        // TODO

        Bot bot = (Bot) o;
        return Objects.equals(name, bot.name);
    }

    @Override
    public int hashCode() {
        // TODO
        return Objects.hash(name);
    }

    /**
     * The state of the sprite of the {@link Bot}.
     * @author Harry Xu
     * @version 1.0 - April 4th 2024
     */
    public enum SpriteState {
        NORMAL,
        HURT,
    }

    /**
     * Ends the damage.
     * @author Harry Xu
     * @version 1.0 - April 4th 2024
     */
    private class DamageAnimationTimerTask extends TimerTask {
        /**
         * Ends the damage animation by resetting the {@link Bot#spriteState} to {@link SpriteState#NORMAL}.
         */
        @Override
        public void run() {
            setSpriteState(SpriteState.NORMAL);
        }
    }
}
