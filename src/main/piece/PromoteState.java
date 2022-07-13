package main.piece;

/**
 * Promote state of the {@link Pawn}
 * 
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 */
public enum PromoteState {
	/**
	 * {@link Pawn} promote to {@link Queen}
	 */
	Queen,

	/**
	 * {@link Pawn} promote to {@link Knight}
	 */
	Knight,

	/**
	 * {@link Pawn} promote to {@link Rook}
	 */
	Rook,

	/**
	 * {@link Pawn} promote to {@link Bishop}
	 */
	Bishop,

	/**
	 * {@link Pawn} did not promote
	 */
	Fail;
}
