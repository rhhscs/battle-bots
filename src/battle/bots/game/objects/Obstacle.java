package battle.bots.game.objects;

import battle.bots.game.Const;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Obstacle extends UnpositionedGameObject {

    private final Rectangle hitbox;

    public Obstacle(Rectangle hitbox) {
        super(hitbox);
        this.hitbox = hitbox;
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, Const.TILE_SIZE, Const.TILE_SIZE);
    }

    @Override
    public void tick() {

    }
}
