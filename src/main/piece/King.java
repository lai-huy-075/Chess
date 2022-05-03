package main.piece;

import main.board.Tile;

public class King extends Piece {
	public King(PieceColor color) {
		super(color);
	}
	
	@Override
	public int getValue() {
		return 0x7fffffff;
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
			return "\u2654";
		case Black:
			return "\u265A";
		default:
			return default_name;
		}
	}
}
