package battle.bots.game.player;

import battle.bots.game.Const;
import battle.bots.game.util.ImmutablePoint;

import java.util.Objects;

public class Coordinate {
    private final ImmutablePoint point;

    public Coordinate(ImmutablePoint point, Type type) {
        switch (type) {
            case NORMAL:
                this.point = point;
                break;
            case GRID:
                this.point = new ImmutablePoint(
                        point.getX() * Const.TILE_SIZE,
                        point.getY() * Const.TILE_SIZE
                );
                break;
            default:
                throw new IllegalArgumentException("Parameter `type` must either be NORMAL or GRID.");
        }
    }

    public ImmutablePoint getNormalCoordinates() {
        return this.point;
    }

    public ImmutablePoint getGridCoordinates() {
        return new ImmutablePoint(
                this.point.getX() / Const.TILE_SIZE,
                this.point.getY() / Const.TILE_SIZE
        );
    }

    public enum Type {
        NORMAL,
        GRID,
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Coordinate that = (Coordinate) o;
        return Objects.equals(this.point, that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.point);
    }
}
