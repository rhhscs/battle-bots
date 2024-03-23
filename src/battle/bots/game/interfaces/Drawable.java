package battle.bots.game.interfaces;

import java.awt.Graphics;

/**
 * Interface for all game objects which can be drawn
 * @author Harry Xu
 * @version 1.0 - March 23rd 2024
 */
public interface Drawable {
    /**
     * Draws the game object via a Java awt {@link Graphics} object.
     * @param g the graphics object
     */
    void draw(Graphics g);
}
