package main.piece;

import java.util.Objects;

import main.board.Tile;

/**
 * The Knight
 */
public class Knight extends Piece {
	public Knight(PieceColor color) {
		super(color);
	}

	@Override
	public Tile[] getTileTraversed(Tile[][] board, Tile src, Tile dest) {
		Objects.requireNonNull(board, "Knight must be on a board");
		Objects.requireNonNull(src, "Source tile cannot be null");
		Objects.requireNonNull(dest, "Destinatino tile cannot be null");

		return empty;
	}

	@Override
	public int getValue() {
		return 3;
	}

	@Override
	public boolean isLegal(Tile src, Tile dest) {
		int dx = Math.abs(src.col - dest.col);
		int dy = Math.abs(src.row - dest.row);

		return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
	}

	@Override
	public void reset() {
	}

	@Override
	public char toAN() {
		return an_knight;
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
}
