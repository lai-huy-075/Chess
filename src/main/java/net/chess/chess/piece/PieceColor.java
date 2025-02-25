package net.chess.chess.piece;

import javafx.scene.paint.Color;

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
    Black(0x2c, 0x2c, 0x2c),

    /**
     * The White Pieces
     */
    White(0xd3, 0xd3, 0xd3);

    /**
     * {@link Color} of this
     */
    public final Color color;

    /**
     * Constructor
     *
     * @param red   red value
     * @param green green value
     * @param blue  blue value
     */
    PieceColor(final int red, final int green, final int blue) {
        this.color = Color.rgb(red, green, blue);
    }

    /**
     * Get the opponent {@link PieceColor}
     *
     * @return opponent {@link PieceColor}
     */
    public PieceColor opponent() {
        return switch (this) {
            case Black -> White;
            case White -> Black;
        };
    }
}