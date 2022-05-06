package main.piece;

import main.board.Tile;

public class King extends Piece {
	private boolean king;
	private boolean queen;
	private boolean check;
	
	public King(PieceColor color) {
		super(color);
	}
	
	public boolean canKingsideCastle() {
		return this.king;
	}

	public boolean canQueensideCastle() {
		return this.queen;
	}
	
	@Override
	public int getValue() {
		return 0x7fffffff;
	}
	
	public boolean isCheck() {
		return this.check;
	}
	
	public void setCheck(boolean bool) {
		this.check = bool;
	}
	
	@Override
	public boolean isLegal(Tile src, Tile dest) {
		return false;
	}

	@Override
	public void reset() {
		this.king = true;
		this.queen = true;
		this.check = false;
	}

	@Override
	public String toString() {
		switch (this.color) {
		case White:
			return white_king;
		case Black:
			return black_king;
		default:
			return default_name;
		}
	}

	@Override
	public Tile[] getTileTraversed(Tile[][] board, Tile src, Tile dest) {
		return empty;
	}
}
