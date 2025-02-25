package net.chess.chess.piece;

import net.chess.chess.board.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * A chess piece, or chessman, is a game piece that is placed on a chessboard to
 * play the game of chess. It can be either {@link PieceColor#White} or
 * {@link PieceColor#Black}, and it can be one of six types: {@link King},
 * {@link Queen}, {@link Rook}, {@link Bishop}, {@link Knight}, or {@link Pawn}.
 * </p>
 *
 * <p>
 * Chess sets generally come with sixteen pieces of each color. Additional
 * pieces, usually an extra {@link Queen} per color, may be provided for use in
 * promotion.
 * </p>
 * Read more <a href="https://en.wikipedia.org/wiki/Chess_piece">here</a>
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public abstract class Piece {
    /**
     * {@link List} of {@link Tile}.<br>
     * This holds no elements.
     */
    protected static final List<Tile> empty = new ArrayList<>(0);

    /**
     * {@link PieceColor}
     */
    public final PieceColor color;

    /**
     * {@link Tile} this is on
     */
    protected Tile tile;

    /**
     * {@link PieceType}
     */
    public final PieceType type;

    /**
     * Constructor
     *
     * @param color {@link PieceColor} of this
     */
    protected Piece(final PieceColor color, final PieceType type) {
        this.color = Objects.requireNonNull(color, "PieceColor cannot be null");
        this.type = Objects.requireNonNull(type, "PieceType cannot be null");
        this.reset();
    }

    /**
     * Get {@link #tile}
     *
     * @return {@link #tile}i
     */
    public Tile getTile() {
        return this.tile;
    }

    /**
     * Get a primitive type array of the tiles traversed from src to dest
     *
     * @param board 2d primitive type array holding the board
     * @param src   source {@link Tile}
     * @param dest  destination {@link Tile}
     * @return {@link Tile}s traversed
     */
    public abstract List<Tile> getTileTraversed(Tile[][] board, Tile src, Tile dest);

    /**
     * Return point value of each piece.<br>
     * Calling this method on the {@link King} will throw
     * {@link IllegalStateException}
     *
     * @return piece point value
     */
    public abstract int getValue();

    /**
     * Determine if a {@link Piece} is an <i>ally</i>.
     *
     * @param piece is the {@code Piece} to compare.
     * @return {@code true} if {@code Piece} is an <i>ally</i>.<br>
     * {@code false} if {@code Piece} is not an <i>ally</i> or is null.
     */
    public boolean isAlly(final Piece piece) {
        if (piece == null)
            return false;
        return this.color == piece.color;
    }

    /**
     * Determine if a piece move is legal
     *
     * @param src  {@link Tile} where the piece came from
     * @param dest {@link Tile} where the piece is going
     * @return true is a legal move is done<br>
     * false is not a legal move
     */
    public abstract boolean isLegal(Tile src, Tile dest);

    /**
     * Set any piece attributes to their default values.
     */
    public abstract void reset();

    /**
     * Set {@link #tile}
     *
     * @param tile {@link Tile} to set
     */
    public void setTile(final Tile tile) {
        this.tile = tile;
    }

    /**
     * Converts this into a char for Forsyth-Edwards Notation
     *
     * @return Forsyth-Edwards Notation of this Piece
     */
    public final char toFEN() {
        return switch (this.color) {
            case Black -> Character.toLowerCase(this.type.an);
            case White -> Character.toUpperCase(this.type.an);
        };
    }

    /**
     * Converts this into a {@link String} to display on the {@link net.chess.chess.board.Chessboard}
     *
     * @return {@link String} to display
     */
    public final String toFigure() {
        return switch (this.color) {
            case Black -> String.valueOf(this.type.black);
            case White -> String.valueOf(this.type.white);
        };
    }

    @Override
    public abstract String toString();
}
