package main.piece;

import java.util.Objects;

import main.Chess;
import main.board.Tile;

/**
 * Generic Piece for Chess
 */
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
	 * Algebraic Notation for the Bishop
	 */
	public static final char an_bishop = 'B';

	/**
	 * Algebraic Notation for the King
	 */
	public static final char an_king = 'K';

	/**
	 * Algebraic Notation for the Knight
	 */
	public static final char an_knight = 'N';

	/**
	 * Algebraic Notation for the Pawn
	 */
	public static final char an_pawn = '\0';

	/**
	 * Algebraic Notation for the Queen
	 */
	public static final char an_queen = 'Q';

	/**
	 * Algebraic Notation for the Rook
	 */
	public static final char an_rook = 'R';

	/**
	 * {@link String} representing the Black Bishop
	 */
	public static final String black_bishop = "\u265D";

	/**
	 * {@link String} representing the Black King
	 */
	public static final String black_king = "\u265A";

	/**
	 * {@link String} representing the Black Knight
	 */
	public static final String black_knight = "\u265E";

	/**
	 * {@link String} representing the Black Pawn
	 */
	public static final String black_pawn = "\u265F";

	/**
	 * {@link String} representing the Black Queen
	 */
	public static final String black_queen = "\u265B";

	/**
	 * {@link String} representing the Black Rook
	 */
	public static final String black_rook = "\u265C";

	/**
	 * {@link String} representing the Default Piece
	 */
	public static final String default_name = "\ufffd";

	/**
	 * Primitive type array of {@link Tile}.<br>
	 * This holds no elements.
	 */
	protected static final Tile[] empty = {};

	/**
	 * {@link String} representing the White Bishop
	 */
	public static final String white_bishop = "\u2657";

	/**
	 * {@link String} representing the White King
	 */
	public static final String white_king = "\u2654";

	/**
	 * {@link String} representing the White Knight
	 */
	public static final String white_knight = "\u2658";

	/**
	 * {@link String} representing the White Pawn
	 */
	public static final String white_pawn = "\u2659";

	/**
	 * {@link String} representing the White Queen
	 */
	public static final String white_queen = "\u2655";

	/**
	 * {@link String} representing the White Rook
	 */
	public static final String white_rook = "\u2656";

	/**
	 * {@link PieceColor}
	 */
	public final PieceColor color;

	/**
	 * {@link Tile} this is on
	 */
	protected Tile tile;

	/**
	 * Constructor
	 * 
	 * @param color {@link PieceColor} of this
	 */
	protected Piece(final PieceColor color) {
		this.color = Objects.requireNonNull(color);
		this.reset();
	}

	/**
	 * Logs Piece attributes using {@link Chess#logger}
	 * 
	 * @return debug {@link String}
	 */
	public abstract String debug();

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
	public abstract Tile[] getTileTraversed(Tile[][] board, Tile src, Tile dest);

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
	 * 
	 * @return {@code true} if {@code Piece} is an <i>ally</i>.<br>
	 *         {@code false} if {@code Piece} is not an <i>ally</i> or is null.
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
	 *         false is not a legal move
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
	public void setTile(Tile tile) {
		this.tile = tile;
	}

	/**
	 * Converts this into a char for Algebraic Notation
	 * 
	 * @return Algebraic Notation for this Piece
	 */
	public abstract char toAN();

	/**
	 * Converts this into a char for Forsyth-Edwards Notation
	 * 
	 * @return Forsyth-Edwards Notation of this Piece
	 */
	public abstract char toFEN();

	@Override
	public abstract String toString();
}
