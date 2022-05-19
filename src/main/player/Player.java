package main.player;

import java.util.Objects;

import main.Chess;
import main.board.Tile;
import main.piece.Bishop;
import main.piece.King;
import main.piece.Knight;
import main.piece.Pawn;
import main.piece.Piece;
import main.piece.Piece.PieceColor;
import main.piece.Queen;
import main.piece.Rook;

/**
 * Player
 */
public class Player {
	/**
	 * Primitive type array of {@link Piece} holding {@link PieceColor#Black} Pieces
	 */
	public static final Piece[] black;

	/**
	 * Default {@link PieceColor#Black} Player
	 */
	public static final Player default_black = new Player("Black", PieceColor.Black);

	/**
	 * Default {@link PieceColor#White} Player
	 */
	public static final Player default_white = new Player("White", PieceColor.White);

	/**
	 * Primitive type array of {@link Piece} holding {@link PieceColor#White} Pieces
	 */
	public static final Piece[] white;

	static {
		white = new Piece[16];
		for (int i = 0; i < 8; i++)
			white[i] = new Pawn(PieceColor.White);

		// Add other pieces
		white[8] = new Rook(PieceColor.White);
		white[9] = new Knight(PieceColor.White);
		white[10] = new Bishop(PieceColor.White);
		white[11] = new Queen(PieceColor.White);
		white[12] = new King(PieceColor.White);
		white[13] = new Bishop(PieceColor.White);
		white[14] = new Knight(PieceColor.White);
		white[15] = new Rook(PieceColor.White);

		black = new Piece[16];
		for (int i = 0; i < 8; i++)
			black[i] = new Pawn(PieceColor.Black);

		// Add other pieces
		black[8] = new Rook(PieceColor.Black);
		black[9] = new Knight(PieceColor.Black);
		black[10] = new Bishop(PieceColor.Black);
		black[11] = new Queen(PieceColor.Black);
		black[12] = new King(PieceColor.Black);
		black[13] = new Bishop(PieceColor.Black);
		black[14] = new Knight(PieceColor.Black);
		black[15] = new Rook(PieceColor.Black);
	}

	/**
	 * {@link PieceColor} that this player controls
	 */
	public final PieceColor color;

	/**
	 * {@link String} holding the name of this player
	 */
	public final String name;

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
	public Player(String name, PieceColor color) {
		this.name = Objects.requireNonNull(name);
		this.color = Objects.requireNonNull(color);
		this.score = 0;
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
	public void incrementScore(Piece piece) {
		if (piece == null)
			return;
		Chess.logger.info("Capturing " + piece.toString());
		this.score += piece.getValue();
	}

	/**
	 * Determine if this is moving an ally {@link Piece} on a {@link Tile}
	 * 
	 * @param tile {@link Tile} this is attempting to interact with
	 * @return true if this can move the piece on the tile false otherwise
	 */
	public boolean movingAlly(Tile tile) {
		try {
			return this.color == tile.getPiece().color;
		} catch (NullPointerException npe) {
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
