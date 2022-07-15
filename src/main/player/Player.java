package main.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import main.Chess;
import main.board.Tile;
import main.piece.Bishop;
import main.piece.King;
import main.piece.Knight;
import main.piece.Pawn;
import main.piece.Piece;
import main.piece.PieceColor;
import main.piece.Queen;
import main.piece.Rook;

/**
 * Player class
 * 
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public class Player {
	/**
	 * Default {@link PieceColor#Black} Player
	 */
	public static final Player default_black = new Player("Black", PieceColor.Black);

	/**
	 * Default {@link PieceColor#White} Player
	 */
	public static final Player default_white = new Player("White", PieceColor.White);

	/**
	 * {@link PieceColor} that this player controls
	 */
	public final PieceColor color;

	/**
	 * {@link String} holding the name of this player
	 */
	public final String name;

	/**
	 * A primitive type array of {@link Piece}
	 */
	public final Piece[] pieces;

	/**
	 * Current score
	 */
	private int score;

	/**
	 * Constructor
	 * 
	 * @param name  {@link String} name of this
	 * @param color {@link PieceColor} this will control
	 */
	public Player(final String name, final PieceColor color) {
		this.name = Objects.requireNonNull(name);
		this.color = Objects.requireNonNull(color);
		this.pieces = new Piece[16];
		this.createPieces(false);
		this.reset();
	}

	/**
	 * Initializes {@link #pieces} with {@link Pawn}
	 */
	private void createPawns() {
		for (int i = 0; i < 8; ++i)
			this.pieces[i] = new Pawn(this.color, i);
	}

	/**
	 * Initialize {@link #pieces}
	 */
	private void createPieces() {
		this.pieces[8] = new Rook(this.color);
		this.pieces[9] = new Knight(this.color);
		this.pieces[10] = new Bishop(this.color);
		this.pieces[11] = new Queen(this.color);
		this.pieces[12] = new King(this.color);
		this.pieces[13] = new Bishop(this.color);
		this.pieces[14] = new Knight(this.color);
		this.pieces[15] = new Rook(this.color);
	}

	/**
	 * Initializes {@link #pieces} but under Chess960 rules if parameter is true.
	 * 
	 * @param random true if random, false if standard
	 */
	private void createPieces(final boolean random) {
		this.createPawns();
		if (random) {
			this.createRandom();
		} else
			this.createPieces();
	}

	/**
	 * Initialize {@link #pieces} under Chess960 rules.
	 */
	private void createRandom() {
		this.createPieces();
	}

	/**
	 * Log attributes using {@link Chess#logger}
	 * 
	 * @return debug {@link String}
	 */
	public String debug() {
		String str = "Player [color=" + this.color + ", name=" + this.name + ", pieces="
				+ Arrays.deepToString(this.pieces) + ", score=" + this.score + "]\n";
		for (final Piece piece : this.pieces)
			str += piece.toString() + "\n";

		return str + "\n";
	}

	/**
	 * Get this {@link Player}'s {@link King}.
	 * 
	 * @return {@link King}
	 */
	public King getKing() {
		for (Piece piece : this.pieces)
			if (piece instanceof King)
				return (King) piece;
		throw new IllegalStateException("Player does not have a King");
	}

	/**
	 * Get this Player's {@link Pawn}s
	 * 
	 * @return primitive type array of {@link Pawn}
	 */
	public Pawn[] getPawn() {
		List<Pawn> pawn = new ArrayList<>();
		for (Piece piece : this.pieces)
			if (piece instanceof Pawn)
				pawn.add((Pawn) piece);
		return pawn.toArray(new Pawn[pawn.size()]);
	}

	/**
	 * Get {@link #score}
	 * 
	 * @return {@link #score}
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * Increment {@link #score} by {@link Piece#getValue()}
	 * 
	 * @param piece {@link Piece} to increment score with
	 */
	public void incrementScore(final Piece piece) {
		if (piece == null)
			return;
		Chess.logger.info("Capturing " + piece.toFigure());
		this.score += piece.getValue();
	}

	/**
	 * Determine if this is moving an ally {@link Piece} on a {@link Tile}
	 * 
	 * @param tile {@link Tile} this is attempting to interact with
	 * @return true if this can move the piece on the tile false otherwise
	 */
	public boolean movingAlly(final Tile tile) {
		try {
			return this.color == tile.getPiece().color;
		} catch (final NullPointerException npe) {
			return false;
		}
	}

	/**
	 * Reset Player's attributes back to their default values
	 */
	public void reset() {
		Chess.logger.info("Reseting " + this.name);
		this.score = 0;
	}

	@Override
	public String toString() {
		return String.format("%-15s:%d", this.name, this.score);
	}
}
