package net.chess.chess.piece;

/**
 * Enumerated type to determine Piece type
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 03 15
 */
public enum PieceType {
    Bishop('\u2657', '\u265D', 'B'), King('\u2654', '\u265A', 'K'), Knight('\u2658', '\u265E', 'N'),
    Pawn('\u2659', '\u265F', 'P'), Queen('\u2655', '\u265B', 'Q'), Rook('\u2656', '\u265C', 'R');

    /**
     * Representation of this piece in Algebraic Notation
     */
    public final char an;

    /**
     * Unicode character for the black piece
     */
    public final char black;

    /**
     * Unicode character for the white piece
     */
    public final char white;

    /**
     * Constructor
     *
     * @param white {@link #white}
     * @param black {@link #black}
     * @param an    {@link #an}
     */
    private PieceType(final char white, final char black, final char an) {
        this.white = white;
        this.black = black;
        this.an = an;
    }
}
