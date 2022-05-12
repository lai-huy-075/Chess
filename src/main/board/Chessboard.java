package main.board;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import main.Chess;
import main.piece.King;
import main.piece.Knight;
import main.piece.Piece;
import main.piece.Piece.PieceColor;
import main.player.Player;

public final class Chessboard {
	/**
	 * {@link Player} with the {@link PieceColor#Black} pieces
	 */
	public final Player black;
	
	/**
	 * 2d array holding the location of all the pieces.
	 */
	private final Tile[][] board;
	
	/**
	 * A reference to the current {@link Player}.
	 */
	private Player currentPlayer;

	/**
	 * Destination {@link Tile}
	 */
	private Tile destination;

	/**
	 * Determine if the game is over
	 */
	private Panel.Mode mode;

	/**
	 * A reference to the next {@link Player}.
	 */
	private Player nextPlayer;

	/**
	 * Source {@link Tile}
	 */
	private Tile source;

	/**
	 * {@link Player} with the {@link PieceColor#White}
	 */
	public final Player white;
	
	public Chessboard(Panel.Mode mode) {
		this.mode = Objects.requireNonNull(mode, "Mode cannot be null");
		this.white = Player.default_white;
		this.black = Player.default_black;
		this.board = new Tile[8][8];
		
		this.createBoard();
	}

	public Chessboard(Player white, Player black) {
		this.white = Objects.requireNonNull(white, "White player cannot be null");
		this.black = Objects.requireNonNull(black, "Blackplayer cannot be null");
		this.board = new Tile[8][8];
		this.createBoard();
		this.reset();
	}

	/**
	 * Move the selected {@link Piece} from {@link #source} to {@link #destination}
	 */
	private void advancePiece() {
		Chess.logger.info("Advancing piece");
		Piece piece = this.source.getPiece();

		this.currentPlayer.incrementScore(this.destination.getPiece());

		this.destination.setPiece(piece);
		this.destination.setText(piece.toString());
		this.destination.setForeground(piece.color.color);
		this.source.reset();
	}
	
	private void updatePlayers() {
		Player temp = this.currentPlayer;
		this.currentPlayer = this.nextPlayer;
		this.nextPlayer = temp;
	}
	
	/**
	 * Determine if enemy Knights are able to Check the King.<br>
	 * If an enemy Knight is found
	 * 
	 * @param tile
	 * @throws NullPointerException if inputed Tile is null
	 * @throws NullPointerException if inputed Tile does not have a piece on it
	 */
	private void checkKnights(Tile tile) throws NullPointerException {
		Objects.requireNonNull(tile, "Tile cannot be null");
		King king = (King) tile.getPiece();
		Objects.requireNonNull(king, "King cannot be null");

		for (int x = -2; x < 3; ++x) {
			int y = 0;
			switch (x) {
			case -2:
				y = 1;
				break;
			case -1:
				y = 2;
				break;
			case 1:
				y = 2;
				break;
			case 2:
				y = 1;
				break;
			default:
				continue;
			}

			Tile tile0;
			try {
				tile0 = this.getTileOffset(tile, x, -y);
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				continue;
			}

			Tile tile1;
			try {
				tile1 = this.getTileOffset(tile, x, y);
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				continue;
			}

			for (Tile t : new Tile[] { tile0, tile1 }) {
				if (t == null)
					continue;
								
				Chess.logger.info("Checking tile " + t.toString());
				Piece piece = t.getPiece();
				if (piece != null)
					if (!king.isAlly(piece))
						if (piece instanceof Knight) {
							king.setCheck(true);
							return;
						}
			}
		}
	}
	
	private void checkRooks() {
		
	}

	/**
	 * Determine if a piece collided with any other piece when moving from
	 * {@link #source} to {@link #destination}
	 * 
	 * @param traversed privative array of {@link Tile}
	 * @return true if a piece collides with another. false otherwise.
	 */
	private boolean collide(Tile[] traversed) throws NullPointerException {
		Objects.requireNonNull(traversed, "Traversal cannot be null");
		for (Tile tile : traversed) {
			if (tile.equals(this.source))
				continue;

			if (tile.equals(this.destination))
				continue;

			if (tile.getPiece() != null)
				return true;
		}
		return false;
	}

	/**
	 * Initialize and add {@link Tile} to the {@link #board}.
	 */
	private void createBoard() {
		Chess.logger.info("Creating board");
		for (int row = 0; row < this.board.length; ++row)
			for (int col = 0; col < this.board[row].length; ++col)
				this.board[row][col] = new Tile(row, col);
	}

	/**
	 * Debugs {@link #source} and {@link #destination} to {@link Chess#logger}
	 */
	public void debugTiles() {
		Chess.logger.info("Source:\t" + (this.source == null ? "null" : this.source.toString()));
		Chess.logger.info("Dest:\t" + (this.destination == null ? "null" : this.destination.toString()));
	}

