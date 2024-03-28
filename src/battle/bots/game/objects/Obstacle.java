package battle.bots.game.objects;

import java.awt.Color;
import java.awt.Graphics;

import battle.bots.game.Const;

public class Obstacle extends GameObject {
    @Override
    public void draw(Graphics g, int x, int y) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, Const.TILE_SIZE, Const.TILE_SIZE);
    }

    @Override
    public void tick() {

    }
}
