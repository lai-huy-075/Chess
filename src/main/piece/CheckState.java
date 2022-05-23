package main.piece;

/**
 * State of the {@link King} being in Check
 */
public enum CheckState {
    /**
     * {@link King} is in Check
     */
    Check,

    /**
     * {@link King} is not in Check
     */
    Fail,

    /**
     * {@link King} is in Checkmate
     */
    Mate,

    /**
     * {@link King} has been stalemated
     */
    Stale;
}
