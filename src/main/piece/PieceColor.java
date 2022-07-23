package main.piece;

import java.awt.Color;

/**
 * Enumerated type of the Piece Color
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public enum PieceColor {
	/**
	 * The Black Pieces
	 */
	Black(0x2c2c2c),

	/**
	 * The White Pieces
	 */
	White(0xd3d3d3);

	/**
	 * {@link Color} of this
	 */
	public final Color color;

	/**
	 * Constructor
	 *
	 * @param rgb value of {@link #color} as an int
	 */
	private PieceColor(final int rgb) {
		this.color = new Color(rgb);
	}

	/**
	 * Get the opponent {@link PieceColor}
	 *
	 * @return opponent {@link PieceColor}
	 */
	public PieceColor opponent() {
		switch (this) {
		case Black:
			return White;
		case White:
			return Black;
		default:
			throw new IllegalStateException("Illegal PieceColor:\t" + this.name());
		}
	}
}