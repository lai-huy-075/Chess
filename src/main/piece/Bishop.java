package main.piece;

import java.util.Objects;

import main.board.Tile;

/**
 * The Bishop
 */
public class Bishop extends Piece {
	/**
	 * Constructor
	 * 
	 * @param color {@link PieceColor} of this
	 */
	public Bishop(PieceColor color) {
		super(color);
	}

	@Override
	public Tile[] getTileTraversed(Tile[][] board, Tile src, Tile dest) {
		Objects.requireNonNull(board, "Bishop must be on a board");
		Objects.requireNonNull(src, "Source tile cannot be null");
		Objects.requireNonNull(dest, "Destinatino tile cannot be null");

		Tile[] temp = new Tile[Math.abs(src.col - dest.col)];
		int dx = dest.col < src.col ? -1 : 1, dy = dest.row < src.row ? -1 : 1;
		for (int i = 0; i < temp.length; ++i)
			temp[i] = board[src.row + i * dy][src.col + i * dx];

		return temp;
	}

	@Override
	public int getValue() {
		return 3;
	}

	@Override
	public boolean isLegal(Tile src, Tile dest) {
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
