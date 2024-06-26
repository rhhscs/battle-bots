package battle.bots.game.objects;

import battle.bots.game.Const;
import battle.bots.game.actions.Action;
import battle.bots.game.player.GameMap;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * Gas game object which is collected to refuel a bot
 * @author Harry Xu
 * @version 1.0 - April 8th 2024
 */
public class Gas extends UnpositionedGameObject {
    private final int gas;

    /**
     * Constructs a {@link Gas} object.
     * @param hitbox the hitbox of the game object
     * @param gas the amount of gas the object replenishes
     */
    public Gas(Rectangle hitbox, int gas) {
        super(hitbox);

        if (gas <= 0) {
            throw new IllegalArgumentException("Parameter gas (" + gas + ") must be strictly positive.");
        }

        this.gas = gas;
    }

    public int getGas() {
        return this.gas;
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, Const.TILE_SIZE, Const.TILE_SIZE);

        Rectangle hitbox = this.getHitbox();
        int centerX = hitbox.x + hitbox.width / 2;
        int centerY = hitbox.y + hitbox.height / 2;

        String str = Integer.toString(this.gas);
        FontMetrics fontMetrics = g.getFontMetrics();
        Rectangle2D stringBounds = fontMetrics.getStringBounds(str, g);

        int topLeftX = (int) (centerX - stringBounds.getWidth() / 2);
        int topLeftY = (int) (centerY - stringBounds.getWidth() / 2);

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawString(str, topLeftX, topLeftY);
    }

    @Override
    public void tick() {

    }
}
