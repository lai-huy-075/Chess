package main.piece;

import java.util.Objects;

import main.board.Tile;
import main.player.Player;

/**
 * The bishop (&#x2657;, &#x265D;) is a piece in the game of chess. <br>
 * It moves and captures along diagonals without jumping over intervening
 * pieces.<br>
 * Each {@link Player} begins the game with two bishops.<br>
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
		super(color);
	}

	@Override
	public String debug() {
		return "Bishop [color=" + this.color + ", tile=" + this.tile + "]";
	}

	@Override
	public Tile[] getTileTraversed(final Tile[][] board, final Tile src, final Tile dest) {
		Objects.requireNonNull(board, "Bishop must be on a board");
		Objects.requireNonNull(src, "Source tile cannot be null");
		Objects.requireNonNull(dest, "Destinatino tile cannot be null");

		final Tile[] temp = new Tile[Math.abs(src.col - dest.col)];
		final int dx = dest.col < src.col ? -1 : 1, dy = dest.row < src.row ? -1 : 1;
		for (int i = 0; i < temp.length; ++i)
			temp[i] = board[src.row + i * dy][src.col + i * dx];

		return temp;
	}

	@Override
	public int getValue() {
		return 3;
	}

	@Override
	public boolean isLegal(final Tile src, final Tile dest) {
		return Math.abs(src.col - dest.col) == Math.abs(src.row - dest.row);
	}

	@Override
	public void reset() {
	}

	@Override
	public char toAN() {
		return an_bishop;
	}

	@Override
	public char toFEN() {
		switch (this.color) {
		case Black:
			return 'b';
		case White:
			return 'B';
		default:
			return '?';
		}
	}

	@Override
	public String toString() {
		switch (this.color) {
		case White:
			return white_bishop;
		case Black:
			return black_bishop;
		default:
			return default_name;
		}
	}
}
