package battle.bots.game.util;

import java.awt.Point;
import java.util.Objects;

/**
 * Provides an immutable implementation of a point object,
 * consisting of an abscissa and ordinate.
 * The immutable nature of this object allows for use in hashmaps.
 * @author Harry Xu
 * @version 1.0 - March 27th 2024
 */
public final class ImmutablePoint {
    private final int x;
    private final int y;

    public ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ImmutablePoint(Point point) {
        this(point.x, point.y);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public ImmutablePoint translateX(int dx) {
        return new ImmutablePoint(this.x + dx, this.y);
    }

    public ImmutablePoint translateY(int dy) {
        return new ImmutablePoint(this.x, this.y + dy);
    }

    public Point toAWTPoint() {
        return new Point(this.x, this.y);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        ImmutablePoint that = (ImmutablePoint) other;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "ImmutablePoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
