package battle.bots.game.actions;

public class Move implements Action {

    private Direction direction;

    public Move(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST,
    }
}
