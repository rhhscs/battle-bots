package battle.bots.game.actions;

import battle.bots.game.Const;
import battle.bots.game.player.GameMap;
import battle.bots.game.util.Vector;

/**
 * An object returned from the {@link battle.bots.game.objects.Bot#update(GameMap)} to
 * indicate the movement of the player to a new location on the game map.
 * @author Harry Xu
 * @version 1.0 - March 26th 2024
 */
public class Move implements Action {
    private Direction direction;

    /**
     * Constructs a {@link Move} instance with a direction.
     * @param direction the direction of movement
     */
    public Move(Direction direction) {
        this.direction = direction;
    }

    /**
     * Gets the direction of this {@link Move} object.
     * @return the direction
     */
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public String toString() {
        return "Move{" +
                "direction=" + this.direction +
                '}';
    }

    /**
     * An enum indicating the direction of movement
     * @author Harry Xu
     * @version 1.0 - March 26th 2024
     */
    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST,
    }
}
