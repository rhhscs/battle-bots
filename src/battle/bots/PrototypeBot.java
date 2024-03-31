import battle.bots.game.objects.Bot;
import battle.bots.game.GameMap;
import battle.bots.game.actions.Action;
import battle.bots.game.actions.Move;
import battle.bots.game.actions.Shoot;
import battle.bots.game.util.Angle;

public class PrototypeBot extends Bot {
    private boolean shoot = false;

    @Override
    public Action update(GameMap gameMap) {
        shoot = !shoot;
        if (shoot) {
            return new Shoot(new Angle(45, Angle.Unit.DEGREE));
        } else {
            Move.Direction[] values = Move.Direction.values();
            return new Move(values[(int) (Math.random() * values.length)]);
        }
    }
}
