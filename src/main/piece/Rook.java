package main.piece;

import java.util.Objects;

import main.Chess;
import main.board.Tile;

/**
 * The rook (&#x2656;, &#x265C;) is a piece in the game of chess.<br>
 * It may move any number of squares horizontally or vertically without jumping,
 * and it may capture an enemy piece on its path; additionally, it may
 * participate in castling.<br>
 * Each player starts the game with two rooks, one in each corner on their own
 * side of the board.<br>
 * Read more <a href="https://en.wikipedia.org/wiki/Rook_(chess)">here</a>
 * 
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public class Rook extends Piece {
    /**
     * Constructor
     * 
     * @param color {@link PieceColor} of this
     */
    public Rook(final PieceColor color) {
	super(color);
    }

    @Override
    public void debug() {
	Chess.logger.info(String.format("%s, %s", this.toString(), this.color.name()));
    }

    @Override
    public Tile[] getTileTraversed(final Tile[][] board, final Tile src, final Tile dest) {
	Objects.requireNonNull(board, "Rook must be on a board");
	Objects.requireNonNull(src, "Source tile cannot be null");
	Objects.requireNonNull(dest, "Destination tile cannot be null");

	final int dx = dest.col - src.col;
	final int dy = dest.row - src.row;

	final int mx = Math.abs(dx);
	final int my = Math.abs(dy);
	final Tile[] temp = new Tile[Math.max(mx, my)];

	for (int i = 0; i < temp.length; ++i)
	    temp[i] = board[src.row + i * Integer.signum(dy)][src.col + i * Integer.signum(dx)];

	return temp;
    }

    @Override
    public int getValue() {
	return 5;
    }

    @Override
    public boolean isLegal(final Tile src, final Tile dest) {
	return src.col == dest.col ^ src.row == dest.row;
    }

    @Override
    public void reset() {

    }

    @Override
    public char toAN() {
	return an_rook;
    }

    @Override
    public char toFEN() {
	switch (this.color) {
	case Black:
	    return 'r';
	case White:
	    return 'R';
	default:
	    return '?';
	}
    }

    @Override
    public String toString() {
	switch (this.color) {
	case White:
	    return white_rook;
	case Black:
	    return black_rook;
	default:
	    return default_name;
	}
    }
}
