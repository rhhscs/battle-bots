package battle.bots.game.objects;

import java.awt.Graphics;

/**
 * Represents a game object in the game which can be drawn and animated
 * @author Harry Xu
 * @version 1.0 - March 24th 2024
 */
public abstract class GameObject {
    /**
     * Draws the {@link GameObject} at the specified location.
     * @param g the {@link Graphics} object
     * @param x the x coordinate of the top left corner of the drawing location
     * @param y the y coordinate of the top left corner of the drawing location
     */
    public abstract void draw(Graphics g, int x, int y);

    /**
     * Advances the sprite image
     */
    public abstract void tick();
}
