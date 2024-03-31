package battle.bots.game.util;

/**
 * Represents a mathematical vector
 * @author Harry Xu
 * @version 1.0 - March 24th 2024
 */
public class Vector {
    private final double x;
    private final double y;

    /**
     * Constructs a {@link Vector} with x and y components.
     * @param x the x component of the vector
     * @param y the y component of the vector
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a {@link Vector} with a magnitude and direction.
     * @param magnitude the magnitude of the vector
     * @param angle the angle of the vector between the vector and the positive x-axis
     * @throws IllegalArgumentException if the magnitude of the vector is negative
     */
    public Vector(double magnitude, Angle angle) {
        this(magnitude * Math.cos(angle.asRadians()), magnitude * Math.sin(angle.asRadians()));

        if (magnitude < 0) {
            throw new IllegalArgumentException("Magnitude must be non-negative.");
        }
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getMagnitude() {
        return Math.hypot(this.x, this.y);
    }

    public Angle getAngle() {
        if (this.y > 0) {
            return new Angle(Math.atan2(this.y, this.x), Angle.Unit.RADIAN);
        } else {
            return new Angle(2 * Math.PI + Math.atan2(this.y, this.x), Angle.Unit.RADIAN);
        }
    }

    public Vector reflectX() {
        return new Vector(
            -this.x,
            this.y
        );
    }

    public Vector reflectY() {
        return new Vector(
                this.x,
                -this.y
        );
    }

    public Vector scale(double factor) {
        return new Vector(
            this.x * factor,
            this.y * factor
        );
    }

    public Vector normalize() {
        double magnitude = this.getMagnitude();

        return new Vector(
            this.x / magnitude,
            this.y / magnitude
        );
    }


    @Override
    public String toString() {
        return "Vector{" +
                "x=" + this.x +
                ", y=" + this.y +
                '}';
    }
}
