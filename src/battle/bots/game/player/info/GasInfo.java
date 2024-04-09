package battle.bots.game.player.info;

import battle.bots.game.objects.Gas;
import battle.bots.game.player.Coordinate;

public class GasInfo extends Info {
    private final Gas gas;
    
    public GasInfo(Coordinate coordinate, Gas gas) {
        super(coordinate);

        this.gas = gas;
    }

    public int getGas() {
        return this.gas.getGas();
    }
}
