package net.chess.chess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.chess.chess.logging.CustomFormatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChessApplication extends Application {
    /**
     * {@link DateTimeFormatter} of Pattern yyyy.MM.dd
     */
    public static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss");

    /**
     * {@link Image} for Application
     */
    public static Image icon_image;

    /**
     * {@link Logger}
     */
    public static final Logger logger;

    /**
     * {@link LocalDateTime} of when the program was executed
     */
    public static final LocalDateTime now = LocalDateTime.now();

    /**
     * Primitive type array of {@link String} holding game modes
     */
    private static final String[] options = { "Standard Game", "Funny Game", "Test" };

    /**
     * File name for Portable Game Notation
     */
    public static final String pgn = "out.pgn";

    /**
     * Output {@link File} for Portable Game Notation
     */
    public static final File pgn_file;

    static {
        FileHandler file = null;
        try {
            file = new FileHandler(String.format("out-%s.log", now.format(format)), false);
        } catch (final SecurityException | IOException e) {
            ChessApplication.logger.throwing("Chess", "static", e);
        }
        assert file != null;
        file.setFormatter(new CustomFormatter());

        logger = Logger.getLogger("Chess");
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
        logger.addHandler(file);

        final File rsc_dir = new File("./src/main/resources/");
        boolean succeeded = false;
        if (!rsc_dir.exists())
            succeeded = rsc_dir.mkdir();
        assert succeeded;

        final String icon_name = "icon.png";
        final URL icon_url = ChessApplication.class.getResource(icon_name);
        if (icon_url == null) {
            ChessApplication.logger.info(icon_name + " does not exist, attempting to download");
            try {
                final File icon_file = new File(rsc_dir, icon_name);
                succeeded = icon_file.getParentFile().mkdirs() && icon_file.createNewFile();
                assert succeeded;
                final URL url = URI.create("https://raw.githubusercontent.com/lai-huy-075/Chess/master/src/resources/icon.png").toURL();
                final ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                final FileOutputStream fout = new FileOutputStream(icon_file);
                fout.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fout.close();
                icon_image = new Image(icon_file.toURI().toString());
            } catch (final IOException ioe) {
                ChessApplication.logger.throwing("Chess", "static", ioe);
            }
        } else {
            icon_image = new Image(icon_url.toString());
            logger.info(icon_name + " loaded from resources.");
        }

        final File pgn_dir = new File("./pgn/");
        if (!pgn_dir.exists())
            succeeded = pgn_dir.mkdir();
        assert succeeded;

        pgn_file = new File("./pgn/" + pgn);
        if (!pgn_file.exists())
            try {
                succeeded = pgn_file.createNewFile();
            } catch (final IOException e) {
                ChessApplication.logger.throwing("Chess", "static", e);
            }
        assert succeeded;

        try {
            ChessApplication.logger.info(pgn_file.getCanonicalPath());
        } catch (final IOException e) {
            ChessApplication.logger.throwing("Chess", "static", e);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChessApplication.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.getIcons().add(ChessApplication.icon_image);
        stage.setTitle("♙");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}