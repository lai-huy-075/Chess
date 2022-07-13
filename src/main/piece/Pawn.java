package main.piece;

import java.util.Objects;

import main.board.Tile;

/**
 * The Pawn
 */
/**
 * The pawn (&#x2659;, &#x265F;) is the most numerous and weakest piece in the
 * game of chess.<br>
 * It may move one vacant {@link Tile} directly forward, it may move two vacant
 * tiles directly forward on its first move, and it may capture one tile
 * diagonally forward.<br>
 * Each player begins a game with eight pawns, one on each square of the rank
 * immediately in front of the other pieces. (The white pawns start on a2, b2,
 * c2, d2, e2, f2, g2, h2; the black pawns start on a7, b7, c7, d7, e7, f7, g7,
 * h7.)<br>
 * Read more <a href="https://en.wikipedia.org/wiki/Pawn_(chess)">here</a>.
 * 
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public class Pawn extends Piece {
	/**
	 * Can this pawn be captured by en passant
	 */
	private boolean en_passant;

	/**
	 * Determine if this pawn moved diagonally.
	 */
	private boolean diagonal;

	/**
	 * Starting file of this.
	 */
	public final int starting_File;

	/**
	 * Constructor
	 * 
	 * @param color {@link PieceColor} of this
	 * @param file  starting file of this
	 */
	public Pawn(final PieceColor color, int file) {
		super(color);
		this.starting_File = file;
	}

	@Override
	public String debug() {
		return "Pawn [en_passant=" + this.en_passant + ", diagonal=" + this.diagonal + ", starting_File="
				+ this.starting_File + ", color=" + this.color + ", tile=" + this.tile + "]";
	}

	/**
	 * Determine {@link #en_passant}
	 * 
	 * @return {@link #en_passant}
	 */
	public boolean en_passant() {
		return this.en_passant;
	}

	/**
	 * Get {@link #diagonal}
	 * 
	 * @return {@link #diagonal}
	 */
	public boolean getDiagonal() {
		return this.diagonal;
	}

	@Override
	public Tile[] getTileTraversed(final Tile[][] board, final Tile src, final Tile dest) {
		Objects.requireNonNull(board, "Pawn must be on board");
		Objects.requireNonNull(src, "Pawn must have a source");
		Objects.requireNonNull(dest, "Pawn must have a destinaiton");

		switch (this.color) {
		case Black:
			return dest.row - src.row == 2 ? new Tile[] { board[src.row + 1][src.col] } : empty;
		case White:
			return src.row - dest.row == 2 ? new Tile[] { board[src.row - 1][src.col] } : empty;
		default:
			throw new IllegalStateException("Illegal PieceColor:\t" + this.color.name());
		}
	}

	@Override
	public int getValue() {
		return 1;
	}

	@Override
	public boolean isLegal(final Tile src, final Tile dest) {
		Objects.requireNonNull(src, "Pawn must have an origin");
		Objects.requireNonNull(dest, "Pawn must have a destination.");

		final Piece dest_piece = dest.getPiece();
		final boolean dest_empty = dest_piece == null;

		switch (this.color) {
		case Black:
			// Check if capturing diagonally
			if (src.row - dest.row == -1) {
				this.diagonal = Math.abs(src.col - dest.col) == 1;
				if (this.diagonal) {
					// Normal Capturing
					if (!dest_empty)
						return !this.isAlly(dest_piece);

					// En passant
					Piece up = dest.getUp().getPiece();
					if (up instanceof Pawn)
						return ((Pawn) up).en_passant();
				}
			}

			// Moving two squares on the first move
			if (src.row == 1) {
				if (dest.row - src.row <= 2) {
					this.en_passant = dest.row - src.row == 2;
					return src.col == dest.col && dest_empty;
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
				this.diagonal = Math.abs(src.col - dest.col) == 1;
				if (this.diagonal) {
					// Normal capturing
					if (!dest_empty)
						return !this.isAlly(dest_piece);

					// En passant
					Piece down = dest.getDown().getPiece();
					if (down instanceof Pawn)
						return ((Pawn) down).en_passant();
				}
			}

			// Moving two squares on the first move
			if (src.row == 6)
				if (src.row - dest.row <= 2) {
					this.en_passant = src.row - dest.row == 2;
					return src.col == dest.col && dest_empty;
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

	/**
	 * Set {@link #en_passant}
	 * 
	 * @param en_passant
	 */
	public void setEnPassant(boolean en_passant) {
		this.en_passant = en_passant;
	}

	@Override
	public char toAN() {
		return an_pawn;
	}

	@Override
	public char toFEN() {
		switch (this.color) {
		case Black:
			return 'p';
		case White:
			return 'P';
		default:
			return '?';
		}
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
}
