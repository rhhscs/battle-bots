package battle.bots.game.actions;

public class Shoot implements Action {
    private final double angle;

    public Shoot(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return this.angle;
    }
}
