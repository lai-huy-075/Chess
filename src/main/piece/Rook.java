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
		// TODO Auto-generated method stub
		return false;
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
		return null;
	}
}
