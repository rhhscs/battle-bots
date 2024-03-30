package battle.bots.game;

public class Const {
    private static final double SQRT_2 = 1.41421356237;

    /** Game constants */
    public static final int TICKS_PER_UPDATE = 8;
    public static final int MS_PER_TICK = 100;
    public static final int MS_PER_UPDATE = MS_PER_TICK * TICKS_PER_UPDATE;

    /** Map generation constants */
    public static final int TILES_PER_PLAYER = 100;
    public static final int TILE_SIZE = 50;
    public static final double TILE_ASPECT_RATIO = SQRT_2; // >1 for landscape
    public static final int MAX_HEIGHT = 50;
    public static final int MIN_HEIGHT = 25;
}
