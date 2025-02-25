package net.chess.chess.piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.chess.chess.board.Tile;

/**
 * The rook (&#x2656;, &#x265C;) is a piece in the game of chess.<br>
 * It may move any number of squares horizontally or vertically without jumping,
 * and it may capture an enemy piece on its path; additionally, it may
 * participate in castling.<br>
 * Each player starts the game with two rooks, one in each corner on their own
 * side of the board.<br>
 * Read more <a href="https://en.wikipedia.org/wiki/Rook_(chess)">here</a>
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public class Rook extends Piece {
	/**
	 * Constructor
	 *
	 * @param color {@link PieceColor} of this
	 */
	public Rook(final PieceColor color) {
		super(color, PieceType.Rook);
	}

	@Override
	public List<Tile> getTileTraversed(final Tile[][] board, final Tile src, final Tile dest) {
		Objects.requireNonNull(board, "Rook must be on a board");
		Objects.requireNonNull(src, "Source tile cannot be null");
		Objects.requireNonNull(dest, "Destination tile cannot be null");

		final int dx = dest.file - src.file;
		final int dy = dest.rank - src.rank;

		final int mx = Math.abs(dx);
		final int my = Math.abs(dy);
		final List<Tile> temp = new ArrayList<>();

		for (int i = 0; i < Math.max(mx, my); ++i)
			temp.add(board[src.rank + i * Integer.signum(dy)][src.file + i * Integer.signum(dx)]);

		return temp;
	}

	@Override
	public int getValue() {
		return 5;
	}

	@Override
	public boolean isLegal(final Tile src, final Tile dest) {
		Objects.requireNonNull(src, "Source file cannot be null");
		Objects.requireNonNull(dest, "Destination tile cannot be null");
		if (src.equals(dest))
			return false;
		return src.file == dest.file ^ src.rank == dest.rank;
	}

	@Override
	public void reset() {
	}

	@Override
	public String toString() {
		return "Rook [color=" + this.color + ", type=" + this.type + ", tile=" + this.tile + "]";
	}
}
