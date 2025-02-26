package net.chess.chess.listeners;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.chess.chess.ChessApplication;
import net.chess.chess.board.Chessboard;
import net.chess.chess.board.Tile;
import net.chess.chess.board.TileColor;

import java.util.Objects;

import static javafx.scene.input.MouseEvent.*;

/**
 * {@link EventHandler} for each {@link Tile}
 *
 * @param board {@link Chessboard} the {@link Tile} is on
 * @param tile  {@link Tile} this is linked to
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public record Mouse(Chessboard board, Tile tile) implements EventHandler<MouseEvent> {
    /**
     * {@link Border} to set when {@link #mouseEntered(MouseEvent)} is called
     */
    private static final Border border = new Border(new BorderStroke(Color.rgb(0x33, 0x33, 0x33), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));

    /**
     * Constructor
     *
     * @param board {@link Chessboard} to interact with
     * @param tile  {@link Tile} to interact with
     */
    public Mouse(final Chessboard board, final Tile tile) {
        this.board = Objects.requireNonNull(board, "Panel cannot be null");
        this.tile = Objects.requireNonNull(tile, "Tile cannot be null");
    }

    public void mouseClicked(final MouseEvent e) {
        switch (e.getButton()) {
            case MouseButton.PRIMARY:
                this.board.tileClicked();
                return;
            case MouseButton.SECONDARY:
                switch (this.tile.color) {
                    case Dark:
                        this.tile.setBackground(TileColor.Dark.standard.equals(this.tile.getBackground()) ? TileColor.Dark.selected : TileColor.Dark.standard);
                        return;
                    case Light:
                        this.tile.setBackground(
                                TileColor.Light.standard.equals(this.tile.getBackground()) ? TileColor.Light.selected
                                        : TileColor.Light.standard);
                        return;
                    default:
                        throw new IllegalStateException("Illegal Tile.TileColor:\t" + this.tile.color.name());
                }
            default:
        }
    }

    public void mouseEntered(final MouseEvent e) {
        this.tile.setBorder(border);
    }

    public void mouseExited(final MouseEvent e) {
        this.tile.setBorder(null);
    }

    public void mousePressed(final MouseEvent e) {
        switch (this.board.getMode()) {
            case Debug, Over:
                return;
            case Funny, Normal:
                break;
            default:
                throw new IllegalStateException("Illegal Mode:\t" + this.board.getMode());
        }

        if (Objects.requireNonNull(e.getButton()) != MouseButton.PRIMARY) {
            return;
        }

        ChessApplication.logger.info("Pressed :\t" + this.tile);
        this.board.updateSource(this.tile);
    }

    public void mouseReleased(final MouseEvent e) {
        switch (this.board.getMode()) {
            case Debug, Over:
                return;
            case Funny, Normal:
                break;
            default:
                throw new IllegalStateException("Illegal Mode:\t" + this.board.getMode());
        }

        if (Objects.requireNonNull(e.getButton()) != MouseButton.PRIMARY) {
            return;
        }

        final int x = Math.floorDiv((int) e.getX(), ChessApplication.TILE_SIZE);
        final int y = Math.floorDiv((int) e.getY(), ChessApplication.TILE_SIZE);
        ChessApplication.logger.info("Offset:\t(" + x + "," + y + ")");
        final Tile t;
        try {
            t = this.board.getTileOffset(this.tile, x, y);
            ChessApplication.logger.info("Released:\t" + t.toString() + "\n");
        } catch (final ArrayIndexOutOfBoundsException aioobe) {
            ChessApplication.logger.info("Released:\tOut of Bounds.");
            return;
        }

        if (!this.board.compareSource(t)) {
            this.board.updateDestination(t);
            this.board.tileClicked();
        }
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
        if (eventType == null) {
            return;
        } else if (eventType == MOUSE_CLICKED) {
            this.mouseClicked(mouseEvent);
        } else if (eventType == MOUSE_ENTERED) {
            this.mouseEntered(mouseEvent);
        } else if (eventType == MOUSE_EXITED) {
            this.mouseExited(mouseEvent);
        } else if (eventType == MOUSE_PRESSED) {
            this.mousePressed(mouseEvent);
        } else if (eventType == MOUSE_RELEASED) {
            this.mouseReleased(mouseEvent);
        }
    }
}
