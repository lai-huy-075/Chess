package net.chess.chess.piece;

import java.util.List;
import java.util.Objects;

import net.chess.chess.board.Tile;
import net.chess.chess.player.Player;

/**
 * The knight (&#x2658;, &#x265E;) is a piece in the game of chess, represented
 * by a horse's head and neck.<br>
 * It may move two {@link Tile}s vertically and one tile horizontally or two
 * tiles horizontally and one tile vertically.<br>
 * Each {@link Player} starts the game with two knights on the b- and g-files,
 * each located between a {@link Rook} and a {@link Bishop}.<br>
 * Read more <a href="https://en.wikipedia.org/wiki/Knight_(chess)">here</a>.
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public class Knight extends Piece {
	/**
	 * Constructor
	 *
	 * @param color {@link PieceColor} of this
	 */
	public Knight(final PieceColor color) {
		super(color, PieceType.Knight);
	}

	@Override
	public List<Tile> getTileTraversed(final Tile[][] board, final Tile src, final Tile dest) {
		Objects.requireNonNull(board, "Knight must be on a board");
		Objects.requireNonNull(src, "Source tile cannot be null");
		Objects.requireNonNull(dest, "Destination tile cannot be null");

		return empty;
	}

	@Override
	public int getValue() {
		return 3;
	}

	@Override
	public boolean isLegal(final Tile src, final Tile dest) {
		Objects.requireNonNull(src, "Source file cannot be null");
		Objects.requireNonNull(dest, "Destination tile cannot be null");
		if (src.equals(dest))
			return false;
		final int dx = Math.abs(src.file - dest.file);
		final int dy = Math.abs(src.rank - dest.rank);
		return dx == 2 && dy == 1 || dx == 1 && dy == 2;
	}

	@Override
	public void reset() {
	}

	@Override
	public String toString() {
		return "Knight [color=" + this.color + ", type=" + this.type + ", tile=" + this.tile + "]";
	}
}
