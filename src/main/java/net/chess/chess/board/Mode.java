package net.chess.chess.board;

/**
 * Game mode
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public enum Mode {
	/**
	 * Normal rules. Allows user to place pieces
	 */
	Debug,

	/**
	 * Funny rules
	 */
	Funny,

	/**
	 * Normal rules
	 */
	Normal,

	/**
	 * Game is complete
	 */
	Over;
}