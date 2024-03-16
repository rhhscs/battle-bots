package battle.bots.game;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final List<Bot> bots;
    private final List<Bullet> bullets;

    public Game() {
        this.bots = new ArrayList<>();
        this.bullets = new ArrayList<>();
    }
}
