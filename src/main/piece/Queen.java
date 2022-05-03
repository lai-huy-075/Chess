package main.piece;

import main.board.Tile;

public class Queen extends Piece {
	private final Rook rook;
	private final Bishop bishop;
	
	public Queen(PieceColor color) {
		super(color);
		this.rook = new Rook(this.color);
		this.bishop = new Bishop(this.color);
	}
	
	@Override
	public int getValue() {
		return 9;
	}

	@Override
	public boolean isLegal(Tile src, Tile dest) {
		return this.rook.isLegal(src, dest) || this.bishop.isLegal(src, dest);
	}

	@Override
	public void reset() {
	}

	@Override
	public String toString() {
		switch (this.color) {
		case White:
			return "\u2655";
		case Black:
			return "\u265B";
		default:
			return default_name;
		}
	}
}
