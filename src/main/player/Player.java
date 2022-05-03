package main.player;

import java.util.Objects;

import main.piece.Bishop;
import main.piece.King;
import main.piece.Knight;
import main.piece.Pawn;
import main.piece.Piece;
import main.piece.Piece.PieceColor;
import main.piece.Queen;
import main.piece.Rook;

public class Player {
	public static final Piece[] black;
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

	public final PieceColor color;
	public final String name;
	private int score;

	public Player(String name, PieceColor color) {
		this.name = Objects.requireNonNull(name);
		this.color = Objects.requireNonNull(color);
		this.score = 0;
	}

	public int getScore() {
		return this.score;
	}
	
	public void incrementScore(int score) {
		this.score += score;
	}

	public void reset() {
		this.score = 0;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return String.format("%-15s:%d", this.name, this.score);
	}
}
