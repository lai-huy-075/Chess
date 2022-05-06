package main.piece;

import java.util.Objects;

import main.Chess;
import main.board.Tile;

public class Pawn extends Piece {
	private boolean en_passant;
	
	public Pawn(PieceColor color) {
		super(color);
	}

	public boolean en_passant() {
		return this.en_passant;
	}

	@Override
	public int getValue() {
		return 1;
	}

	@Override
	public boolean isLegal(Tile src, Tile dest) {
		Objects.requireNonNull(src, "Pawn must have an origin");
		Objects.requireNonNull(dest, "Pawn must have a destination.");
		Chess.logger.info("Determine if Pawn move is legal");

		Piece dest_piece = dest.getPiece();
		boolean dest_empty = dest_piece == null;
		
		Chess.logger.info(dest_empty ? "Destination Tile is empty" : "Destination Tile is not empty");

		switch (this.color) {
		case Black:
			// Check if capturing diagonally
			if (src.row - dest.row == -1) {
				if (Math.abs(src.col - dest.col) == 1) {
					if (dest_empty)
						return false;
					return !this.isAlly(dest_piece);
				}
			}

			// Moving two squares on the first move
			if (src.row == 1) {
				if (dest.row - src.row <= 2) {
					this.en_passant = dest.row - src.row == 2;
					return src.col == dest.col;
				}
				return false;
			}

			// Moving one square
			if (dest.row - src.row == 1)
				return dest_empty;

			return false;
		case White:
			// Check if capturing diagonally
			if (src.row - dest.row == 1) {
				if (Math.abs(src.col - dest.col) == 1) {
					// En passant
					
					
					// Normal capturing					
					if (dest_empty)
						return false;
					return !this.isAlly(dest_piece);				
				}
			}

			// Moving two squares on the first move
			if (src.row == 6) {
				if (src.row - dest.row <= 2) {
					this.en_passant = src.row - dest.row == 2;
					return src.col == dest.col;
				}
			}

			// Moving one square
			if (src.row - dest.row == 1)
				return dest_empty;

			return false;
		default:
			throw new IllegalStateException("Illegal PieceColor " + this.color.name());
		}
	}

	@Override
	public void reset() {
		this.en_passant = false;
	}
	
	@Override
	public String toString() {
		switch (this.color) {
		case White:
			return white_pawn;
		case Black:
			return black_pawn;
		default:
			return default_name;
		}
	}

	@Override
	public Tile[] getTileTraversed(Tile[][] board, Tile src, Tile dest) {
		Objects.requireNonNull(board, "Pawn must be on board");
		Objects.requireNonNull(src, "Pawn must have a source");
		Objects.requireNonNull(dest, "Pawn must have a destinaiton");
		
		return empty;
	}
}
