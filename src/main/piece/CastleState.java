package main.piece;

/**
 * Result of a Castle made by the {@link King}
 */
public enum CastleState {
    /**
     * If the {@link King} has attempted an invalid castle
     */
    Fail,

    /**
     * If the {@link King} has completed a valid king-side castle
     */
    Kingside,

    /**
     * If the {@link King} has completed a valid queen-side castle
     */
    Queenside,

    /**
     * If the {@link King} did not castle.
     */
    Unattempted;
}