	/**
	 * Finds the {@link King} on this board.
	 * 
	 * @param isAlly true if finding {@link #currentPlayer} King false if finding
	 *               {@link #nextPlayer} King
	 * @return {@link Tile} King is currently on.
	 */
	private Tile findKing(boolean isAlly) throws IllegalStateException {
		Chess.logger.info(isAlly ? "Searching for ally King" : "Searching for enemy King");
		for (Tile[] row : this.board)
			for (Tile tile : row) {
				Piece piece = tile.getPiece();
				if (piece == null)
					continue;
				if (!(piece instanceof King))
					continue;
				if (isAlly) {
					if (piece.color == this.currentPlayer.color) {
						Chess.logger.info(String.format("Found %s King on %s", this.currentPlayer.color.name(), tile.toString()));
						return tile;
					}
				} else if (piece.color == this.nextPlayer.color) {
					Chess.logger.info(String.format("Found %s King on %s", this.nextPlayer.color.name(), tile.toString()));
					return tile;
				}
			}
		throw new IllegalStateException("Cannot find King");
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
		switch (this.mode) {
		case Over:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Determine if the piece moved out of a pin.
	 * 
	 * @return
	 */
	private boolean moveOutOfPin() {
		return false;
	}

	/**
	 * Handling logic of moving a piece.<br>
	 * Actual updating is done in {@link #advancePiece()}
	 */
	private void movePiece() {
		if (this.source == null)
			return;

		if (this.destination == null)
			return;

		Piece src_piece = this.source.getPiece();
		if (src_piece == null)
			return;

		Chess.logger.info(String.format("Moving %s from %s to %s", src_piece.toString(), this.source.toString(),
				this.destination.toString()));

		if (this.source.equals(this.destination)) {
			Chess.logger.info("No movement detected.");
			return;
		}

		boolean legal = src_piece.isLegal(this.source, this.destination);
		Chess.logger.info(legal ? "Move is legal" : "Move is not legal");
		if (!legal)
			return;

		Tile[] traversed = src_piece.getTileTraversed(this.board, this.source, this.destination);
		Chess.logger.info("Traversed:\t" + Arrays.deepToString(traversed));
		boolean collide = this.collide(traversed);
		Chess.logger.info(
				src_piece.toString() + (collide ? " collided on its journey" : " did not collide on its journey"));
		if (collide)
			return;
		
		
		boolean unpin = this.moveOutOfPin();
		Chess.logger.info(unpin ? "Move out of pin" : "Did not move out of pin");
		if (unpin)
			return;
		

		this.advancePiece();
		this.updateKing();
		this.updatePlayers();
	}

	/**
	 * Place the {@link Piece} on {@link #board}
	 */
	private void placePieces() {
		Chess.logger.info("Placing pieces on the Chessboard");

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
	 * Reset the board and all attributes.
	 */
	public void reset() {
		Chess.logger.info("Reset Chessboard");
		this.resetBoard();
		this.resetTiles();
		this.placePieces();
		this.white.reset();
		this.black.reset();
		this.currentPlayer = this.white;
		this.nextPlayer = this.black;
	}

	/**
	 * Reset {@link #board}
	 */
	private void resetBoard() {
		Chess.logger.info("Reset board array");
		for (Tile[] row : this.board)
			for (Tile tile : row)
				tile.reset();
	}

	/**
	 * Set {@link #source} and {@link #destination} to null
	 */
	public void resetTiles() {
		Chess.logger.info("Reseting Tiles");
		this.source = null;
		this.destination = null;
	}
	
	public void setMode(Panel.Mode mode) {
		this.mode = Objects.requireNonNull(mode, "New mode cannot be null");
	}

	/**
	 * This method is called whenever a {@link Tile} is clicked.
	 * 
	 * @param tile Tile clicked
	 */
	public void tileClicked(Tile tile) {
		Piece tile_piece = tile.getPiece();
		boolean moving_ally = this.currentPlayer.movingAlly(tile);

		if (this.source == null) {
			if (tile_piece == null) {
				Chess.logger.info("Not moving a piece");
				return;
			}

			if (!moving_ally) {
				Chess.logger.info("Moving enemy piece");
				return;
			}

			this.source = tile;
			return;
		}

		if (moving_ally) {
			this.source = tile;
			return;
		}

		this.destination = tile;

		this.movePiece();
		this.resetTiles();
	}

	private void updateKing() {
		Chess.logger.info("Updating King");
		
		Tile king_tile = this.findKing(false);
		this.checkKnights(king_tile);
		this.checkRooks();
	}

	/**
	 * Write {@link Chess#pgn_file} with details of the game
	 */
	public final void write() {
		Chess.logger.info("Writting pgn started...");
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

		Chess.logger.info("Writting pgn complete!");
	}
}
