package net.chess.chess.board;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

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
    Dark(new Background(new BackgroundFill(Color.rgb(0x73, 0x46, 0x3c), CornerRadii.EMPTY, Insets.EMPTY)),
			new Background(new BackgroundFill(Color.rgb(0xe0, 0x69, 0x54), CornerRadii.EMPTY, Insets.EMPTY))),

    /**
     * Light Squares
     */
    Light(new Background(new BackgroundFill(Color.rgb(0xc8, 0x96, 0x69), CornerRadii.EMPTY, Insets.EMPTY)),
			new Background(new BackgroundFill(Color.rgb(0xec, 0x79, 0x64), CornerRadii.EMPTY, Insets.EMPTY)));

    /**
     * {@link Background} displayed when selected by {@link javafx.scene.input.MouseButton#SECONDARY}
     */
    public final Background selected;

    /**
     * {@link Background} displayed when not selected
     */
    public final Background standard;

    /**
     * Constructor
     *
     * @param standard {@link Color} of Tile when not selected
     * @param selected {@link Color} of Tile when selected
     */
    TileColor(final Background standard, final Background selected) {
        this.standard = Objects.requireNonNull(standard, "Standard Color cannot be null");
        this.selected = Objects.requireNonNull(selected, "Selected Color cannot be null");
    }
}
