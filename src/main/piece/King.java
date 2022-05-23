package main.piece;

import java.util.Objects;

import main.Chess;
import main.board.Tile;

/**
 * The King
 */
public class King extends Piece {
    /**
     * {@link CheckState} holding if this King is check.
     */
    private CheckState check;

    /**
     * boolean holding if King-side castle is possible
     */
    private boolean king;

    /**
     * boolean holding if Queen-side castle is possible
     */
    private boolean queen;

    /**
     * Constructor
     * 
     * @param color {@link PieceColor} of this
     */
    public King(final PieceColor color) {
	super(color);
    }

    /**
     * Determine if the King can King-side castle
     * 
     * @return true if King-side castle is possible<br>
     *         false if King-side castle is not possible
     */
    public boolean canKingsideCastle() {
	return this.king;
    }

    /**
     * Determine if the King can Queen-side castle
     * 
     * @return true if Queen-side castle is possible<br>
     *         false if Queen-side castle is not possible
     */
    public boolean canQueensideCastle() {
	return this.queen;
    }

    @Override
    public void debug() {
	Chess.logger.info(String.format("%s, king=%b, queen=%b, check=%b", this.color.name() + " " + this.toString(),
		this.king, this.queen, this.check));
    }

    /**
     * Getter method for {@link #check}
     * 
     * @return {@link #check}
     */
    public CheckState getCheckState() {
	return this.check;
    }

    @Override
    public Tile[] getTileTraversed(final Tile[][] board, final Tile src, final Tile dest) {
	Objects.requireNonNull(board, "King must be on a board");
	Objects.requireNonNull(src, "Source tile cannot be null");
	Objects.requireNonNull(dest, "Destinatino tile cannot be null");

	return empty;
    }

    @Override
    public int getValue() throws IllegalStateException {
	throw new IllegalStateException("King cannot be captured");
    }

    /**
     * Determine if the King is in Check
     * 
     * @return true if the King is in Check<br>
     *         false if the King is not in Check
     */
    public boolean isCheck() {
	return this.check == CheckState.Check;
    }

    @Override
    public boolean isLegal(final Tile src, final Tile dest) {
	final int dx = Math.abs(src.col - dest.col), dy = Math.abs(src.row - dest.row);

	if (dest.col == 6 && dy == 0 && this.king)
	    return true;

	if (dest.col == 2 && dy == 0 && this.queen)
	    return true;

	return dx <= 1 && dy <= 1;
    }

    @Override
    public void reset() {
	this.king = true;
	this.queen = true;
	this.check = CheckState.Fail;
    }

    /**
     * Set {@link #check}
     * 
     * @param bool new {@link #check} value
     */
    public void setCheck(final CheckState state) {
	Objects.requireNonNull(state, "CheckState cannot be null");

	Chess.logger.info("Setting King.check to " + state.name());
	this.check = state;
    }

    /**
     * Set {@link #king}
     * 
     * @param bool new {@link #king} value
     */
    public void setKingside(final boolean bool) {
	this.king = bool;
    }

    /**
     * Set {@link #queen}
     * 
     * @param bool new {@link #queen} value
     */
    public void setQueenside(final boolean bool) {
	this.queen = bool;
    }

    @Override
    public char toAN() {
	return an_king;
    }

    @Override
    public char toFEN() {
	switch (this.color) {
	case Black:
	    return 'k';
	case White:
	    return 'K';
	default:
	    return '?';
	}
    }

    @Override
    public String toString() {
	switch (this.color) {
	case White:
	    return white_king;
	case Black:
	    return black_king;
	default:
	    return default_name;
	}
    }
}
