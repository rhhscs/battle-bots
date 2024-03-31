package battle.bots.game.objects;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class UnpositionedGameObject extends GameObject {
    protected UnpositionedGameObject(Rectangle hitbox) {
        super(hitbox);
    }

    /**
     * Draws the {@link GameObject} at the specified location.
     * @param g the {@link Graphics} object
     * @param x the x coordinate of the top left corner of the drawing location
     * @param y the y coordinate of the top left corner of the drawing location
     */
    public abstract void draw(Graphics g, int x, int y);
}
