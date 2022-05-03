package main.piece;

import java.util.Objects;

import main.Chess;
import main.board.Tile;

public class Pawn extends Piece {
	
	public Pawn(PieceColor color) {
		super(color);
	}

	@Override
	public int getValue() {
		return 1;
	}

	@Override
	public boolean isLegal(Tile src, Tile dest) {
		Objects.requireNonNull(src, "Pawn must have an origin");
		Objects.requireNonNull(dest, "Pawn must have a destination.");
		
		Chess.logger.info("Pawn movement");
		Chess.logger.info("From:\t" + src.toString());
		Chess.logger.info("To:\t" + dest.toString());
		
		Piece dest_piece = dest.getPiece();
		boolean dest_empty = dest_piece == null;
		
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
				if (dest.row - src.row <= 2)
					return src.col == dest.col;
				return false;
			}
			
			// Moving one square
			if (dest.row - src.row == 1)
				return !dest_empty;
			
			return false;
		case White:
			// Check if capturing diagonally
			if (src.row - dest.row == 1) {
				if (Math.abs(src.col - dest.col) == 1) {
					if (dest_empty)
						return false;
					return !this.isAlly(dest_piece);
				}
			}
			
			// Moving two squares on the first move
			if (src.row == 6) {
				if (src.row - dest.row <= 2)
					return src.col == dest.col;
			}
			
			return false;
		default:
			throw new IllegalStateException("Illegal PieceColor " + this.color.name());
		}
	}

	@Override
	public void reset() {
	}

	@Override
	public String toString() {
		switch (this.color) {
		case White:
			return "\u2659";
		case Black:
			return "\u265F";
		default:
			return default_name;
		}
	}
}
