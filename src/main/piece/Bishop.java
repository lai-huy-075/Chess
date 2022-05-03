package main.piece;

import main.board.Tile;

public class Bishop extends Piece {
	public Bishop(PieceColor color) {
		super(color);
	}
	
	@Override
	public int getValue() {
		return 0;
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
			return "\u2657";
		case Black:
			return "\u265D";
		default:
			return default_name;
		}
	}
}
