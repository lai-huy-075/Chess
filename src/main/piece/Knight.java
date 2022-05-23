package main.piece;

import java.util.Objects;

import main.Chess;
import main.board.Tile;

/**
 * The Knight
 */
public class Knight extends Piece {
    /**
     * Constructor
     * 
     * @param color {@link PieceColor} of this
     */
    public Knight(final PieceColor color) {
	super(color);
    }

    @Override
    public void debug() {
	Chess.logger.info(String.format("%s, %s", this.toString(), this.color.name()));
    }

    @Override
    public Tile[] getTileTraversed(final Tile[][] board, final Tile src, final Tile dest) {
	Objects.requireNonNull(board, "Knight must be on a board");
	Objects.requireNonNull(src, "Source tile cannot be null");
	Objects.requireNonNull(dest, "Destinatino tile cannot be null");

	return empty;
    }

    @Override
    public int getValue() {
	return 3;
    }

    @Override
    public boolean isLegal(final Tile src, final Tile dest) {
	final int dx = Math.abs(src.col - dest.col);
	final int dy = Math.abs(src.row - dest.row);

	return dx == 2 && dy == 1 || dx == 1 && dy == 2;
    }

    @Override
    public void reset() {
    }

    @Override
    public char toAN() {
	return an_knight;
    }

    @Override
    public char toFEN() {
	switch (this.color) {
	case Black:
	    return 'n';
	case White:
	    return 'N';
	default:
	    return '?';
	}
    }

    @Override
    public String toString() {
	switch (this.color) {
	case White:
	    return white_knight;
	case Black:
	    return black_knight;
	default:
	    return default_name;
	}
    }
}
