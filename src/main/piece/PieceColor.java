package main.piece;

import java.awt.Color;

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
     * @param rgb of the color
     */
    PieceColor(final int rgb) {
        this.color = new Color(rgb);
    }

    /**
     * Get the oponent {@link PieceColor}
     * @return oponent PieceColor
     */
    public PieceColor oponent() {
        switch (this) {
            case White:
                return Black;
            case Black:
                return White;
            default:
                throw new IllegalStateException("Illegal PieceColor:\t" + this.name());
        }
    }
}
