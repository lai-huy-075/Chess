package main.board;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

import main.Chess;
import main.piece.Piece;
import main.player.Player;

public class Chessboard {
	public final Player black;

	private final Tile[][] board;
	/**
	 * A reference to the current {@link Player}.
	 */
	private Player currentPlayer;

	private boolean gameover;

	/**
	 * A reference to the next {@link Player}.
	 */
	private Player nextPlayer;

	public final Player white;

	private Tile source;
	private Tile destination;

	public Chessboard(Player white, Player black) {
		this.white = Objects.requireNonNull(white, "White player cannot be null");
		this.black = Objects.requireNonNull(black, "Blackplayer cannot be null");
		this.board = new Tile[8][8];
		this.createBoard();
		this.reset();
	}

	/**
	 * Initialize and add {@link Tile} to the {@link #board}.
	 */
	private void createBoard() {
		for (int row = 0; row < this.board.length; ++row)
			for (int col = 0; col < this.board[row].length; ++col)
				this.board[row][col] = new Tile(row, col);
	}

	public Tile[][] getBoard() {
		return this.board;
	}

	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	public Player getNextPlayer() {
		return this.nextPlayer;
	}

	public final Tile getTileOffset(Tile tile, int x, int y) throws ArrayIndexOutOfBoundsException {
		return this.board[tile.row + y][tile.col + x];
	}

	public boolean isGameOver() {
		return this.gameover;
	}

	/** Place the {@link Piece} on {@link #board} */
	private void placePieces() {
		int indexInList = 0;
		Tile tile;
		Piece piece;

		// Place white pieces
		for (int i = 6; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
				tile = this.board[i][j];
				piece = Player.white[indexInList];
				piece.reset();
				tile.setPiece(piece);
				tile.setText(piece.toString());
				tile.setForeground(piece.color.color);
				++indexInList;
			}
		}

		// Place black pieces
		indexInList = 0;
		for (int i = 1; i > -1; --i) {
			for (int j = 0; j < 8; ++j) {
				tile = this.board[i][j];
				piece = Player.black[indexInList];
				piece.reset();
				tile.setPiece(piece);
				tile.setText(piece.toString());
				tile.setForeground(piece.color.color);
				++indexInList;
			}
		}
	}

	/**
	 * Reset the board
	 */
	public void reset() {
		Chess.logger.entering("Chessboard", "reset()");
		this.resetBoard();
		this.resetTiles();
		this.currentPlayer = this.white;
		this.nextPlayer = this.black;
	}

	private void resetBoard() {
		for (Tile[] row : this.board)
			for (Tile tile : row)
				tile.reset();

		white.reset();
		black.reset();

		this.placePieces();
	}

	public void resetTiles() {
		this.source = null;
		this.destination = null;
	}

	public void setGameOver(boolean bool) {
		this.gameover = bool;
	}

	public void tileClicked(Tile tile) {
		if (this.source == null) {
			this.source = tile;
			return;
		}

		this.destination = tile;

		this.movePiece();
	}

	private void movePiece() {
		if (this.source == null)
			return;

		if (this.destination == null)
			return;

		Piece src_piece = this.source.getPiece();
		if (src_piece == null)
			return;

		Chess.logger.info(String.format("Moving %s from %s to %s", src_piece.toString(), source.toString(),
				destination.toString()));
		boolean legal = src_piece.isLegal(this.source, this.destination);
		Chess.logger.info(legal ? "Move is legal" : "Move is not legal");
		if (!legal) {
			this.resetTiles();
			return;
		}

		this.advancePiece();
	}

	private void advancePiece() {
		Chess.logger.info("Advancing piece");
		Piece piece = this.source.getPiece();
		this.destination.setPiece(piece);
		this.destination.setText(piece.toString());
		this.destination.setForeground(piece.color.color);
		this.source.reset();

		Player temp = this.currentPlayer;
		this.currentPlayer = this.nextPlayer;
		this.nextPlayer = temp;

		this.resetTiles();
	}

	public final void write() {
		String date = Chess.now.format(Chess.format);

		try (FileWriter writer = new FileWriter(Chess.pgn_file)) {
			writer.write("[Event \"1v1\"]\n");
			writer.write("[Site \"Chess.jar\"]\n");
			writer.write("[Date " + date + "]\n");
			writer.write("[Round \"-\"]\n");
			writer.write("[White \"" + this.white.name + "\"]\n");
			writer.write("[Black \"" + this.black.name + "\"]\n");
		} catch (IOException e) {
			return;
		}

		Chess.logger.info("Writting pgn complete");

		try (FileWriter writer = new FileWriter(Chess.fen_file)) {

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
