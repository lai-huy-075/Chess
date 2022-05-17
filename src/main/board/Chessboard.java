package main.board;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import main.Chess;
import main.board.Panel.Mode;
import main.piece.King;
import main.piece.King.CastleState;
import main.piece.Knight;
import main.piece.Piece;
import main.piece.Piece.PieceColor;
import main.piece.Rook;
import main.player.Player;

/**
 * Chess board
 */
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
	 * Move {@link List}
	 */
	private final List<String> moves;

	/**
	 * A reference to the next {@link Player}.
	 */
	private Player nextPlayer;

	/**
	 * {@link String} holding the result.
	 */
	private String result;

	/**
	 * Source {@link Tile}
	 */
	private Tile source;

	/**
	 * {@link Player} with the {@link PieceColor#White}
	 */
	public final Player white;

	/**
	 * Constructor
	 * 
	 * @param mode {@link Mode} of this
	 */
	public Chessboard(Panel.Mode mode) {
		this.mode = Objects.requireNonNull(mode, "Mode cannot be null");
		this.white = Player.default_white;
		this.black = Player.default_black;
		this.board = new Tile[8][8];
		this.moves = new ArrayList<>();

		this.createBoard();
	}

	/**
	 * Constructor
	 * 
	 * @param white {@link Player} controlling {@link PieceColor#White}
	 * @param black {@link Player} controlling {@link PieceColor#Black}
	 */
	public Chessboard(Player white, Player black) {
		this.mode = Mode.Normal;
		this.white = Objects.requireNonNull(white, "White player cannot be null");
		this.black = Objects.requireNonNull(black, "Blackplayer cannot be null");
		this.board = new Tile[8][8];
		this.moves = new ArrayList<>();

		this.createBoard();
		this.reset();
	}
	
	/**
	 * Terminates the program.<br>
	 * Does not edit any files
	 */
	public void quit() {
		System.exit(0);
	}
	
	/**
	 * {@link #currentPlayer} resigns
	 */
	public void resign() {
		this.mode = Mode.Over;
		switch (this.currentPlayer.color) {
		case Black:
			this.result = "1-0";
			break;
		case White:
			this.result = "0-1";
			break;
		default:
			throw new IllegalStateException("Illegal current Player PieceColor:\t" + this.currentPlayer.color.name());
		}
		this.write();
	}

	/**
	 * Move the selected {@link Piece} from {@link #source} to {@link #destination}
	 */
	private void advancePiece() {
		Chess.logger.info("Advancing piece");
		Piece piece = this.source.getPiece();

		this.currentPlayer.incrementScore(this.destination.getPiece());
		this.destination.updatePiece(piece);
		this.source.reset();
	}

	/**
	 * Append the move made to {@link #moves}
	 * 
	 * @param castle {@link CastleState} when King has castled.
	 */
	private void appendMove(CastleState castle) {
		String move = "";
		boolean attack = this.destination.getPiece() != null;

		switch (castle) {
		case Fail:
			return;
		case Kingside:
			move = "O-O";
			break;
		case Queenside:
			move = "O-O-O";
			break;
		case Unattempted:
			final char an = this.source.getPiece().toAN();
			switch (an) {
			case Piece.an_pawn:
				move += attack ? this.source.colToString() : "";
				break;
			default:
				move += String.valueOf(an);
				break;
			}

			move += attack ? "x" : "";
			move += this.destination.toString();
			break;
		default:
			throw new IllegalStateException("Illegal CastleState:\t" + castle.name());
		}

		Chess.logger.info("Appending move:\t" + move);
		this.moves.add(move);
	}

	/**
	 * Determine if enemy Knights are able to Check the King.<br>
	 * If an enemy Knight is found.
	 * 
	 * @param tile {@link Tile} King is on
	 * @param king {@link King} itself
	 * @throws NullPointerException if inputed Tile is null
	 * @throws NullPointerException if inputed Tile does not have a piece on it
	 */
	private void checkKnights(Tile tile, King king) throws NullPointerException {
		Objects.requireNonNull(tile, "Tile cannot be null");
		Objects.requireNonNull(king, "King cannot be null");

		for (Tile t : this.knightTiles(tile)) {
			if (t == null)
				continue;

			Chess.logger.info(String.format("Checking tile %s for Knight", t.toString()));
			Piece piece = t.getPiece();
			if (piece != null)
				if (!king.isAlly(piece))
					if (piece instanceof Knight) {
						king.setCheck(true);
						return;
					}
		}
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
	 * Compares a {@link Tile} to {@link #source}
	 * 
	 * @param tile {@link Tile} to compare with
	 * @return true if {@link #source} and tile are {@link Tile#equals(Object)}<br>
	 *         false otherwise
	 */
	public boolean compareSource(Tile tile) {
		if (this.source == null)
			return false;
		return this.source.equals(tile);
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
	 * Draw the game
	 */
	public void draw() {
		this.mode = Mode.Over;
		this.result = "1/2-1/2";
		this.write();
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
						Chess.logger.info(
								String.format("Found %s King on %s", this.currentPlayer.color.name(), tile.toString()));
						return tile;
					}
				} else if (piece.color == this.nextPlayer.color) {
					Chess.logger
							.info(String.format("Found %s King on %s", this.nextPlayer.color.name(), tile.toString()));
					return tile;
				}
			}
		throw new IllegalStateException("Cannot find King");
	}

	/**
	 * Get {@link #board}
	 * 
	 * @return {@link #board}
	 */
	public Tile[][] getBoard() {
		return this.board;
	}

	/**
	 * Get {@link #currentPlayer}
	 * 
	 * @return {@link #currentPlayer}
	 */
	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	/**
	 * Get {@link #nextPlayer}
	 * 
	 * @return {@link #nextPlayer}
	 */
	public Player getNextPlayer() {
		return this.nextPlayer;
	}

	/**
	 * Get a {@link Tile} that is offset from an inputed Tile
	 * 
	 * @param tile {@link Tile} to serve as the origin
	 * @param x    horizontal offset
	 * @param y    vertical offset
	 * @return Tile offset
	 * @throws ArrayIndexOutOfBoundsException when offset goes out of bounds
	 */
	public final Tile getTileOffset(Tile tile, int x, int y) throws ArrayIndexOutOfBoundsException {
		return this.board[tile.row + y][tile.col + x];
	}

	/**
	 * Determine if {@link #mode} is {@link Mode#Over}
	 * 
	 * @return true if the game is over<br>
	 *         false if the game is not over
	 */
	public boolean isGameOver() {
		switch (this.mode) {
		case Over:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Handle the {@link King} castling
	 * 
	 * @return {@link CastleState} depending upon if the King castled.
	 */
	private CastleState kingCastled() {
		if (!(this.source.getPiece() instanceof King))
			return CastleState.Unattempted;

		King king = (King) this.source.getPiece();
		Piece piece;
		switch (this.destination.col) {
		case 1:
			if (!king.canQueensideCastle())
				return CastleState.Fail;

			for (int i = 1; i < this.source.col; ++i)
				if (this.board[this.source.row][i].getPiece() != null)
					return CastleState.Fail;

			piece = this.board[this.source.row][0].getPiece();

			this.board[this.source.row][0].reset();
			this.board[this.source.row][2].updatePiece(piece);
			return CastleState.Queenside;
		case 6:
			if (!king.canKingsideCastle())
				return CastleState.Unattempted;

			for (int i = this.source.col + 1; i < 7; ++i)
				if (this.board[this.source.row][i].getPiece() != null)
					return CastleState.Fail;

			piece = this.board[this.source.row][7].getPiece();
			this.board[this.source.row][7].reset();
			this.board[this.source.row][5].updatePiece(piece);
			return CastleState.Kingside;
		default:
			return CastleState.Unattempted;
		}
	}

	/**
	 * Determine if the {@link King} moves itself into a Check
	 * 
	 * @return true if the {@link King} moves itself into a check<br>
	 *         false otherwise
	 */
	private boolean kingMoveIntoCheck() {
		return false;
	}

	/**
	 * Find {@link Tile} where {@link Knight} can move to the inputed {@link Tile}
	 * 
	 * @param tile {@link Tile} to find Knights from
	 * @return primitive type array
	 */
	private Tile[] knightTiles(Tile tile) {
		List<Tile> temp = new ArrayList<>();

		for (int x = -2; x < 3; ++x) {
			int y;
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

			try {
				temp.add(this.getTileOffset(tile, x, -y));
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				continue;
			}

			try {
				temp.add(this.getTileOffset(tile, x, y));
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				continue;
			}
		}

		return temp.toArray(new Tile[temp.size()]);
	}

	/**
	 * Determine if a {@link Piece} moved out of a pin.
	 * 
	 * @return true if a piece moves itself out of a pin<br>
	 *         false otherwise
	 */
	private boolean moveOutOfPin() {
		return false;
	}

	/**
	 * Handling logic of moving a piece.<br>
	 * Actual updating of the GUI is done in {@link #advancePiece()}
	 */
	private void movePiece() {
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

		boolean kingMoveIntoCheck = this.kingMoveIntoCheck();
		Chess.logger.info(kingMoveIntoCheck ? "King moved into Check" : "King did not move into Check");

		Tile ally_king = this.findKing(true);

		CastleState castled = this.kingCastled();
		Chess.logger.info("CastleState:\t" + castled.name());
		switch (castled) {
		case Fail:
			return;
		case Kingside:
		case Queenside:
		case Unattempted:
			break;
		default:
			throw new IllegalStateException("Illegal CastleState:\t" + castled.name());
		}

		this.updateKing(ally_king);
		this.appendMove(castled);
		this.advancePiece();
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
				tile.updatePiece(piece);
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
				tile.updatePiece(piece);
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
		this.moves.clear();
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

	/**
	 * Determine the result of the game
	 * 
	 * @return {@link #result}
	 */
	public String result() {
		return this.result;
	}

	/**
	 * Set {@link #mode}
	 * 
	 * @param mode new {@link Mode}
	 */
	public void setMode(Mode mode) {
		this.mode = Objects.requireNonNull(mode, "New mode cannot be null");
	}

	/**
	 * This method is called whenever a {@link Tile} is clicked.
	 */
	public void tileClicked() {
		if (this.source == null)
			return;

		if (!this.currentPlayer.movingAlly(this.source)) {
			this.resetTiles();
			return;
		}

		if (this.destination == null)
			return;

		this.movePiece();
		this.resetTiles();
	}

	/**
	 * Check for ally rooks and update {@link King#king} and {@link King#queen}
	 * 
	 * @param tile {@link Tile} King is on
	 * @param king {@link King} itself
	 */
	private void updateCastle(Tile tile, King king) {
		Chess.logger.info("Update Castle");
		Piece piece = this.destination.getPiece();

		if (piece instanceof King) {
			king.setKingside(false);
			king.setQueenside(false);
		}

		if (piece instanceof Rook) {
			switch (this.source.col) {
			case 0:
				king.setQueenside(false);
				return;
			case 7:
				king.setKingside(false);
				return;
			default:
				return;
			}
		}
	}

	/**
	 * Update {@link #destination}
	 * 
	 * @param tile new destination {@link Tile}
	 */
	public void updateDestination(Tile tile) {
		Objects.requireNonNull(tile, "New destination tile cannot be null");
		Chess.logger.info("Updating destination:\t" + tile.toString());

		if (this.destination == null)
			this.destination = tile;
	}

	/**
	 * Update {@link King#check}, {@link King#king}, and {@link King#queen}
	 * 
	 * @param king_tile {@link Tile} {@link King} is on
	 */
	private void updateKing(Tile king_tile) {
		Chess.logger.info("Updating King");

		King king = (King) king_tile.getPiece();
		this.checkKnights(king_tile, king);
		this.updateCastle(king_tile, king);
		king.debug();
	}

	/**
	 * Swap {@link #currentPlayer} and {@link #nextPlayer}
	 */
	private void updatePlayers() {
		Player temp = this.currentPlayer;
		this.currentPlayer = this.nextPlayer;
		this.nextPlayer = temp;
	}

	/**
	 * Update {@link #source}
	 * 
	 * @param tile new source {@link Tile}
	 */
	public void updateSource(Tile tile) {
		Objects.requireNonNull(tile, "New source tile cannot be null");
		Chess.logger.info("Updating source:\t" + tile.toString());

		if (this.source == null)
			this.source = tile;
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
			writer.write("[Result \"" + this.result + "\"]\n");
			writer.write("\n");

			for (int i = 0; i < this.moves.size(); i += 2) {
				String white = this.moves.get(i);
				String black;
				try {
					black = this.moves.get(i + 1);
				} catch (IndexOutOfBoundsException ioobe) {
					black = "";
				}
				int move = i / 2 + 1;

				writer.write(String.format("%d. %s %s", move, white, black));
				writer.write(move % 7 == 0 ? "\n" : " ");
			}

			writer.write(this.result.isEmpty() ? "" : this.result);
		} catch (IOException e) {
			Chess.logger.throwing("Chessboard", "write", e);
			return;
		}

		Chess.logger.info("Writting pgn complete!");
	}
}
