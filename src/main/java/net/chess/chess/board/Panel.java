package net.chess.chess.board;

import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.chess.chess.ChessApplication;
import net.chess.chess.file.PGNReader;
import net.chess.chess.listeners.Keys;
import net.chess.chess.listeners.Mouse;
import net.chess.chess.piece.PieceColor;
import net.chess.chess.player.Player;

import java.io.File;
import java.text.ParseException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static net.chess.chess.ChessApplication.TILE_SIZE;

/**
 * All GUI elements
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public final class Panel extends Pane {
    /**
     * Arial {@link Font}
     */
    public static final Font arial = new Font("Arial", 30);

    /**
     * Primitive type array of {@link String} holding column names
     */
    private static final String[] columns = {"a", "b", "c", "d", "e", "f", "g", "h"};

    /**
     * {@link Map} mapping {@link #options} to their respective keyboard shortcuts
     */
    private static final Map<String, String> controlMap = Map.ofEntries(
            Map.entry("Pause", "Escape"),
            Map.entry("Scores", "s"),
            Map.entry("Reset", "r"),
            Map.entry("Resign", "q"),
            Map.entry("Draw", "d"),
            Map.entry("Quit", "q"),
            Map.entry("Controls", "c"),
            Map.entry("Deselect Piece", "e")
    );

    /**
     * {@link String} to display keyboard shortcuts
     */
    public static final String controls;

    /**
     * Grid Layout
     */
    private static final GridPane grid = new GridPane();

    /**
     * {@link Button} that handles displaying the menu
     */
    private static final Button menuButton;

    /**
     * Primitive type array of {@link String} to pick from when
     * {@link #displayMenu()} is called
     */
    private static final String[] options = {"Controls", "Reset", "Resign", "Draw", "Quit", "Deselect Piece",
            "Scores"};

    /**
     * Primitive type array of {@link String} holding rank names
     */
    private static final String[] rows = {"1", "2", "3", "4", "5", "6", "7", "8"};

    /**
     * Versus {@link Label}
     */
    private static final Label vs = new Label("vs");

    static {
        StringBuilder cont = new StringBuilder();
        for (final String key : controlMap.keySet())
            cont.append(key).append("\t").append(controlMap.get(key)).append("\n");

        vs.setFont(arial);
        vs.setPrefSize(TILE_SIZE, TILE_SIZE);
        vs.setTextFill(Color.BLACK);

        controls = cont.toString();

        menuButton = new Button("\u2261");
        menuButton.setFont(new Font("Arial", 60));
        menuButton.setVisible(true);
        menuButton.setPrefSize(TILE_SIZE, TILE_SIZE);
    }

    /**
     * {@link Chessboard} on this
     */
    public final Chessboard board;

    /**
     * {@link Keys} listener
     */
    public final Keys keys;

    /**
     * Current {@link Mode} of gameplay
     */
    public final Mode mode;

    /**
     * Load a game from a {@link File}
     *
     * @param data PGN {@link File}.
     */
    public Panel(final File data) {
        Objects.requireNonNull(data, "Data file cannot be null");
        if (!data.exists())
            throw new IllegalArgumentException("Data file does not exist");

        this.mode = Mode.Debug;
        final PGNReader reader = new PGNReader(data);
        reader.read();

        this.board = new Chessboard(this.mode, reader.getWhite(), reader.getBlack());
        this.keys = new Keys(this);
        try {
            this.board.setResult(reader.getResult());
            this.board.loadMoves(reader.getMoves());
        } catch (final ParseException e) {
            ChessApplication.logger.throwing("Panel", "Constructor", e);
        }

        this.init();
    }

    /**
     * Constructor
     *
     * @param mode  {@link Mode} of the game
     * @param white {@link PieceColor#White} {@link Player}
     * @param black {@link PieceColor#Black} {@link Player}
     */
    public Panel(final Mode mode, final Player white, final Player black) {
        this.mode = Objects.requireNonNull(mode);
        this.board = new Chessboard(this.mode, white, black);
        this.keys = new Keys(this);

        this.init();
    }

    /**
     * Display the controls of the game.
     */
    public void controlsOption() {
        ChessApplication.logger.info("Display Controls Option");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, controls, ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * Create {@link Label} to add to the {@link net.chess.chess.board.Panel}.
     */
    private void createLabels() {
        final Label white = new Label(this.board.white.name);
        white.setFont(new Font("", 20 - 3 * (int) Math.floor(this.board.white.name.length() / 12.5f)));

        final Label black = new Label(this.board.black.name);
        black.setFont(new Font("", 20 - 3 * (int) Math.floor(this.board.black.name.length() / 12.5f)));

        grid.add(white, 0, 0, 3, 1);
        grid.add(vs, 4, 0, 2, 1);
        grid.add(black, 6, 0, 3, 1);
    }

    /**
     * Adds {@link Tile} to this.
     */
    private void createTiles() {
        final Tile[][] board = this.board.getBoard();
        for (int i = 0, num = columns.length - 1; i < board.length; ++i) {
            final Label col = new Label(rows[num]);
            col.setPrefSize(TILE_SIZE, TILE_SIZE);
            --num;
            grid.add(col, 0, i + 1, 1, 1);

            for (int j = 0; j < board[i].length; ++j) {
                final Tile tile = board[i][j];
                tile.addEventHandler(KeyEvent.KEY_PRESSED, this.keys);
                tile.addEventHandler(MouseEvent.ANY, new Mouse(this.board, tile));
                grid.add(tile, j + 1, i + 1, 1, 1);
            }
        }

        for (int i = 0; i < columns.length; ++i) {
            final Label col = new Label(columns[i]);
            col.setPrefSize(TILE_SIZE, TILE_SIZE);
            grid.add(col, i + 1, 0, 1, 1);
        }

        menuButton.addEventHandler(KeyEvent.KEY_PRESSED, this.keys);
        menuButton.addEventHandler(MouseEvent.MOUSE_CLICKED, _ -> {
            this.displayMenu();
        });
        grid.add(menuButton, 0, 9, 1, 1);
        final Label blank = new Label("");
        blank.setPrefSize(TILE_SIZE, TILE_SIZE);
        grid.add(blank, 1, 9, 8, 1);
    }

    /**
     * Display menu
     */
    public void displayMenu() {
        ChessApplication.logger.info("Display Menu");
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options[0], options);
        dialog.setTitle("Menu");
        dialog.setContentText("Pick an option");
        final Optional<String> result = dialog.showAndWait();
        result.ifPresent(selection -> {
            ChessApplication.logger.info(selection + " selected");
            switch (selection) {
                case "Controls":
                    this.controlsOption();
                    break;
                case "Reset":
                    this.resetOption();
                    break;
                case "Resign":
                    this.resignOption();
                    break;
                case "Draw":
                    this.drawOption();
                    break;
                case "Quit":
                    this.quitOption();
                    break;
                case "Deselect Piece":
                    this.board.resetTiles();
                    break;
                case "Scores":
                    this.scoresOption();
            }
        });
    }

    /**
     * Draw the game
     */
    public void drawOption() {
        ChessApplication.logger.info("Draw offered.");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to accept the draw offer?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            ChessApplication.logger.info("Draw accepted.");
            this.board.draw();
            return;
        }

        ChessApplication.logger.info("Draw declined.");
    }

    /**
     * Initialize stuff. &#x1F60A;
     */
    private void init() {
        // Set Default GUI Elements
        this.setDefaultGUIElements();

        // Creates other GUI elements
        this.createLabels();
        this.createTiles();
        this.getChildren().add(grid);
    }

    /**
     * Quit the game.
     */
    public void quitOption() {
        ChessApplication.logger.info("Quit offered.");
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to quit?", ButtonType.YES, ButtonType.NO);
        final Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            ChessApplication.logger.info("Quit accepted.");
            this.getChildren().clear();
            this.board.quit();
            return;
        }

        ChessApplication.logger.info("Quit declined.");
    }

    /**
     * Reset {@link #board}.
     */
    public void resetOption() {
        ChessApplication.logger.info("Reset offered.");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to reset?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            ChessApplication.logger.info("Reset accepted.");
            this.board.reset();
            return;
        }

        ChessApplication.logger.info("Reset declined.");
    }

    /**
     * A {@link Player} has resigned.
     */
    public void resignOption() {
        if (this.board.isGameOver())
            return;

        ChessApplication.logger.info("Resign offered");
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to accept the resignation?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            ChessApplication.logger.info("Resign accepted");
            this.board.resign();
            Alert win = new Alert(Alert.AlertType.INFORMATION, this.board.getNextPlayer().name + " wins!", ButtonType.OK);
            win.showAndWait();
            return;
        }

        ChessApplication.logger.info("Resign declined");
    }

    /**
     * Display the current scores of the {@link Player}
     */
    public void scoresOption() {
        ChessApplication.logger.info("Display Scores");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Scores", ButtonType.OK);
        alert.setContentText(String.format("%s%n%s", this.board.white.toString(), this.board.black.toString()));
    }

    /**
     * Set default GUI elements using
     */
    private void setDefaultGUIElements() {
    }
}
