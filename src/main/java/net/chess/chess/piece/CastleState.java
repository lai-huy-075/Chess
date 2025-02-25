package net.chess.chess.piece;

/**
 * Result of a Castle made by the {@link King}<br>
 * Read more <a href="https://en.wikipedia.org/wiki/Castling">here</a>.
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public enum CastleState {
	/**
	 * If the {@link King} has attempted an invalid castle
	 */
	Fail,

	/**
	 * If the {@link King} has completed a valid king-side castle
	 */
	Kingside,

	/**
	 * If the {@link King} has completed a valid queen-side castle
	 */
	Queenside,

	/**
	 * If the {@link King} did not castle.
	 */
	Unattempted;
}
