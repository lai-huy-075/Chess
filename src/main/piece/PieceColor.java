package main.piece;

import java.awt.Color;
import java.util.Objects;

/**
 * Enumerated type of the piece colors.
 * 
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public enum PieceColor {
    /**
     * The Black Pieces
     */
    Black(new Color(0x2c2c2c)),

    /**
     * The White Pieces
     */
    White(new Color(0xd3d3d3));

    /**
     * {@link Color} of this
     */
    public final Color color;

    /**
     * Constructor
     * 
     * @param color {@link Color} of this piece
     */
    PieceColor(final Color color) {
	this.color = Objects.requireNonNull(color, "Color cannot be null");
    }
}
