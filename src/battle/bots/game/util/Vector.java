package battle.bots.game.util;

/**
 * Represents a mathematical vector
 * @author Harry Xu
 * @version 1.0 - March 24th 2024
 */
public class Vector {
    private double x;
    private double y;

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

    public Vector(Vector vector) {
        this(vector.x, vector.y);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
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
        this.x = -this.x;

        return this;
    }

    public Vector reflectY() {
        this.y = -this.y;

        return this;
    }

    public Vector scale(double factor) {
        this.x *= factor;
        this.y *= factor;

        return this;
    }

    public Vector normalize() {
        double magnitude = this.getMagnitude();
        this.x /= magnitude;
        this.y /= magnitude;

        return this;
    }

    public Vector add(Vector other) {
        this.x += other.x;
        this.y += other.y;

        return this;
    }


    @Override
    public String toString() {
        return "Vector{" +
                "x=" + this.x +
                ", y=" + this.y +
                '}';
    }
}
