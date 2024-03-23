package battle.bots.game.objects;

import battle.bots.game.actions.Action;

import java.awt.Graphics;

public abstract class Bot extends GameObject {
    public Bot() {
        super();
    }

    public abstract Action update();

    @Override
    public void draw(Graphics g) {

    }
}
