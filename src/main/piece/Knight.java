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
		return false;
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
