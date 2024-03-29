package main;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.board.Mode;
import main.board.Panel;
import main.listeners.Window;
import main.logging.CustomFormatter;
import main.piece.PieceColor;
import main.player.Player;

/**
 * Main class
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 06 14
 */
public class Chess {
	/**
	 * {@link DateTimeFormatter} of Pattern yyyy.MM.dd
	 */
	public static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy.MM.dd");

	/**
	 * {@link ImageIcon} for the {@link JFrame}
	 */
	public static final ImageIcon icon;

	/**
	 * {@link Image} of {@link #icon}
	 */
	public static final Image icon_image;

	/**
	 * {@link File} in local directory
	 */
	private static final File local = new File("./");

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

	/**
	 * Creates logger, icon, and files for the program
	 */
	static {
		FileHandler file = null;
		try {
			file = new FileHandler("./out.log", false);
		} catch (final SecurityException e) {
			Chess.logger.throwing("Chess", "static", e);
		} catch (final IOException e) {
			Chess.logger.throwing("Chess", "static", e);
		}
		file.setFormatter(new CustomFormatter());

		logger = Logger.getLogger("Chess");
		logger.setLevel(Level.ALL);
		logger.setUseParentHandlers(false);
		logger.addHandler(file);

		final File rsc_dir = new File("./src/resources/");
		if (!rsc_dir.exists())
			rsc_dir.mkdir();

		final String icon_name = "./src/resources/icon.png";
		final File icon_file = new File(icon_name);
		if (!icon_file.exists()) {
			Chess.logger.info(icon_file.getName() + " created");
			try {
				icon_file.createNewFile();
				final URL url = new URL(
						"https://raw.githubusercontent.com/MrPineapple065/Chess/master/src/resources/icon.png");
				final ReadableByteChannel rbc = Channels.newChannel(url.openStream());
				final FileOutputStream fout = new FileOutputStream(icon_file);
				fout.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				fout.close();
			} catch (final IOException ioe) {
				Chess.logger.throwing("Chess", "static", ioe);
			}
		} else
			Chess.logger.info(icon_file.getName() + " exists");
		icon = new ImageIcon(icon_name);
		icon_image = icon.getImage();

		final File pgn_dir = new File("./pgn/");
		if (!pgn_dir.exists())
			pgn_dir.mkdir();

		pgn_file = new File("./pgn/" + pgn);
		if (!pgn_file.exists())
			try {
				pgn_file.createNewFile();
			} catch (final IOException e) {
				Chess.logger.throwing("Chess", "static", e);
			}

		try {
			Chess.logger.info(pgn_file.getCanonicalPath());
		} catch (final IOException e) {
			Chess.logger.throwing("Chess", "static", e);
		}
	}

	/**
	 * Create a standard game of chess
	 */
	private static final void normalMode() {
		logger.info("Normal Game\n");

		final Panel panel;
		try {
			panel = createPanel(Mode.Normal);
		} catch (InstantiationException ie) {
			Chess.logger.throwing("Chess", "normalMode", ie);
			return;
		}
		createFrame(panel);
	}

	/**
	 * Create a {@link JFrame} and display it.
	 *
	 * @param panel {@link Panel} to add to created JFrame
	 *
	 * @return {@link JFrame} created
	 */
	private static final JFrame createFrame(final Panel panel) {
		Chess.logger.info("Creating JFrame");

		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		final JFrame frame = new JFrame("Chess");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setIconImage(icon_image);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addWindowListener(new Window(panel));

		return frame;
	}

	private static final Panel createPanel(Mode mode) throws InstantiationException {
		String white_name = (String) JOptionPane.showInputDialog(null, "White, enter you name", "White",
				JOptionPane.INFORMATION_MESSAGE, Chess.icon, null, null);

		if (white_name == null || white_name.isEmpty())
			throw new InstantiationException("White's name is empty");

		while (white_name.length() > 49) {
			white_name = (String) JOptionPane.showInputDialog(null, "Name is too long. Try Again", "White",
					JOptionPane.INFORMATION_MESSAGE, Chess.icon, null, null);

			if ((white_name == null) || white_name.isEmpty())
				throw new InstantiationException("White's name is empty");
		}

		String black_name = (String) JOptionPane.showInputDialog(null, "Black, enter you name", "Black",
				JOptionPane.INFORMATION_MESSAGE, Chess.icon, null, null);

		if (black_name == null || black_name.isEmpty())
			throw new InstantiationException("Black's name is empty");

		while (black_name.length() > 49) {
			black_name = (String) JOptionPane.showInputDialog(null, "Name is too long. Try Again", "White",
					JOptionPane.INFORMATION_MESSAGE, Chess.icon, null, null);
			if ((black_name == null) || black_name.isEmpty())
				throw new InstantiationException("Black's name is empty");
		}

		return new Panel(Mode.Normal, new Player(white_name, PieceColor.White),
				new Player(black_name, PieceColor.Black));
	}

	/**
	 * Create a game of Chess with altered rules
	 */
	private static final void funnyMode() {
		Chess.logger.info("Funny Mode\n");

		final Panel panel;
		try {
			panel = createPanel(Mode.Normal);
		} catch (InstantiationException ie) {
			Chess.logger.throwing("Chess", "funnyMode", ie);
			return;
		}
		final JFrame frame = createFrame(panel);
		frame.dispose();
	}

	/**
	 * Main function
	 *
	 * @param args primitive type array of {@link String}
	 */
	public static void main(final String[] args) {
		Chess.logger.info("Args:\t" + Arrays.deepToString(args));
		mainMenu();
	}

	/**
	 * Display the Main Menu
	 */
	private static final void mainMenu() {
		switch (JOptionPane.showOptionDialog(null, "Pick an option", "Welcome to Chess!", JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, icon, options, options[0])) {
		case 0:
			normalMode();
			return;
		case 1:
			funnyMode();
			return;
		case 2:
			testMode();
			return;
		default:
			Chess.logger.info("Picked Illegal Option");
			return;
		}
	}

	/**
	 * Create a game of Chess to allow the user to Place Pieces
	 */
	private static final void testMode() {
		Chess.logger.info("Test Mode\n");
		final JFileChooser fc = new JFileChooser(local);
		fc.setFileFilter(new FileNameExtensionFilter("PGN files", "pgn"));
		fc.showOpenDialog(fc);

		final File file = fc.getSelectedFile();
		final Panel panel = new Panel(file);
		createFrame(panel);
	}

	/**
	 * Constructor
	 */
	public Chess() {
	}
}
