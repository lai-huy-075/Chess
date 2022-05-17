package main.piece;

import java.util.Objects;

import main.Chess;
import main.board.Tile;

/**
 * The Rook
 */
public class Rook extends Piece {
	/**
	 * Constructor
	 * 
	 * @param color {@link PieceColor} of this
	 */
	public Rook(PieceColor color) {
		super(color);
	}
	
	@Override
	public void debug() {
		Chess.logger.info(String.format("%s, %s", this.toString(), this.color.name()));
	}

	@Override
	public Tile[] getTileTraversed(Tile[][] board, Tile src, Tile dest) {
		Objects.requireNonNull(board, "Rook must be on a board");
		Objects.requireNonNull(src, "Source tile cannot be null");
		Objects.requireNonNull(dest, "Destination tile cannot be null");

		int dx = dest.col - src.col;
		int dy = dest.row - src.row;

		int mx = Math.abs(dx);
		int my = Math.abs(dy);
		Tile[] temp = new Tile[Math.max(mx, my)];

		for (int i = 0; i < temp.length; ++i)
			temp[i] = board[src.row + i * Integer.signum(dy)][src.col + i * Integer.signum(dx)];

		return temp;
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
	public char toAN() {
		return an_rook;
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
}
