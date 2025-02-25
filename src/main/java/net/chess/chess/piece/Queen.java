package net.chess.chess.piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.chess.chess.board.Tile;

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
	public List<Tile> getTileTraversed(final Tile[][] board, final Tile src, final Tile dest) {
		Objects.requireNonNull(board, "Queen must be on a board");
		Objects.requireNonNull(src, "Source tile cannot be null");
		Objects.requireNonNull(dest, "Destination tile cannot be null");

		List<Tile> temp;
		int dx, dy;
		if (this.rook) {
			dx = dest.file - src.file;
			dy = dest.rank - src.rank;

			final int mx = Math.abs(dx);
			final int my = Math.abs(dy);
			temp = new ArrayList<>();

			for (int i = 0; i < Math.max(mx, my); ++i)
				temp.add(board[src.rank + i * Integer.signum(dy)][src.file + i * Integer.signum(dx)]);

			return temp;
		}
		if (this.bishop) {
			temp = new ArrayList<>();
			dx = dest.file < src.file ? -1 : 1;
			dy = dest.rank < src.rank ? -1 : 1;
			for (int i = 0; i < Math.abs(src.file - dest.file); ++i)
				temp.add(board[src.rank + i * dy][src.file + i * dx]);
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
		Objects.requireNonNull(src, "Source file cannot be null");
		Objects.requireNonNull(dest, "Destination tile cannot be null");
		if (src.equals(dest))
			return false;
		this.rook = src.file == dest.file ^ src.rank == dest.rank;
		this.bishop = Math.abs(src.file - dest.file) == Math.abs(src.rank - dest.rank);
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
