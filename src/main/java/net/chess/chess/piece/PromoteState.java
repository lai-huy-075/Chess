package net.chess.chess.piece;

import net.chess.chess.player.Player;

/**
 * <p>
 * Promotion is the replacement of a {@link Pawn} with a new {@link Queen},
 * {@link Rook}, {@link Bishop}, or {@link Knight} of the same
 * {@link PieceColor}. It occurs immediately when the pawn moves to its last
 * rank, with the {@link Player} choosing the piece of promotion. The new piece
 * does not have to be a previously captured piece. Promotion is mandatory; the
 * pawn cannot remain as a pawn.
 * </p>
 * Read more <a href="https://en.wikipedia.org/wiki/Promotion_(chess)">here</a>.
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 07 13
 */
public enum PromoteState {
	/**
	 * {@link Pawn} promote to {@link Bishop}
	 */
	Bishop,

	/**
	 * {@link Pawn} did not promote
	 */
	Fail,

	/**
	 * {@link Pawn} promote to {@link Knight}
	 */
	Knight,

	/**
	 * {@link Pawn} promote to {@link Queen}
	 */
	Queen,

	/**
	 * {@link Pawn} promote to {@link Rook}
	 */
	Rook;
}
