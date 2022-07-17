package main.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.swing.JOptionPane;

import main.Chess;
import main.file.PGNWriter;
import main.piece.Bishop;
import main.piece.CastleState;
import main.piece.CheckState;
import main.piece.King;
import main.piece.Knight;
import main.piece.Pawn;
import main.piece.Piece;
import main.piece.PieceColor;
import main.piece.PieceType;
import main.piece.PromoteState;
import main.piece.Queen;
import main.piece.Rook;
import main.player.Player;

/**
 * Chess board
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public final class Chessboard {
	/**
	 * Primitive type array of {@link String} holding the options of promotion
	 */
	private static final String[] pieces = { "Queen", "Knight", "Rook", "Bishop" };

	/**
	 * {@link Player} with the {@link PieceColor#Black} pieces
	 */
	public final Player black;

	/**
	 * 2d array holding the location of all the pieces
	 */
	private final Tile[][] board;

	/**
	 * A reference to the current {@link Player}
	 */
	private Player currentPlayer;

	/**
	 * Destination {@link Tile}
	 */
	private Tile destination;

	/**
	 * Determine if the game is over
	 */
	private Mode mode;

	/**
	 * Move {@link List}
	 */
	private final List<String> moves;

	/**
	 * A reference to the next {@link Player}
	 */
	private Player nextPlayer;

	/**
	 * {@link String} holding the result
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
	 * @param mode  {@link Mode} of this
	 * @param white {@link Player} controlling {@link PieceColor#White}
	 * @param black {@link Player} controlling {@link PieceColor#Black}
	 */
	public Chessboard(final Mode mode, final Player white, final Player black) {
		this.mode = Objects.requireNonNull(mode, "Mode cannot be null");
		this.white = Objects.requireNonNull(white, "White player cannot be null");
		this.black = Objects.requireNonNull(black, "Black player cannot be null");
		this.board = new Tile[8][8];
		this.moves = new ArrayList<>();

		this.createBoard();
		this.reset();
	}

	/**
	 * Move the selected {@link Piece} from {@link #source} to {@link #destination}
	 */
	private void advancePiece() {
		Chess.logger.info("Advancing piece");
		final Piece src_piece = this.source.getPiece(), dest_piece = this.destination.getPiece();

		if (dest_piece != null)
			dest_piece.setTile(null);

		this.currentPlayer.incrementScore(dest_piece);
		this.destination.updatePiece(src_piece);
		this.source.reset();

		for (final Pawn pawn : this.nextPlayer.getPawn())
			pawn.setEnPassant(false);

		Chess.logger.info(this.currentPlayer.debug());
	}

	/**
	 * Append the move made to {@link #moves}
	 * 
	 * @param attack  determine if an enemy {@link Piece} was captured
	 * @param promote determine if a {@link Pawn} promoted.
	 */
	private void appendMove(final boolean attack, final PromoteState promote) {
		Objects.requireNonNull(promote, "PromoteState cannot be null");
		String move = "";
		King ally_king, enemy_king;
		switch (this.currentPlayer.color) {
		case Black:
			ally_king = this.black.getKing();
			enemy_king = this.white.getKing();
			break;
		case White:
			ally_king = this.white.getKing();
			enemy_king = this.black.getKing();
			break;
		default:
			throw new IllegalStateException("Illegal PieceColor:\t" + this.currentPlayer.color.name());
		}

		final CastleState castle = ally_king.getCastle();
		final CheckState check = enemy_king.getCheckState();

		switch (castle) {
		case Fail:
			break;
		case Kingside:
			move = "O-O";
			break;
		case Queenside:
			move = "O-O-O";
			break;
		case Unattempted:
			if (promote != PromoteState.Fail)
				move += attack ? this.source.colToString() : "";
			else {
				final PieceType type = this.destination.getPiece().type;
				switch (type) {
				case Pawn:
					move += attack ? this.source.colToString() : "";
					break;
				default:
					move += String.valueOf(type.an);
					break;
				}
			}

			move += attack ? "x" : "";
			move += this.destination.toString();

			break;
		default:
			throw new IllegalStateException("Illegal CastleState:\t" + ally_king.getCastle().name());
		}

		switch (promote) {
		case Bishop:
			move += "=B";
			break;
		case Fail:
			break;
		case Knight:
			move += "=N";
			break;
		case Queen:
			move += "=Q";
			break;
		case Rook:
			move += "=R";
			break;
		default:
			throw new IllegalStateException("Illegal PromoteState:\t" + promote.name());
		}

		switch (check) {
		case Check:
			move += "+";
			break;
		case Fail:
			break;
		case Mate:
			move += "#";
			break;
		case Stale:
			break;
		default:
			throw new IllegalStateException("Illegal CheckState:\t" + check.name());
		}

		Chess.logger.info("Appending move:\t" + move);
		this.moves.add(move);
	}

	/**
	 * Determine if a piece collided with any other piece when moving from
	 * {@link #source} to {@link #destination}
	 *
	 * @param traversed privative array of {@link Tile}
	 * @return true if a piece collides with another. false otherwise.
	 */
	/**
	 * Determine if a piece collided with any other piece when moving
	 * 
	 * @param source      The {@link Tile} the {@link Piece} is moving from
	 * @param destination The {@link Tile} the {@link Piece} is moving to
	 * @param traversed   primitive type array of {@link Tile} holding all
	 *                    {@link Tile} a {@link Piece} traverses on its journey
	 * @return {@code true} is collision is detected<br>
	 *         {@code false} otherwise.
	 */
	private boolean collide(final Tile source, final Tile destination, final Tile[] traversed) {
		Objects.requireNonNull(source, "Source tile cannnot be null");
		Objects.requireNonNull(destination, "Destination tile cannot be null");
		Objects.requireNonNull(traversed, "Traversal cannot be null");

		for (final Tile tile : traversed) {
			if (tile.equals(source))
				continue;

			if (tile.equals(destination))
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
	public boolean compareSource(final Tile tile) {
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

		for (int row = 0; row < this.board.length; ++row)
			for (int col = 0; col < this.board[row].length; ++col) {
				this.board[row][col].setUp(row - 1 > 0 ? this.board[row - 1][col] : null);
				this.board[row][col].setDown(row + 1 < this.board.length ? this.board[row + 1][col] : null);
			}
	}

	/**
	 * Debugs {@link #source} and {@link #destination} to {@link Chess#logger}
	 */
	public void debugTiles() {
		Chess.logger.info("Source:\t" + this.source);
		Chess.logger.info("Dest:\t" + this.destination);
	}

	/**
	 * Draw the game
	 */
	public void draw() {
		this.result = "1/2-1/2";
		this.endGame();
	}

	/**
	 * Sets {@link #mode} to {@link Mode#Over} and calls {@link #write}
	 */
	private void endGame() {
		this.mode = Mode.Over;
		this.write();
	}

	/**
	 * Find all pieces of a certain {@link PieceColor}
	 *
	 * @param color {@link PieceColor} to find
	 * @return primitive type array of {@link Tile}
	 */
	private Tile[] findPieces(final PieceColor color) {
		Objects.requireNonNull(color, "PieceColor cannot be null");

		List<Tile> pieces = new ArrayList<>();
		Tile tile;
		switch (color) {
		case White:
			for (final Piece piece : this.white.pieces) {
				tile = piece.getTile();
				if (tile == null)
					continue;
				pieces.add(tile);
			}
			break;
		case Black:
			for (final Piece piece : this.black.pieces) {
				tile = piece.getTile();
				if (tile == null)
					continue;
				pieces.add(tile);
			}
			break;
		default:
			throw new IllegalStateException("Illegal PieceColor:\t" + color.name());
		}

		return pieces.toArray(new Tile[pieces.size()]);
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
	 * Get {@link #mode}
	 * 
	 * @return {@link #mode}
	 */
	public Mode getMode() {
		return this.mode;
	}

	/**
	 * Get {@link #moves}
	 * 
	 * @return {@link #moves}
	 */
	public List<String> getMoves() {
		return this.moves;
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
	public Tile getTileOffset(final Tile tile, final int x, final int y) throws ArrayIndexOutOfBoundsException {
		Objects.requireNonNull(tile, "Origin tile cannot be null");
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

		final King king = (King) this.source.getPiece();

		if (this.destination.row != (king.color == PieceColor.White ? 7 : 0))
			return CastleState.Unattempted;

		Piece piece;
		switch (this.destination.col) {
		case 2:
			if (!king.canQueensideCastle())
				return CastleState.Fail;

			for (int i = 1; i < this.source.col; ++i)
				if (this.board[this.source.row][i].getPiece() != null
						|| this.kingMoveIntoCheck(this.board[this.source.row][i]))
					return CastleState.Fail;

			piece = this.board[this.source.row][0].getPiece();

			this.board[this.source.row][0].reset();
			this.board[this.source.row][3].updatePiece(piece);
			king.setCastle(CastleState.Queenside);
			return CastleState.Queenside;
		case 6:
			if (!king.canKingsideCastle())
				return CastleState.Fail;

			for (int i = this.source.col + 1; i < 7; ++i)
				if (this.board[this.source.row][i].getPiece() != null
						|| this.kingMoveIntoCheck(this.board[this.source.row][i]))
					return CastleState.Fail;

			piece = this.board[this.source.row][7].getPiece();
			this.board[this.source.row][7].reset();
			this.board[this.source.row][5].updatePiece(piece);
			king.setCastle(CastleState.Kingside);
			return CastleState.Kingside;
		default:
			return CastleState.Unattempted;
		}
	}

	/**
	 * Determine if the {@link King} moves itself into a Check
	 *
	 * @param tile {@link Tile} to check
	 *
	 * @return true if the {@link King} moves itself into a check<br>
	 *         false otherwise
	 */
	private boolean kingMoveIntoCheck(final Tile tile) {
		Objects.requireNonNull(tile, "Tile cannot be null");
		if (!(this.source.getPiece() instanceof King))
			return false;

		final Piece sp = this.source.getPiece(), dp = this.destination.getPiece();
		this.source.reset();
		this.destination.updatePiece(sp);

		for (final Tile t : this.findPieces(sp.color.opponent())) {
			final Piece piece = t.getPiece();
			if (!piece.isLegal(t, tile))
				continue;

			if (this.collide(t, tile, piece.getTileTraversed(this.board, t, tile)))
				continue;

			this.source.updatePiece(sp);
			this.destination.updatePiece(dp);
			return true;
		}

		this.source.updatePiece(sp);
		this.destination.updatePiece(dp);
		return false;
	}

	/**
	 * Load moves from a pre-set list of moves.
	 * 
	 * @param moves variable argument of moves to load in
	 */
	public void loadMoves(String... moves) {
		this.moves.clear();
		this.moves.addAll(List.of(moves));
	}

	/**
	 * Handling logic of moving a piece.<br>
	 * Actual updating of the GUI is done in {@link #advancePiece()}
	 */
	private void movePiece() {
		final Piece src_piece = this.source.getPiece();
		if (src_piece == null)
			return;

		Chess.logger.info(String.format("Moving %s from %s to %s", src_piece.toFigure(), this.source.toString(),
				this.destination.toString()));

		if (this.source.equals(this.destination)) {
			Chess.logger.info("No movement detected.");
			return;
		}

		boolean isAlly = this.source.getPiece().isAlly(this.destination.getPiece());
		Chess.logger.info(isAlly ? "Attempting to capture Ally" : "Capturing enemy or moving to empty tile");
		if (isAlly)
			return;

		final boolean legal = src_piece.isLegal(this.source, this.destination);
		Chess.logger.info(legal ? "Move is legal\n" : "Move is not legal\n");
		if (!legal)
			return;

		final Tile[] traversed = src_piece.getTileTraversed(this.board, this.source, this.destination);
		Chess.logger.info("Traversed:\t" + Arrays.deepToString(traversed));
		final boolean collide = this.collide(this.source, this.destination, traversed);
		Chess.logger.info(
				src_piece.toFigure() + (collide ? " collided on its journey" : " did not collide on its journey"));
		if (collide)
			return;

		King ally_king, enemy_king;
		switch (this.currentPlayer.color) {
		case Black:
			ally_king = this.black.getKing();
			enemy_king = this.white.getKing();
			break;
		case White:
			ally_king = this.white.getKing();
			enemy_king = this.black.getKing();
			break;
		default:
			throw new IllegalStateException("Illegal PieceColor:\t" + this.currentPlayer.color.name());
		}

		if (src_piece instanceof King) {
			final boolean kingMoveIntoCheck = this.kingMoveIntoCheck(this.destination);
			Chess.logger.info(kingMoveIntoCheck ? "King moved into Check" : "King did not move into Check");
			if (kingMoveIntoCheck)
				return;

			final CastleState castle = this.kingCastled();
			Chess.logger.info("CastleState:\t" + castle.name());
			switch (castle) {
			case Fail:
				return;
			case Kingside:
				break;
			case Queenside:
				break;
			case Unattempted:
				break;
			default:
				throw new IllegalStateException("Illegal CastleState:\t" + castle.name());
			}
		} else {
			final boolean protect = this.moveProtectKing(ally_king, this.source, this.destination);
			Chess.logger.info(protect ? "Move protects the King" : "Move does not protect the King");
			if (!protect)
				return;
		}

		boolean attack = this.destination.getPiece() != null;
		PromoteState promote = PromoteState.Fail;
		if (src_piece instanceof Pawn) {
			boolean diagonal = ((Pawn) src_piece).getDiagonal();
			switch (src_piece.color) {
			case Black:
				if (diagonal && (!attack)) {
					attack |= this.destination.getUp().getPiece() != null;
					this.destination.getUp().reset();
				}

				if (this.destination.row == 7)
					promote = this.promote();
				break;
			case White:
				if (diagonal && (!attack)) {
					attack |= this.destination.getDown().getPiece() != null;
					this.destination.getDown().reset();
				}

				if (this.destination.row == 0)
					promote = this.promote();
				break;
			default:
				break;
			}
		}

		this.advancePiece();
		this.updateKing(ally_king);
		this.updateCheck(enemy_king);
		this.updateCheckMate(enemy_king);
		this.appendMove(attack, promote);
		this.updatePlayers();

		if (enemy_king.getCheckState() == CheckState.Mate) {
			switch (this.currentPlayer.color) {
			case Black:
				this.result = "1-0";
				break;
			case White:
				this.result = "0-1";
				break;
			default:
				throw new IllegalStateException(
						"Illegal current Player PieceColor:\t" + this.currentPlayer.color.name());
			}

			this.endGame();
		}
	}

	/**
	 * Determine if a move will protect the {@link King} from check.<br>
	 * This method assumes that movement from source to destination is
	 * {@link Piece#isLegal(Tile, Tile)} and does not
	 * {@link #collide(Tile, Tile, Tile[])}
	 * 
	 * @param king        The {@link King} that needs protection
	 * @param source      The {@link Tile} a piece is moving from
	 * @param destination The {@link Tile} a piece is moving to
	 * @return {@code true} if the move protects the {@link King}<br>
	 *         {@code false} otherwise
	 */
	private boolean moveProtectKing(final King king, final Tile source, final Tile destination) {
		Objects.requireNonNull(king, "King cannot be null");
		Objects.requireNonNull(source, "Source tile cannot be null");
		Objects.requireNonNull(destination, "Destination tile cannot be null");

		final Piece sp = source.getPiece(), dp = destination.getPiece();
		source.reset();
		destination.updatePiece(sp);
		if (dp != null)
			dp.setTile(null);

		final Tile king_tile = sp instanceof King ? destination : king.getTile();

		for (final Tile tile : this.findPieces(king.color.opponent())) {
			final Piece piece = tile.getPiece();

			if (!piece.isLegal(tile, king_tile))
				continue;

			if (this.collide(tile, king_tile, piece.getTileTraversed(this.board, tile, king_tile)))
				continue;

			source.updatePiece(sp);
			destination.updatePiece(dp);
			if (dp != null)
				dp.setTile(destination);
			return false;
		}

		source.updatePiece(sp);
		destination.updatePiece(dp);
		if (dp != null)
			dp.setTile(destination);
		return true;
	}

	/**
	 * Place the {@link Piece} on {@link #board}
	 */
	private void placePieces() {
		Chess.logger.info("Placing pieces on the Chess board");

		int indexInList = 0;
		Tile tile;
		Piece piece;

		// Place white pieces
		for (int i = 6; i < 8; ++i)
			for (int j = 0; j < 8; ++j) {
				tile = this.board[i][j];
				piece = this.white.pieces[indexInList];
				piece.reset();
				tile.updatePiece(piece);
				piece.setTile(tile);
				++indexInList;
			}

		// Place black pieces
		indexInList = 0;
		for (int i = 1; i > -1; --i)
			for (int j = 0; j < 8; ++j) {
				tile = this.board[i][j];
				piece = this.black.pieces[indexInList];
				piece.reset();
				tile.updatePiece(piece);
				piece.setTile(tile);
				++indexInList;
			}
	}

	/**
	 * Promote a pawn
	 * 
	 * @return {@link PromoteState}
	 */
	private PromoteState promote() {
		final int piece = JOptionPane.showOptionDialog(null, "Select a piece", "Promotion", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, Chess.icon, pieces, "Queen");
		final int file = ((Pawn) this.source.getPiece()).starting_File;
		final PromoteState state;
		switch (piece) {
		case 1:
			this.currentPlayer.pieces[file] = new Knight(this.currentPlayer.color);
			state = PromoteState.Knight;
			break;
		case 2:
			this.currentPlayer.pieces[file] = new Rook(this.currentPlayer.color);
			state = PromoteState.Rook;
			break;
		case 3:
			this.currentPlayer.pieces[file] = new Bishop(this.currentPlayer.color);
			state = PromoteState.Bishop;
			break;
		default:
			this.currentPlayer.pieces[file] = new Queen(this.currentPlayer.color);
			state = PromoteState.Queen;
			break;
		}
		Chess.logger.info("Promoted Pawn to " + state.name());
		this.source.updatePiece(this.currentPlayer.pieces[file]);
		return state;
	}

	/**
	 * Terminates the program.<br>
	 * Does not edit any files
	 */
	public void quit() {
		System.exit(0);
		return;
	}

	/**
	 * Reset the board and all attributes.
	 */
	public void reset() {
		this.resetBoard();
		this.resetTiles();
		this.placePieces();
		this.white.reset();
		this.black.reset();
		this.moves.clear();
		this.currentPlayer = this.white;
		this.nextPlayer = this.black;
		Chess.logger.info("Reset Chess board\n\n");
	}

	/**
	 * Reset {@link #board}
	 */
	private void resetBoard() {
		Chess.logger.info("Reset board array");
		for (final Tile[] row : this.board)
			for (final Tile tile : row)
				tile.reset();
	}

	/**
	 * Set {@link #source} and {@link #destination} to null
	 */
	public void resetTiles() {
		Chess.logger.info("Reseting Tiles\n");
		this.source = null;
		this.destination = null;
	}

	/**
	 * {@link #currentPlayer} resigns
	 */
	public void resign() {
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
		this.endGame();
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
	 * This method is called whenever a {@link Tile} is clicked.
	 */
	public void tileClicked() {
		switch (this.mode) {
		case Debug:
			return;
		case Funny:
			break;
		case Normal:
			break;
		case Over:
			return;
		default:
			throw new IllegalStateException("Illegal Mode:\t" + this.mode);
		}

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

	@Override
	public String toString() {
		String out = "";
		int count;
		for (final Tile[] row : this.board) {
			count = 0;
			for (final Tile tile : row) {
				final Piece piece = tile.getPiece();
				if (piece == null)
					++count;
				else {
					if (count != 0)
						out += String.valueOf(count);
					out += String.valueOf(piece.toFEN());
				}
			}
			if (count != 0)
				out += String.valueOf(count);
			out += "/";
		}

		return out.substring(0, out.length() - 1);
	}

	/**
	 * Check for ally rooks and update {@link King#king} and {@link King#queen}
	 *
	 * @param king {@link King} itself
	 */
	private void updateCastle(final King king) {
		Objects.requireNonNull(king, "King cannot be null");
		Chess.logger.info(String.format("Updating CastleState for %s King%n", king.color.name()));
		final Piece piece = this.destination.getPiece();

		if (piece instanceof King) {
			king.setKingside(false);
			king.setQueenside(false);
			return;
		}

		if (piece instanceof Rook)
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

	/**
	 * Update {@link King#check} to {@link CheckState#Check} if required
	 * 
	 * @param king {@link King}
	 */
	private void updateCheck(final King king) {
		Objects.requireNonNull(king, "King cannot be null");
		Chess.logger.info(String.format("Updating CheckState for %s King", king.color.name()));

		final Tile king_tile = king.getTile();

		for (final Tile t : this.findPieces(king.color.opponent())) {
			final Piece piece = t.getPiece();
			if (!piece.isLegal(t, king_tile))
				continue;

			if (this.collide(t, king_tile, piece.getTileTraversed(this.board, t, king_tile)))
				continue;

			king.setCheck(CheckState.Check);
			return;
		}

		king.setCheck(CheckState.Fail);
	}

	/**
	 * Update {@link King#check} to {@link CheckState#Mate} if required
	 * 
	 * @param king {@link King}
	 */
	private void updateCheckMate(final King king) {
		Objects.requireNonNull(king, "King cannot be null");
		Chess.logger.info(String.format("Updating CheckState for %s King to Mate if necessary", king.color.name()));
		if (king.getCheckState() == CheckState.Fail)
			return;

		// Find all attacking pieces and their path to the King
		Tile king_tile = king.getTile();
		List<Tile> traverse = new ArrayList<>();
		for (Tile enemy : this.findPieces(king.color.opponent())) {
			final Piece piece = enemy.getPiece();
			if (!piece.isLegal(enemy, king_tile))
				continue;

			Tile[] traversed = piece.getTileTraversed(this.board, enemy, king_tile);
			if (this.collide(enemy, king_tile, traversed))
				continue;

			traverse.addAll(List.of(traversed));
		}

		// Determine if any ally piece can protect the king
		for (Tile ally : this.findPieces(king.color)) {
			final Piece piece = ally.getPiece();
			for (Tile prot : traverse) {
				if (!piece.isLegal(ally, prot))
					continue;

				if (this.collide(ally, prot, piece.getTileTraversed(this.board, ally, prot)))
					continue;

				if (!this.moveProtectKing(king, ally, prot))
					continue;

				Chess.logger.info("Found defending piece:\t" + piece.toFigure());
				Chess.logger.info(String.format("Move from %s to %s", ally.toString(), prot.toString()));
				return;
			}
		}

		// Determine if the King can move itself out of check
		for (Tile tile : king.getSurround(this.board)) {
			if (king.isAlly(tile.getPiece()))
				continue;

			if (this.moveProtectKing(king, king_tile, tile))
				return;
		}

		king.setCheck(CheckState.Mate);
	}

	/**
	 * Update {@link #destination}
	 *
	 * @param tile new destination {@link Tile}
	 */
	public void updateDestination(final Tile tile) {
		Objects.requireNonNull(tile, "New destination tile cannot be null");
		Chess.logger.info("Updating destination:\t" + tile.toString());

		if (this.destination == null)
			this.destination = tile;
	}

	/**
	 * Update {@link King#check}, {@link King#king}, and {@link King#queen}
	 *
	 * @param king {@link King} being updated
	 */
	private void updateKing(final King king) {
		Objects.requireNonNull(king, "King cannot be null");
		Chess.logger.info(String.format("Updating %s King", king.color.name()));

		this.updateCheck(king);
		this.updateCheckMate(king);
		this.updateCastle(king);
	}

	/**
	 * Swap {@link #currentPlayer} and {@link #nextPlayer}
	 */
	private void updatePlayers() {
		final Player temp = this.currentPlayer;
		this.currentPlayer = this.nextPlayer;
		this.nextPlayer = temp;
	}

	/**
	 * Update {@link #source}
	 *
	 * @param tile new source {@link Tile}
	 */
	public void updateSource(final Tile tile) {
		Objects.requireNonNull(tile, "New source tile cannot be null");
		Chess.logger.info("Updating source:\t" + tile.toString());

		if (this.source == null)
			this.source = tile;
	}

	/**
	 * Write {@link Chess#pgn_file} with details of the game
	 */
	public void write() {
		PGNWriter.write(this);
	}
}
