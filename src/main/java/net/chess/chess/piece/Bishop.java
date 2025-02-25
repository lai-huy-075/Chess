package net.chess.chess.piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.chess.chess.board.Tile;

/**
 * The bishop (&#x2657;, &#x265D;) is a piece in the game of chess. <br>
 * It moves and captures along diagonals without jumping over intervening
 * pieces.<br>
 * Each {@link net.chess.chess.player.Player} begins the game with two bishops.<br>
 * One starts between the {@link King}'s {@link Knight} and the {@link King},
 * the other between the {@link Queen}'s {@link Knight} and the
 * {@link Queen}.<br>
 * The starting squares are c1 and f1 for {@link PieceColor#White}'s bishops,
 * and c8 and f8 for {@link PieceColor#Black}'s bishops.<br>
 * Read more <a href="https://en.wikipedia.org/wiki/Bishop_(chess)">here</a>.
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public class Bishop extends Piece {
	/**
	 * Constructor
	 *
	 * @param color {@link PieceColor} of this
	 */
	public Bishop(final PieceColor color) {
		super(color, PieceType.Bishop);
	}

	@Override
	public List<Tile> getTileTraversed(final Tile[][] board, final Tile src, final Tile dest) {
		Objects.requireNonNull(board, "Bishop must be on a board");
		Objects.requireNonNull(src, "Source tile cannot be null");
		Objects.requireNonNull(dest, "Destination tile cannot be null");

		final List<Tile> temp = new ArrayList<>();
		final int dx = dest.file < src.file ? -1 : 1, dy = dest.rank < src.rank ? -1 : 1;
		for (int i = 0; i < Math.abs(src.file - dest.file); ++i)
			temp.add(board[src.rank + i * dy][src.file + i * dx]);

		return temp;
	}

	@Override
	public int getValue() {
		return 3;
	}

	@Override
	public boolean isLegal(final Tile source, final Tile destination) {
		Objects.requireNonNull(source, "Source file cannot be null");
		Objects.requireNonNull(destination, "Destination tile cannot be null");
		if (source.equals(destination))
			return false;
		return Math.abs(source.file - destination.file) == Math.abs(source.rank - destination.rank);
	}

	@Override
	public void reset() {
	}

	@Override
	public String toString() {
		return "Bishop [color=" + this.color + ", type=" + this.type + ", tile=" + this.tile + "]";
	}
}
