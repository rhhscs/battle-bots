package battle.bots.game.objects;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Represents a game object in the game which can be drawn and animated
 * @author Harry Xu
 * @version 1.0 - March 24th 2024
 */
public abstract class GameObject {
    private final Rectangle hitbox;

    /**
     * Constructs a {@link GameObject} with a rectangular hitbox.
     * @param hitbox the hitbox of the game object
     */
    protected GameObject(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    /**
     * Gets the hitbox of the game object.
     * @return the object's hitbox
     */
    Rectangle getHitbox() {
        return this.hitbox;
    }

    /**
     * Advances the sprite image
     */
    public abstract void tick();
}
