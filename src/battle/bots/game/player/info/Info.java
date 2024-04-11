package battle.bots.game.player.info;

import battle.bots.game.player.Coordinate;

public abstract class Info {
    private final Coordinate coordinate;

    public Info(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }
}
