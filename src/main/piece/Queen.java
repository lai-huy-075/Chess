package main.piece;

import java.util.Objects;

import main.board.Tile;

/**
 * The Queen
 */
public class Queen extends Piece {
	/**
	 * Determine if this moves like a {@link Bishop}
	 */
	private boolean bishop;

	/**
	 * Determine if this moves like a {@link Rook}
	 */
	private boolean rook;

	/**
	 * Constructor
	 * 
	 * @param color {@link PieceColor} of this
	 */
	public Queen(PieceColor color) {
		super(color);
	}

	@Override
	public Tile[] getTileTraversed(Tile[][] board, Tile src, Tile dest) {
		Objects.requireNonNull(board, "Queen must be on a board");
		Objects.requireNonNull(src, "Source tile cannot be null");
		Objects.requireNonNull(dest, "Destination tile cannot be null");

		Tile[] temp;
		int dx, dy;
		if (rook) {
			dx = dest.col - src.col;
			dy = dest.row - src.row;

			int mx = Math.abs(dx);
			int my = Math.abs(dy);
			temp = new Tile[Math.max(mx, my)];

			for (int i = 0; i < temp.length; ++i)
				temp[i] = board[src.row + i * Integer.signum(dy)][src.col + i * Integer.signum(dx)];

			return temp;
		} else if (bishop) {
			temp = new Tile[Math.abs(src.col - dest.col)];
			dx = dest.col < src.col ? -1 : 1;
			dy = dest.row < src.row ? -1 : 1;
			for (int i = 0; i < temp.length; ++i)
				temp[i] = board[src.row + i * dy][src.col + i * dx];
			return temp;
		}
		throw new IllegalStateException("Queen has made an illegal move.");
	}

	@Override
	public int getValue() {
		return 9;
	}

	@Override
	public boolean isLegal(Tile src, Tile dest) {
		this.rook = (src.col == dest.col) ^ (src.row == dest.row);
		this.bishop = Math.abs(src.col - dest.col) == Math.abs(src.row - dest.row);
		return this.rook ^ this.bishop;
	}

	@Override
	public void reset() {
		this.rook = false;
		this.bishop = false;
	}

	@Override
	public char toAN() {
		return an_queen;
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
}
