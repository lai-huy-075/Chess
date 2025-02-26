package net.chess.chess;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.chess.chess.board.Mode;
import net.chess.chess.board.Panel;
import net.chess.chess.logging.CustomFormatter;
import net.chess.chess.piece.PieceColor;
import net.chess.chess.player.Player;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChessApplication extends Application {
    public static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    public static final LocalDateTime now = LocalDateTime.now();
    public static final Logger logger;
    public static final String pgn = "game.pgn";
    public static final File pgn_file;
    public static final Image icon = new Image(Objects.requireNonNull(ChessApplication.class.getResourceAsStream("icon.png")));
    private static final int BOARD_SIZE = 8;
    public static final int TILE_SIZE = 80;
    private static final String[] options = {"Standard Game", "Test"};

    static {
        final FileHandler file;
        try {
            file = new FileHandler("./chess.log", false);
            file.setFormatter(new CustomFormatter());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger = Logger.getLogger(ChessApplication.class.getName());
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
        logger.addHandler(file);

        final File pgn_dir = new File("./pgn/");
        if (!pgn_dir.exists())
            pgn_dir.mkdir();

        pgn_file = new File("./pgn/" + pgn);
        if (!pgn_file.exists())
            try {
                pgn_file.createNewFile();
            } catch (final IOException e) {
                ChessApplication.logger.throwing("Chess", "static", e);
            }

        try {
            ChessApplication.logger.info(pgn_file.getCanonicalPath());
        } catch (final IOException e) {
            ChessApplication.logger.throwing("Chess", "static", e);
        }
    }

    @Override
    public void start(Stage stage) {
        stage.setOnCloseRequest(_ -> exitConfirmation(stage));
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options[0], options);
        dialog.setTitle("Welcome to Chess!");
        dialog.setHeaderText("Pick a game mode");
        dialog.setContentText("Mode:");
        dialog.showAndWait().ifPresent(choice -> {
            switch (choice) {
                case "Standard Game":
                    normalMode(stage);
                    break;
                case "Test":
                    testMode(stage);
                    break;
                default:
                    logger.info("Invalid selection");
            }
        });
    }

    private void normalMode(Stage stage) {
        logger.info("Normal Game");
        Panel panel = createPanel(Mode.Normal);
        createGameWindow(stage, panel);
    }

    private void testMode(Stage stage) {
        logger.info("Test Mode");
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PGN files", "*.pgn"));
        final File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Panel panel = new Panel(file);
            createGameWindow(stage, panel);
        }
    }

    private Panel createPanel(final Mode mode) {
        final String whiteName = getPlayerName("White");
        final String blackName = getPlayerName("Black");
        return new Panel(mode, new Player(whiteName, PieceColor.White), new Player(blackName, PieceColor.Black));
    }

    private String getPlayerName(String color) {
        final TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Player " + color);
        dialog.setHeaderText("Enter " + color + " player name");

        Optional<String> result;
        String name;
        do {
            result = dialog.showAndWait();
            name = result.orElse("").trim();
        } while (name.isEmpty() || name.length() > 49);

        return name;
    }

    private void createGameWindow(final Stage stage, final Panel panel) {
        Scene scene = new Scene(panel, TILE_SIZE * (BOARD_SIZE + 2), TILE_SIZE * (BOARD_SIZE + 2));
        stage.setScene(scene);
        stage.setTitle("Chess");
        stage.show();
    }

    private void exitConfirmation(final Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Game");
        alert.setHeaderText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage.close();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
