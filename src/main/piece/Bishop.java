package main.piece;

import java.util.Objects;

import main.Chess;
import main.board.Tile;

/**
 * The Bishop
 */
public class Bishop extends Piece {
    /**
     * Constructor
     * 
     * @param color {@link PieceColor} of this
     */
    public Bishop(final PieceColor color) {
	super(color);
    }

    @Override
    public void debug() {
	Chess.logger.info(String.format("%s, %s", this.toString(), this.color.name()));
    }

    @Override
    public Tile[] getTileTraversed(final Tile[][] board, final Tile src, final Tile dest) {
	Objects.requireNonNull(board, "Bishop must be on a board");
	Objects.requireNonNull(src, "Source tile cannot be null");
	Objects.requireNonNull(dest, "Destinatino tile cannot be null");

	final Tile[] temp = new Tile[Math.abs(src.col - dest.col)];
	final int dx = dest.col < src.col ? -1 : 1, dy = dest.row < src.row ? -1 : 1;
	for (int i = 0; i < temp.length; ++i)
	    temp[i] = board[src.row + i * dy][src.col + i * dx];

	return temp;
    }

    @Override
    public int getValue() {
	return 3;
    }

    @Override
    public boolean isLegal(final Tile src, final Tile dest) {
	return Math.abs(src.col - dest.col) == Math.abs(src.row - dest.row);
    }

    @Override
    public void reset() {
    }

    @Override
    public char toAN() {
	return an_bishop;
    }

    @Override
    public char toFEN() {
	switch (this.color) {
	case Black:
	    return 'b';
	case White:
	    return 'B';
	default:
	    return '?';
	}
    }

    @Override
    public String toString() {
	switch (this.color) {
	case White:
	    return white_bishop;
	case Black:
	    return black_bishop;
	default:
	    return default_name;
	}
    }
}
