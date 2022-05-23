package main.board;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import main.Chess;
import main.piece.CastleState;
import main.piece.CheckState;
import main.piece.King;
import main.piece.Piece;
import main.piece.PieceColor;
import main.piece.Rook;
import main.player.Player;

/**
 * Chess board
 *
 * @author Μr. Pιηεαρρlξ
 * @version 2022 05 23
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
    private Mode mode;

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
    public Chessboard(final Mode mode) {
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
    public Chessboard(final Player white, final Player black) {
	this.mode = Mode.Normal;
	this.white = Objects.requireNonNull(white, "White player cannot be null");
	this.black = Objects.requireNonNull(black, "Blackplayer cannot be null");
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

	this.currentPlayer.incrementScore(this.destination.getPiece());
	this.destination.updatePiece(this.source.getPiece());
	this.source.reset();
    }

    /**
     * Append the move made to {@link #moves}
     *
     * @param castle {@link CastleState} when King has castled.
     */
    private void appendMove(final CastleState castle, final CheckState check, final boolean attack) {
	Objects.requireNonNull(castle, "CastleState cannot be null");
	Objects.requireNonNull(check, "CheckState cannot be null");

	String move = "";

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
	    final char an = this.destination.getPiece().toAN();
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
    private boolean collide(final Tile[] traversed) throws NullPointerException {
	Objects.requireNonNull(traversed, "Traversal cannot be null");
	for (final Tile tile : traversed) {
	    if (tile.equals(this.source) || tile.equals(this.destination))
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
     * @param allies primitive type array of {@link Tile}
     * @return
     */
    private Tile findKing(final Tile... allies) {
	Chess.logger.info("Seaching for King in " + Arrays.deepToString(allies));
	for (final Tile tile : allies)
	    if (tile.getPiece() instanceof King) {
		Chess.logger.info("Found King on " + tile.toString());
		return tile;
	    }

	throw new IllegalStateException("Cannot find King");
    }

    /**
     * Find all pieces of a certain {@link PieceColor}
     *
     * @param isAlly which {@link PieceColor} to find
     * @return primitive type array of {@link Tile}
     */
    private Tile[] findPieces(final boolean isAlly) {
	Chess.logger.info(isAlly ? "Searching for ally Pieces" : "Searching for enemy Pieces");
	final List<Tile> temp = new ArrayList<>();

	for (final Tile[] row : this.board)
	    for (final Tile tile : row) {
		final Piece piece = tile.getPiece();
		if (piece == null)
		    continue;
		if (isAlly) {
		    if (piece.color == this.currentPlayer.color) {
			Chess.logger.info("Found Piece on " + tile.toString());
			temp.add(tile);
		    }
		} else if (piece.color != this.currentPlayer.color) {
		    Chess.logger.info("Found Piece on " + tile.toString());
		    temp.add(tile);
		}
	    }

	return temp.toArray(new Tile[temp.size()]);
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
    private CastleState kingCastled(final Tile... enemies) {
	if (!(this.source.getPiece() instanceof King))
	    return CastleState.Unattempted;

	final King king = (King) this.source.getPiece();
	Piece piece;
	switch (this.destination.col) {
	case 2:
	    if (!king.canQueensideCastle())
		return CastleState.Fail;

	    for (int i = 1; i < this.source.col; ++i)
		if (this.board[this.source.row][i].getPiece() != null
			|| this.kingMoveIntoCheck(this.board[this.source.row][i], enemies))
		    return CastleState.Fail;

	    piece = this.board[this.source.row][0].getPiece();

	    this.board[this.source.row][0].reset();
	    this.board[this.source.row][3].updatePiece(piece);
	    return CastleState.Queenside;
	case 6:
	    if (!king.canKingsideCastle())
		return CastleState.Fail;

	    for (int i = this.source.col + 1; i < 7; ++i)
		if (this.board[this.source.row][i].getPiece() != null
			|| this.kingMoveIntoCheck(this.board[this.source.row][i], enemies))
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
     * @param tile    {@link Tile} to check
     * @param enemies primitive type array of {@link Tile} holding locations of all
     *                enemy {@link Piece}
     *
     * @return true if the {@link King} moves itself into a check<br>
     *         false otherwise
     */
    private boolean kingMoveIntoCheck(final Tile tile, final Tile... enemies) {
	Objects.requireNonNull(tile, "Tile cannot be null");
	Objects.requireNonNull(enemies, "Enemy tiles cannot be null");
	if (enemies.length == 0)
	    throw new IllegalArgumentException("Enemy tiles cannot be empty");

	if (!(this.source.getPiece() instanceof King))
	    return false;

	for (final Tile t : enemies) {
	    final Piece piece = t.getPiece();
	    if (!piece.isLegal(t, tile) || this.collide(piece.getTileTraversed(this.board, t, tile)))
		continue;

	    return true;
	}
	return false;
    }

    /**
     * Determine if a {@link Piece} moved out of a pin.
     *
     * @param king_tile {@link Tile} {@link King} is currently on
     * @param enemies   primitive type array of {@link Tile} holding locations of
     *                  all enemy {@link Piece}
     * @return true if a piece moves itself out of a pin<br>
     *         false otherwise
     */
    private boolean moveOutOfPin(final Tile king_tile, final Tile[] enemies) {
	Objects.requireNonNull(king_tile, "King tile cannot be null");
	Objects.requireNonNull(enemies, "Enemy tiles cannot be null");
	final Piece temp = this.source.getPiece();
	this.source.reset();

	if (enemies.length == 0)
	    throw new IllegalArgumentException("Enemy tiles cannot be empty");

	for (final Tile tile : enemies) {
	    final Piece piece = tile.getPiece();

	    if (!piece.isLegal(tile, king_tile) || this.collide(piece.getTileTraversed(this.board, tile, king_tile)))
		continue;

	    this.source.updatePiece(temp);
	    return true;
	}

	this.source.updatePiece(temp);
	return false;
    }

    /**
     * Handling logic of moving a piece.<br>
     * Actual updating of the GUI is done in {@link #advancePiece()}
     */
    private void movePiece() {
	final Piece src_piece = this.source.getPiece();
	if (src_piece == null)
	    return;

	Chess.logger.info(String.format("Moving %s from %s to %s", src_piece.toString(), this.source.toString(),
		this.destination.toString()));

	if (this.source.equals(this.destination)) {
	    Chess.logger.info("No movement detected.");
	    return;
	}

	final boolean legal = src_piece.isLegal(this.source, this.destination);
	Chess.logger.info(legal ? "Move is legal" : "Move is not legal");
	if (!legal)
	    return;

	final Tile[] traversed = src_piece.getTileTraversed(this.board, this.source, this.destination);
	Chess.logger.info("Traversed:\t" + Arrays.deepToString(traversed));
	final boolean collide = this.collide(traversed);
	Chess.logger.info(
		src_piece.toString() + (collide ? " collided on its journey" : " did not collide on its journey"));
	if (collide)
	    return;

	Tile[] allies = this.findPieces(true);
	Tile ally_king = this.findKing(allies);
	final Tile[] enemies = this.findPieces(false);
	final Tile enemy_king = this.findKing(enemies);

	final boolean unpin = this.moveOutOfPin(ally_king, enemies);
	Chess.logger.info(unpin ? "Move out of pin" : "Did not move out of pin");
	if (unpin)
	    return;

	final boolean kingMoveIntoCheck = this.kingMoveIntoCheck(this.destination, enemies);
	Chess.logger.info(kingMoveIntoCheck ? "King moved into Check" : "King did not move into Check");

	final CastleState castle = this.kingCastled(enemies);
	Chess.logger.info("CastleState:\t" + castle.name());
	switch (castle) {
	case Fail:
	    return;
	case Kingside:
	case Queenside:
	case Unattempted:
	    break;
	default:
	    throw new IllegalStateException("Illegal CastleState:\t" + castle.name());
	}

	final boolean attack = this.destination.getPiece() != null;

	this.advancePiece();
	allies = this.findPieces(true);
	ally_king = this.findKing(allies);

	this.updateKing(ally_king, allies, enemies);
	this.updateKing(enemy_king, enemies, allies);
	this.appendMove(castle, ((King) enemy_king.getPiece()).getCheckState(), attack);
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
	for (int i = 6; i < 8; ++i)
	    for (int j = 0; j < 8; ++j) {
		tile = this.board[i][j];
		piece = Player.white[indexInList];
		piece.reset();
		tile.updatePiece(piece);
		++indexInList;
	    }

	// Place black pieces
	indexInList = 0;
	for (int i = 1; i > -1; --i)
	    for (int j = 0; j < 8; ++j) {
		tile = this.board[i][j];
		piece = Player.black[indexInList];
		piece.reset();
		tile.updatePiece(piece);
		++indexInList;
	    }
    }

    /**
     * Terminates the program.<br>
     * Does not edit any files
     */
    public void quit() {
	System.exit(0);
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
	for (final Tile[] row : this.board)
	    for (final Tile tile : row)
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
    public void setMode(final Mode mode) {
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

	return out;
    }

    /**
     * Check for ally rooks and update {@link King#king} and {@link King#queen}
     *
     * @param tile {@link Tile} King is on
     * @param king {@link King} itself
     */
    private void updateCastle(final Tile tile, final King king) {
	Chess.logger.info("Update Castle");
	final Piece piece = this.destination.getPiece();

	if (piece instanceof King) {
	    king.setKingside(false);
	    king.setQueenside(false);
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
     * Update {@link King#check}
     *
     * @param tile {@link Tile} {@link King} is on
     * @param king {@link King}
     */
    private void updateCheck(final Tile tile, final King king, final Tile[] enemies) {
	for (final Tile t : enemies) {
	    final Piece piece = t.getPiece();
	    if (!piece.isLegal(t, tile) || this.collide(piece.getTileTraversed(this.board, t, tile)))
		continue;

	    king.setCheck(CheckState.Check);
	    return;
	}

	king.setCheck(CheckState.Fail);
    }

    private void updateCheckMate(final Tile tile, final King king, final Tile[] allies, final Tile[] enemies) {

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
     * @param king_tile {@link Tile} {@link King} is on
     * @param allies    primitive type array of {@link Tile} holding location of all
     *                  ally {@link Piece}s
     * @param enemies   primitive type array of {@link Tile} holding location of all
     *                  enemy {@link Piece}s
     */
    private void updateKing(final Tile king_tile, final Tile[] allies, final Tile[] enemies) {
	final King king = (King) king_tile.getPiece();

	Chess.logger.info(String.format("Updating %s King", king.color.name()));

	this.updateCheck(king_tile, king, enemies);
	this.updateCheckMate(king_tile, king, allies, enemies);
	this.updateCastle(king_tile, king);
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
	Chess.logger.info("Writting pgn started...");
	final String date = Chess.now.format(Chess.format);

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
		final String white = this.moves.get(i);
		String black;
		try {
		    black = this.moves.get(i + 1);
		} catch (final IndexOutOfBoundsException ioobe) {
		    black = "";
		}
		final int move = i / 2 + 1;

		writer.write(String.format("%d. %s %s", move, white, black));
		writer.write(move % 7 == 0 ? "\n" : " ");
	    }

	    writer.write(this.result.isEmpty() ? "" : this.result);
	} catch (final IOException e) {
	    Chess.logger.throwing("Chessboard", "write", e);
	    return;
	}

	Chess.logger.info("Writting pgn complete!");
    }
}
