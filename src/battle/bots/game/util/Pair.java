package battle.bots.game.util;

/**
 * Generic pair class which holds two values.
 * @author Harry Xu
 * @version 1.0 - March 29th 2024
 */
public class Pair<T, U> {
    private final T first;
    private final U second;

    /**
     * Constructs a pair with both values.
     * @param first the first value
     * @param second the second value
     */
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Gets the first value held by this {@link Pair}.
     * @return the first value
     */
    public T getFirst() {
        return this.first;
    }

    /**
     * Gets the second value held by this {@link Pair}.
     * @return the second value
     */
    public U getSecond() {
        return this.second;
    }
}
