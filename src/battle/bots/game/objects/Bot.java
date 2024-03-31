package battle.bots.game.objects;

import battle.bots.game.Const;
import battle.bots.game.GameMap;
import battle.bots.game.GameObject;
import battle.bots.game.actions.Action;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public abstract class Bot extends UnpositionedGameObject {
    private static final int DEFAULT_HEALTH = 10;
    private static final int DEFAULT_RANGE = 1;

    public static final int MIN_HEALTH = 0;
    public static final int MIN_RANGE = 1;

    private int currentSprite;
    private Image[] sprites;

    private String name;
    private int health;
    private int range;

    private final Color color;

    public Bot() {
        super(new Rectangle(0, 0, Const.TILE_SIZE, Const.TILE_SIZE));
        this.health = DEFAULT_HEALTH;
        this.range = DEFAULT_RANGE;
        this.color = new Color((float) Math.random(), (float)Math.random(), (float)Math.random());
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
     * Sets the range of the player.
     * @param range the new range of the player
     * @throws IllegalArgumentException if {@code range} is below {@link Bot#MIN_RANGE}
     */
    void setRange(int range) {
        if (range < MIN_RANGE) {
            throw new IllegalArgumentException("Parameter `range` (" + range + ") cannot be below the minimum range (" + MIN_RANGE + ").");
        }

        this.range = range;
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
     * Gets the range of the player.
     * @return the range
     */
    public int getRange() {
        return this.range;
    }

    /**
     * Called by the game to update the state of the player.
     * @param gameMap a {@link GameMap} object which provides map-related functionality
     * @return an {@link Action} representing the action of the player
     */
    public abstract Action update(GameMap gameMap);

    /**
     * Draws the {@link GameObject} at the specified location.
     * @param g the {@link Graphics} object
     * @param x the x coordinate of the top left corner of the drawing location
     * @param y the y coordinate of the top left corner of the drawing location
     */
    @Override
    public void draw(Graphics g, int x, int y) {
        g.setColor(this.color);
        g.fillRect(x, y, Const.TILE_SIZE, Const.TILE_SIZE);
//        Image sprite = this.sprites[this.currentSprite];
//        g.drawImage(sprite, x, y, null);
    }

    /**
     * Advances the sprite image
     */
    @Override
    public void tick() {
//        this.currentSprite = (this.currentSprite + 1) % this.sprites.length;
    }
}
