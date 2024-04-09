package battle.bots.game.actions;

import battle.bots.game.player.GameMap;
import battle.bots.game.util.Angle;

/**
 * An object returned from the {@link battle.bots.game.objects.Bot#update(GameMap)} to
 * indicate that the player has shot a bullet.
 * @author Harry Xu
 * @version 1.0 - March 29th 2024
 */
public class Shoot implements Action {
    private final Angle angle;

    /**
     * Constructs a {@link Shoot} instance with an angle.
     * The angle indicates the direction of movement from the positive x-axis,
     * going <b>clockwise</b>.
     * @param angle the angle of the shot
     */
    public Shoot(Angle angle) {
        this.angle = angle;
    }

    /**
     * Gets the angle of the shot as an {@link Angle} object.
     * @return the angle of the shot.
     */
    public Angle getAngle() {
        return this.angle;
    }
}
