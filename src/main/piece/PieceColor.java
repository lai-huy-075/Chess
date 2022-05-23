package main.piece;

import java.awt.Color;
import java.util.Objects;

/**
 * Enumerated type of the piece colors.
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
