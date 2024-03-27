package battle.bots;

import battle.bots.game.Bot;
import battle.bots.game.GameMap;
import battle.bots.game.actions.Action;
import battle.bots.game.actions.Move;

public class PrototypeBot extends Bot {
    @Override
    public Action update(GameMap gameMap) {
        return new Move(Move.Direction.NORTH);
    }
}
