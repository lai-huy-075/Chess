package main.piece;

import java.util.Objects;

import main.board.Tile;

/**
 * The queen (&#x2655;, &#x265B;) is the most powerful piece in the game of
 * chess, able to move any number of {@link Tile}s vertically, horizontally or
 * diagonally, combining the power of the {@link Rook} and {@link Bishop}.<br>
 * Each player starts the game with one {@link Queen}, placed in the middle of
 * the first rank next to the {@link King}.<br>
 * Because the {@link Queen} is the strongest piece, a {@link Pawn} is promoted
 * to a {@link Queen} in the vast majority of cases.<br>
 * Read more <a href="https://en.wikipedia.org/wiki/Queen_(chess)">here</a>
 * 
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public class Queen extends Piece {
	/**
	 * Determine if this moves like a {@link Bishop}
	 */
	private boolean bishop;

	/**
	 * Determine if this moves like a {@link Rook}
	 */
	private boolean rook;

	/**
	 * Constructor
	 * 
	 * @param color {@link PieceColor} of this
	 */
	public Queen(final PieceColor color) {
		super(color, PieceType.Queen);
	}

	@Override
	public Tile[] getTileTraversed(final Tile[][] board, final Tile src, final Tile dest) {
		Objects.requireNonNull(board, "Queen must be on a board");
		Objects.requireNonNull(src, "Source tile cannot be null");
		Objects.requireNonNull(dest, "Destination tile cannot be null");

		Tile[] temp;
		int dx, dy;
		if (this.rook) {
			dx = dest.col - src.col;
			dy = dest.row - src.row;

			final int mx = Math.abs(dx);
			final int my = Math.abs(dy);
			temp = new Tile[Math.max(mx, my)];

			for (int i = 0; i < temp.length; ++i)
				temp[i] = board[src.row + i * Integer.signum(dy)][src.col + i * Integer.signum(dx)];

			return temp;
		}
		if (this.bishop) {
			temp = new Tile[Math.abs(src.col - dest.col)];
			dx = dest.col < src.col ? -1 : 1;
			dy = dest.row < src.row ? -1 : 1;
			for (int i = 0; i < temp.length; ++i)
				temp[i] = board[src.row + i * dy][src.col + i * dx];
			return temp;
		}
		throw new IllegalStateException("Queen has made an illegal move.");
	}

	@Override
	public int getValue() {
		return 9;
	}

	@Override
	public boolean isLegal(final Tile src, final Tile dest) {
		this.rook = src.col == dest.col ^ src.row == dest.row;
		this.bishop = Math.abs(src.col - dest.col) == Math.abs(src.row - dest.row);
		return this.rook ^ this.bishop;
	}

	@Override
	public void reset() {
		this.rook = false;
		this.bishop = false;
	}

	@Override
	public String toString() {
		return "Queen [bishop=" + this.bishop + ", rook=" + this.rook + ", color=" + this.color + ", type=" + this.type
				+ ", tile=" + this.tile + "]";
	}
}
