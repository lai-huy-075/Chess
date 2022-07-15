package main.piece;

/**
 * Enumerated type to determine Piece type
 * 
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 03 15
 */
public enum PieceType {
	King('\u2654', '\u265A', 'K'), Queen('\u2655', '\u265B', 'Q'), Rook('\u2656', '\u265C', 'R'),
	Bishop('\u2657', '\u265D', 'B'), Knight('\u2658', '\u265E', 'N'), Pawn('\u2659', '\u265F', '\0');

	public final char white;
	public final char black;
	public final char an;

	private PieceType(char white, char black, char an) {
		this.white = white;
		this.black = black;
		this.an = an;
	}
}
