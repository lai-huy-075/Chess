package net.chess.chess.piece;

import java.util.List;
import java.util.Objects;

import net.chess.chess.board.Tile;

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
	 * Determine if this pawn moved diagonally.
	 */
	private boolean diagonal;

	/**
	 * Can this pawn be captured by en passant
	 */
	private boolean en_passant;

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
	public Pawn(final PieceColor color, final int file) {
		super(color, PieceType.Pawn);
		this.starting_File = file;
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
	public List<Tile> getTileTraversed(final Tile[][] board, final Tile src, final Tile dest) {
		Objects.requireNonNull(board, "Pawn must be on board");
		Objects.requireNonNull(src, "Pawn must have a source");
		Objects.requireNonNull(dest, "Pawn must have a destination");

        return switch (this.color) {
            case Black -> dest.rank - src.rank == 2 ? List.of(src, board[src.rank + 1][src.file]) : empty;
            case White -> src.rank - dest.rank == 2 ? List.of(src, board[src.rank - 1][src.file]) : empty;
        };
	}

	@Override
	public int getValue() {
		return 1;
	}

	@Override
	public boolean isLegal(final Tile src, final Tile dest) {
		Objects.requireNonNull(src, "Source file cannot be null");
		Objects.requireNonNull(dest, "Destination tile cannot be null");
		if (src.equals(dest))
			return false;

		final Piece dest_piece = dest.getPiece();
		final boolean dest_empty = dest_piece == null;

		final boolean diagonal = Math.abs(src.file - dest.file) == 1;
		switch (this.color) {
		case Black:
			// Check if capturing diagonally

			this.diagonal = diagonal;
			if (this.diagonal) {
				if (src.rank - dest.rank == -1) {
					// Normal Capturing
					if (!dest_empty)
						return !this.isAlly(dest_piece);

					// En passant
					final Piece up = dest.getUp().getPiece();
					if (this.isAlly(up))
						return false;
					if (up instanceof Pawn)
						return ((Pawn) up).en_passant();

					return false;
				}
				return false;
			}

			// Moving two squares on the first move
			if (src.rank == 1) {
				if (dest.rank - src.rank == 1 || dest.rank - src.rank == 2) {
					this.en_passant = dest.rank - src.rank == 2;
					return src.file == dest.file && dest_empty;
				}
				return false;
			}

			// Moving one square
			if (src.rank - dest.rank == -1 && src.file == dest.file)
				return dest_empty;

			return false;
		case White:
			// Check if capturing diagonally
			this.diagonal = diagonal;
			if (this.diagonal) {
				if (src.rank - dest.rank == 1) {
					// Normal capturing
					if (!dest_empty)
						return !this.isAlly(dest_piece);

					// En passant
					final Piece down = dest.getDown().getPiece();
					if (this.isAlly(down))
						return false;
					if (down instanceof Pawn)
						return ((Pawn) down).en_passant();

					return false;
				}

				return false;
			}

			// Moving two squares on the first move
			if (src.rank == 6)
				if (src.rank - dest.rank == 1 || src.rank - dest.rank == 2) {
					this.en_passant = src.rank - dest.rank == 2;
					return src.file == dest.file && dest_empty;
				}

			// Moving one square
			if (src.rank - dest.rank == 1 && src.file == dest.file)
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
	 * @param en_passant new {@link #en_passant} value
	 */
	public void setEnPassant(final boolean en_passant) {
		this.en_passant = en_passant;
	}

	@Override
	public String toString() {
		return "Pawn [diagonal=" + this.diagonal + ", en_passant=" + this.en_passant + ", starting_File="
				+ this.starting_File + ", color=" + this.color + ", type=" + this.type + ", tile=" + this.tile + "]";
	}
}
