package main.piece;

import main.board.Tile;

public class Queen extends Piece {
	private final Bishop bishop;
	private final Rook rook;
	
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
			return white_queen;
		case Black:
			return black_queen;
		default:
			return default_name;
		}
	}

	@Override
	public Tile[] getTileTraversed(Tile[][] board, Tile src, Tile dest) {
		return null;
	}
}
