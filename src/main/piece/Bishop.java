package main.piece;

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
	
	@Override
	public String toAN() {
		return an_bishop;
	}

	@Override
	public Tile[] getTileTraversed(Tile[][] board, Tile src, Tile dest) {
		Tile[] temp = new Tile[Math.abs(src.col - dest.col)];
		int dx = dest.col < src.col ? -1 : 1, dy = dest.row < src.row ? -1 : 1;
		for (int i = 0; i < temp.length; ++i)
			temp[i] = board[src.row + i * dy][src.col + i * dx];

		return temp;
	}
}
