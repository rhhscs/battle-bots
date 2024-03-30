package battle.bots.game.objects;

import battle.bots.game.GameObject;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class PositionedGameObject extends GameObject {
    private int x;
    private int y;

    protected PositionedGameObject(Rectangle hitbox, int x, int y) {
        super(hitbox);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Draws the {@link GameObject}.
     * @param g the {@link Graphics} object
     */
    public abstract void draw(Graphics g);
}
