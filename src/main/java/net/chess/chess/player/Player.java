package net.chess.chess.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.chess.chess.ChessApplication;
import net.chess.chess.board.Tile;
import net.chess.chess.piece.Bishop;
import net.chess.chess.piece.King;
import net.chess.chess.piece.Knight;
import net.chess.chess.piece.Pawn;
import net.chess.chess.piece.Piece;
import net.chess.chess.piece.PieceColor;
import net.chess.chess.piece.Queen;
import net.chess.chess.piece.Rook;

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
		if (random)
			this.createRandom();
		else
			this.createPieces();
	}

	/**
	 * Initialize {@link #pieces} under Chess960 rules.
	 */
	private void createRandom() {
		this.createPieces();
	}

	/**
	 * Log attributes using {@link net.chess.chess.ChessApplication#logger}
	 *
	 * @return debug {@link String}
	 */
	public String debug() {
		StringBuilder str = new StringBuilder("Player [color=" + this.color + ", name=" + this.name + ", score=" + this.score + "]\n");
		for (final Piece piece : this.pieces)
			str.append(piece.toString()).append("\n");

		return str + "\n";
	}

	/**
	 * Get the Player's {@link Bishop}
	 *
	 * @return primitive type array of {@link Bishop}
	 */
	public Bishop[] getBishop() {
		final List<Bishop> bishop = new ArrayList<>();
		for (final Piece piece : this.pieces)
			if (piece instanceof Bishop)
				bishop.add((Bishop) piece);
		return bishop.toArray(new Bishop[0]);
	}

	/**
	 * Get this {@link Player}'s {@link King}.
	 *
	 * @return {@link King}
	 */
	public King getKing() {
		for (final Piece piece : this.pieces)
			if (piece instanceof King)
				return (King) piece;
		throw new IllegalStateException("Player does not have a King");
	}

	/**
	 * Get this Player's {@link Knight}
	 *
	 * @return primitive type array of {@link Pawn}
	 */
	public Knight[] getKnight() {
		final List<Knight> knight = new ArrayList<>();
		for (final Piece piece : this.pieces)
			if (piece instanceof Knight)
				knight.add((Knight) piece);
		return knight.toArray(new Knight[0]);
	}

	/**
	 * Get this Player's {@link Pawn}s
	 *
	 * @return primitive type array of {@link Pawn}
	 */
	public Pawn[] getPawn() {
		final List<Pawn> pawn = new ArrayList<>();
		for (final Piece piece : this.pieces)
			if (piece instanceof Pawn)
				pawn.add((Pawn) piece);
		return pawn.toArray(new Pawn[0]);
	}

	/**
	 * Get this Player's {@link Queen}
	 *
	 * @return primitive type array of {@link Queen}
	 */
	public Queen[] getQueen() {
		final List<Queen> queen = new ArrayList<>();
		for (final Piece piece : this.pieces)
			if (piece instanceof Queen)
				queen.add((Queen) piece);
		return queen.toArray(new Queen[0]);
	}

	/**
	 * Get this Player's {@link Rook}
	 *
	 * @return primitive type array of {@link Rook}
	 */
	public Rook[] getRook() {
		final List<Rook> rook = new ArrayList<>();
		for (final Piece piece : this.pieces)
			if (piece instanceof Rook)
				rook.add((Rook) piece);
		return rook.toArray(new Rook[0]);
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
		ChessApplication.logger.info("Capturing " + piece.toFigure());
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
		ChessApplication.logger.info("Resetting " + this.name);
		this.score = 0;
	}

	@Override
	public String toString() {
		return String.format("%-15s:%d", this.name, this.score);
	}
}
