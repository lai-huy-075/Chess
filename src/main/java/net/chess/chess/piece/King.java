package net.chess.chess.piece;

import net.chess.chess.ChessApplication;
import net.chess.chess.board.Tile;
import net.chess.chess.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
 * rarely, the middle game.<br>
 * Read more <a href="https://en.wikipedia.org/wiki/King_(chess)">here</a>.
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public class King extends Piece {
	/**
	 * {@link CastleState} holding if the King castled.
	 */
	private CastleState castle;
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
		super(color, PieceType.King);
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

	/**
	 * Get {@link #castle}
	 *
	 * @return {@link #castle}
	 */
	public CastleState getCastle() {
		return this.castle;
	}

	/**
	 * Getter method for {@link #check}
	 *
	 * @return {@link #check}
	 */
	public CheckState getCheckState() {
		return this.check;
	}

	/**
	 * Get the surrounding {@link Tile} of this
	 *
	 * @param board 2d primitive type array storing the board
	 * @return surrounding {@link Tile}
	 */
	public Tile[] getSurround(final Tile[][] board) {
		final List<Tile> surround = new ArrayList<>();
		final int x = this.tile.file, y = this.tile.rank;
		for (int dy = -1; dy < 2; ++dy)
			for (int dx = -1; dx < 2; ++dx)
				try {
					final Tile tile = board[y + dy][x + dx];
					if (!this.isAlly(tile.getPiece()))
						surround.add(tile);
				} catch (final ArrayIndexOutOfBoundsException aioobe) {
					continue;
				}

		return surround.toArray(new Tile[0]);
	}

	@Override
	public List<Tile> getTileTraversed(final Tile[][] board, final Tile src, final Tile dest) {
		Objects.requireNonNull(board, "King must be on a board");
		Objects.requireNonNull(src, "Source tile cannot be null");
		Objects.requireNonNull(dest, "Destination tile cannot be null");
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
		Objects.requireNonNull(src, "Source file cannot be null");
		Objects.requireNonNull(dest, "Destination tile cannot be null");
		if (src.equals(dest))
			return false;
		final int dx = Math.abs(src.file - dest.file), dy = Math.abs(src.rank - dest.rank);
		if (dy == 0 && !this.isCheck()) {
			if ((dest.file == 6 && this.king) || (dest.file == 2 && this.queen))
				return true;
		}
		return dx <= 1 && dy <= 1;
	}

	@Override
	public void reset() {
		this.king = true;
		this.queen = true;
		this.check = CheckState.Fail;
		this.castle = CastleState.Unattempted;
	}

	/**
	 * Set {@link #castle}
	 *
	 * @param castle new {@link CastleState}
	 */
	public void setCastle(final CastleState castle) {
		Objects.requireNonNull(castle, "CastleState cannot be null");
		ChessApplication.logger.info(String.format("Setting %s King.castle to %s", this.color.name(), castle.name()));
		this.castle = castle;
	}

	/**
	 * Set {@link #check}
	 *
	 * @param state new {@link #check} value
	 */
	public void setCheck(final CheckState state) {
		Objects.requireNonNull(state, "CheckState cannot be null");
		ChessApplication.logger.info(String.format("Setting %s King.check to %s", this.color.name(), state.name()));
		this.check = state;
	}

	/**
	 * Set {@link #king}
	 *
	 * @param bool new {@link #king} value
	 */
	public void setKingside(final boolean bool) {
		ChessApplication.logger.info(String.format("Setting %s King.king to %b", this.color.name(), bool));
		this.king = bool;
	}

	/**
	 * Set {@link #queen}
	 *
	 * @param bool new {@link #queen} value
	 */
	public void setQueenside(final boolean bool) {
		ChessApplication.logger.info(String.format("Setting %s King.queen to %b", this.color.name(), bool));
		this.queen = bool;
	}

	@Override
	public String toString() {
		return "King [castle=" + this.castle + ", check=" + this.check + ", king=" + this.king + ", queen=" + this.queen
				+ ", color=" + this.color + ", type=" + this.type + ", tile=" + this.tile + "]";
	}
}