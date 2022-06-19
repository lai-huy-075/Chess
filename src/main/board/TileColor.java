package main.board;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * {@link Color} for each tile
 * 
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public enum TileColor {
    /**
     * Dark Squares
     */
    Dark(new Color(0x73463c), new Color(0xe06954)),

    /**
     * Light Squares
     */
    Light(new Color(0xc89669), new Color(0xec7964));

    /**
     * {@link Color} displayed when selected by {@link MouseEvent#BUTTON3}
     */
    public final Color selected;

    /**
     * {@link Color} displayed when not selected
     */
    public final Color standard;

    /**
     * Constructor
     *
     * @param standard {@link Color} of Tile when not selected
     * @param selected {@link Color} of Tile when selected
     */
    TileColor(final Color standard, final Color selected) {
        this.standard = Objects.requireNonNull(standard, "Standard Color cannot be null");
        this.selected = Objects.requireNonNull(selected, "Selected Color cannot be null");
    }
}
