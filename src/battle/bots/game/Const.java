package battle.bots.game;

public class Const {
    private static final double SQRT_2 = 1.41421356237;

    /** Game constants */
    public static final int TICKS_PER_UPDATE = 4;
    public static final int MS_PER_TICK = 10;
    public static final int MS_PER_UPDATE = MS_PER_TICK * TICKS_PER_UPDATE;

    public static final int UPDATES_PER_GRID_MOVE = 4;
    public static final int UPDATES_PER_MOVE = 1;

    public static final int MS_PER_MOVE = MS_PER_UPDATE * UPDATES_PER_GRID_MOVE;
    public static final double S_PER_MOVE = MS_PER_MOVE / 1000.0;
    public static final int MOVES_PER_GRID_MOVE = UPDATES_PER_GRID_MOVE / UPDATES_PER_MOVE;
    public static final int UPDATES_PER_BULLET_MOVE = 1;
    public static final int MS_PER_BULLET_MOVE = MS_PER_UPDATE * UPDATES_PER_BULLET_MOVE;
    public static final double S_PER_BULLET_MOVE = MS_PER_BULLET_MOVE / 1000.0;

    /** Map generation constants */
    public static final int TILES_PER_PLAYER = 100;
    public static final int TILE_SIZE = 50;
    public static final double TILE_ASPECT_RATIO = SQRT_2; // > 1 for landscape
    public static final int MAX_HEIGHT = 50;
    public static final int MIN_HEIGHT = 25;
    public static final int[] CHANGE_IN_X = {1, -1, 0, 0};
    public static final int[] CHANGE_IN_Y = {0, 0, -1, 1};

    /** Game behaviour constants */
    // Approximate percentage of moves lost when a player is out of fuel
    public static final double OUT_OF_FUEL_PENALTY = 0.5;

    // Approximate percentage of fuel post when spent idling
    public static final double IDLING_PENALTY = 0.5;
}
