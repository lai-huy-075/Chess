package net.chess.chess.piece;

/**
 * State of the {@link King} being in Check, Checkmate, or Stalemate.<br>
 * Read about Check
 * <a href="https://en.wikipedia.org/wiki/Check_(chess)">here</a>.<br>
 * Read about Checkmate
 * <a href="https://en.wikipedia.org/wiki/Checkmate">here</a>.<br>
 * Read about Stalemate
 * <a href="https://en.wikipedia.org/wiki/Stalemate">here</a>.
 */
public enum CheckState {
	/**
	 * {@link King} is in Check
	 */
	Check,

	/**
	 * {@link King} is not in Check
	 */
	Fail,

	/**
	 * {@link King} is in Checkmate
	 */
	Mate,

	/**
	 * {@link King} has been stalemated
	 */
	Stale;
}
