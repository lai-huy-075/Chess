package main.piece;

import java.awt.Color;
import java.util.Objects;

import main.Chess;
import main.board.Tile;

/**
 * Generic Piece for Chess
 */
public abstract class Piece {
	/**
	 * Enumerated type of the piece colors.
	 */
	public static enum PieceColor {
		Black(new Color(0x2c2c2c)), White(new Color(0xd3d3d3));

		/**
		 * {@link Color} of this
		 */
		public final Color color;

		private PieceColor(Color color) {
			this.color = color;
		}
	}

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
	public static final String default_name = "\uFFFD";

	/**
	 * Primitive type array of {@link Tile}.<br>
	 * This holds no elements.
	 */
	protected static final Tile[] empty = new Tile[] {};

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
	 * Constructor
	 * 
	 * @param color {@link PieceColor} of this
	 */
	protected Piece(PieceColor color) {
		this.color = Objects.requireNonNull(color);
		this.reset();
	}
	
	/**
	 * Logs Piece attributes using {@link Chess#logger}
	 */
	public abstract void debug();

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
	public boolean isAlly(Piece piece) {
		if (piece == null)
			return false;
		return this.color == piece.color;
	}

	/**
	 * Determine if this is {@link PieceColor#Black}
	 * 
	 * @return {@code true} if {@link PieceColor#Black}.<br>
	 *         {@code false} otherwise.
	 */
	public boolean isBlack() {
		return PieceColor.Black == this.color;
	}

	/**
	 * Determine if a piece move is legal
	 * 
	 * @param src  {@link Tile} where the piece came from
	 * @param dest {@link Tile} where the piece is goign
	 * @return true is a legal move is done<br>
	 *         false is not a legal move
	 */
	public abstract boolean isLegal(Tile src, Tile dest);

	/**
	 * Determine if this is {@link PieceColor#White}
	 * 
	 * @return {@code true} if {@link PieceColor#White}.<br>
	 *         {@code false} otherwise.
	 */
	public boolean isWhite() {
		return PieceColor.White == this.color;
	}

	/**
	 * Set any piece attributes to their default values.
	 */
	public abstract void reset();

	/**
	 * Converts this into a {@link String} for Algebraic Notation
	 * 
	 * @return Algebraic Notation for this Piece
	 */
	public abstract char toAN();
	
	@Override
	public abstract String toString();
}
