package main.piece;

import main.board.Tile;

public class Rook extends Piece {
	public Rook(PieceColor color) {
		super(color);
	}

	@Override
	public int getValue() {
		return 5;
	}

	@Override
	public boolean isLegal(Tile src, Tile dest) {
		return (src.col == dest.col) ^ (src.row == dest.row);
	}

	@Override
	public void reset() {
		
	}

	@Override
	public String toString() {
		switch (this.color) {
		case White:
			return white_rook;
		case Black:
			return black_rook;
		default:
			return default_name;
		}
	}

	@Override
	public Tile[] getTileTraversed(Tile[][] board, Tile src, Tile dest) {
		int dx = dest.col - src.col;
		int dy = dest.row - src.row;
		
		int mx = Math.abs(dx);
		int my = Math.abs(dy);
		Tile[] temp = new Tile[Math.max(mx, my)];
		
		for (int i = 0; i < temp.length; ++i)
			temp[i] = board[src.row + i * Integer.signum(dy)][src.col + i * Integer.signum(dx)];
		
		return temp;
	}
}
