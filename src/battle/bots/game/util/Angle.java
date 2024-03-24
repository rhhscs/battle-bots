package battle.bots.game.util;

/**
 * Represents an angle in either radians or degrees, and allows for easy conversion between the two.
 * Internally, the angle is stored in radians for better ease-of-use with
 * Java's built-in methods in the {@link Math} class.
 * @author Harry Xu
 * @version 1.0 - March 24th 2024
 */
public class Angle {
    private double val;

    public Angle(double val, Unit unit) {
        switch (unit) {
            case DEGREE: {
                this.val = Math.toRadians(val);
                break;
            }
            case RADIAN: {
                this.val = val;
            }
        }
    }

    public double asDegrees() {
        return Math.toDegrees(this.val);
    }

    public double asRadians() {
        return this.val;
    }

    public enum Unit {
        DEGREE,
        RADIAN,
    }
}
