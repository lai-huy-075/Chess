package main.piece;

import main.board.Tile;

public class King extends Piece {
	/**
	 * boolean holding if King-side castle is possible
	 */
	private boolean king;
	
	/**
	 * boolean holding if Queen-side castle is possible
	 */
	private boolean queen;
	
	/**
	 * boolean holding if this King is check.
	 */
	private boolean check;
	
	public King(PieceColor color) {
		super(color);
	}
	
	/**
	 * Determine if the King can King-side castle
	 * 
	 * @return true if King-side castle is possible
	 * @return false if King-side castle is not possible
	 */
	public boolean canKingsideCastle() {
		return this.king;
	}
	
	/**
	 * Determine if the King can Queen-side castle
	 * 
	 * @return true if Queen-side castle is possible
	 * @return false if Queen-side castle is not possible
	 */
	public boolean canQueensideCastle() {
		return this.queen;
	}
	
	@Override
	public int getValue() throws IllegalStateException {
		throw new IllegalStateException("King cannot be captured");
	}
	
	/**
	 * Determine if the King is in Check
	 * 
	 * @return true if the King is in Check
	 * @return false if the King is not in Check
	 */
	public boolean isCheck() {
		return this.check;
	}
	
	/**
	 * Set {@link #check}
	 * @param bool 
	 */
	public void setCheck(boolean bool) {
		this.check = bool;
	}
	
	@Override
	public boolean isLegal(Tile src, Tile dest) {
		return Math.abs(src.col - dest.col) <= 1 && Math.abs(src.row - dest.row) <= 1;
	}

	@Override
	public void reset() {
		this.king = true;
		this.queen = true;
		this.check = false;
	}
	
	@Override
	public String toAN() {
		return an_king;
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
	
	public void setKingside(boolean bool) {
		this.king = bool;
	}
	
	public void setQueenside(boolean bool) {
		this.queen = bool;
	}
}
