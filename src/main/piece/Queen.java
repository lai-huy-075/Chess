package main.piece;

import main.board.Tile;

public class Queen extends Piece {
	private boolean bishop;
	private boolean rook;

	public Queen(PieceColor color) {
		super(color);
	}

	@Override
	public int getValue() {
		return 9;
	}

	@Override
	public boolean isLegal(Tile src, Tile dest) {
		this.rook = (src.col == dest.col) ^ (src.row == dest.row);
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
		switch (this.color) {
		case White:
			return white_queen;
		case Black:
			return black_queen;
		default:
			return default_name;
		}
	}

	@Override
	public Tile[] getTileTraversed(Tile[][] board, Tile src, Tile dest) {
		if (rook) {
			return empty;
		} else if (bishop) {
			Tile[] temp = new Tile[Math.abs(src.col - dest.col)];
			int dx = dest.col < src.col ? 1 : -1, dy = dest.row < src.row ? 1 : -1;
			for (int i = 0; i < temp.length; ++i)
				temp[i] = board[src.row + i * dy][src.col + i * dx];
			return temp;
		}
		throw new IllegalStateException("Queen has made an illegal move.");
	}
}
