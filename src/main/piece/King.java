package main.piece;

import java.util.Objects;

import main.Chess;
import main.board.Tile;
import main.player.Player;

/**
 * The king (&#x2654;, &#x265A;) is the most important piece in the game of
 * chess.<br>
 * It may move to any adjoining square or perform a move known as castling.<br>
 * If a {@link Player}'s king is threatened with capture, it is said to be in
 * {@link CheckState#Check}, and the player must remove the threat of capture on
 * the next move.<br>
 * If this cannot be done, the king is said to be in {@link CheckState#Mate},
 * resulting in a loss for that player.<br>
 * A player cannot make any move that places their own king in check.<br>
 * Despite this, the king can become a strong offensive piece in the endgame or,
 * rarely, the middlegame.<br>
 * Read more <a href="https://en.wikipedia.org/wiki/King_(chess)">here</a>.
 * 
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
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
     * @param state new {@link #check} value
     */
    public void setCheck(final CheckState state) {
        Objects.requireNonNull(state, "CheckState cannot be null");

        Chess.logger.info(String.format("Setting %s King.check to %s", this.color.name(), this.check.name()));
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
