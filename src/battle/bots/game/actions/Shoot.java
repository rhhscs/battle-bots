package battle.bots.game.actions;

import battle.bots.game.util.Angle;

public class Shoot implements Action {
    private final Angle angle;

    public Shoot(Angle angle) {
        this.angle = angle;
    }

    public Angle getAngle() {
        return this.angle;
    }
}
