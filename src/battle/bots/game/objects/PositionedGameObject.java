package battle.bots.game.objects;

import battle.bots.game.GameObject;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class PositionedGameObject extends GameObject {
    private double x;
    private double y;

    protected PositionedGameObject(Rectangle hitbox, double x, double y) {
        super(hitbox);
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * Draws the {@link GameObject}.
     * @param g the {@link Graphics} object
     */
    public abstract void draw(Graphics g);
}
