package main.board;

/**
 * Enumerated type defining the difference between {@link Tile}
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 02 21
 */
public enum TileDifference {
	/**
	 * Both {@link Tile#row} and {@link Tile#col} are different
	 */
	Both,

	/**
	 * Only {@link Tile#col} is different
	 */
	File,

	/**
	 * {@link Tile#row} and {@link Tile#col} are not different
	 */
	None,

	/**
	 * Only {@link Tile#row} is different
	 */
	Rank;
}
