package main.piece;

import main.board.Tile;

public class Knight extends Piece {
	public Knight(PieceColor color) {
		super(color);
	}
	
	@Override
	public int getValue() {
		return 3;
	}

	@Override
	public boolean isLegal(Tile src, Tile dest) {
		int dx = Math.abs(src.col - dest.col);
		int dy = Math.abs(src.row - dest.row);
		
		return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
	}

	@Override
	public void reset() {
	}

	@Override
	public String toString() {
		switch (this.color) {
		case White:
			return white_knight;
		case Black:
			return black_knight;
		default:
			return default_name;
		}
	}

	@Override
	public Tile[] getTileTraversed(Tile[][] board, Tile src, Tile dest) {
		return empty;
	}
}
