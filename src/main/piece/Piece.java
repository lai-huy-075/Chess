package main.piece;

import java.awt.Color;
import java.util.Objects;

import main.board.Tile;

public abstract class Piece {
	public static enum PieceColor {
		Black(new Color(0x2c2c2c)), White(new Color(0xd3d3d3));

		public final Color color;

		private PieceColor(Color color) {
			this.color = color;
		}
	}
	
	protected static final String black_bishop = "\u265D";
	protected static final String black_king = "\u265A";
	protected static final String black_knight = "\u265E";
	protected static final String black_pawn = "\u265F";
	protected static final String black_queen = "\u265B";
	protected static final String black_rook = "\u265C";
	protected static final String default_name = "\uFFFD";
	protected static final String white_bishop = "\u2657";
	protected static final String white_king = "\u2654";
	protected static final String white_knight = "\u2658";
	protected static final String white_pawn = "\u2659";
	protected static final String white_queen = "\u2655";
	protected static final String white_rook = "\u2656";
	protected static final Tile[] empty = new Tile[] {};

	public final PieceColor color;

	protected Piece(PieceColor color) {
		this.color = Objects.requireNonNull(color);
		this.reset();
	}

	public abstract int getValue();
	
	public abstract Tile[] getTileTraversed(Tile[][] board, Tile src, Tile dest);

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

	public abstract void reset();

	@Override
	public abstract String toString();
}
