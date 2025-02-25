package net.chess.chess.listeners;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import net.chess.chess.ChessApplication;
import net.chess.chess.board.Panel;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * {@link EventHandler} for key events
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public class Keys implements EventHandler<KeyEvent> {
    /**
     * {@link Panel} holding all {@link javafx.scene.Node}
     */
    public final Panel panel;

    /**
     * Constructor
     *
     * @param panel {@link Panel}
     */
    public Keys(final Panel panel) {
        this.panel = Objects.requireNonNull(panel, "Panel cannot be null.");
    }

    private void keyPressed(final @NotNull KeyEvent e) {
        ChessApplication.logger.info("Key pressed: " + e.getCode());
        switch (e.getCode()) {
            case KeyCode.UP -> {
                ChessApplication.logger.info("Up arrow");
                this.panel.board.loadInitialPosition();
            }
            case KeyCode.DOWN -> {
                ChessApplication.logger.info("Down arrow");
                this.panel.board.loadLastPosition();
            }
            case KeyCode.LEFT -> {
                ChessApplication.logger.info("Left arrow");
                this.panel.board.loadPreviousPosition();
            }
            case KeyCode.RIGHT -> {
                this.panel.board.loadNextPosition();
                ChessApplication.logger.info("Right arrow");
            }
            case KeyCode.ESCAPE -> this.panel.displayMenu();
            case KeyCode.C -> this.panel.controlsOption();
            case KeyCode.D -> this.panel.drawOption();
            case KeyCode.E -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Piece deselected", ButtonType.OK);
                alert.showAndWait();
                this.panel.board.resetTiles();
            }
            case KeyCode.F -> this.panel.resignOption();
            case KeyCode.Q -> this.panel.quitOption();
            case KeyCode.R -> this.panel.resetOption();
            case KeyCode.S -> this.panel.scoresOption();
        }
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        EventType<? extends KeyEvent> eventType = keyEvent.getEventType();
        if (eventType == null)
            return;

        this.keyPressed(keyEvent);
    }
}
