package main;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
 * @version 2022 05 23
 */
public class Chess {
	private static final String[] options = { "Standard Game", "Funny Game", "Test" };
	
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
	 * {@link Logger}
	 */
	public static final Logger logger;

	/**
	 * {@link LocalDateTime} of when the program was executed
	 */
	public static final LocalDateTime now = LocalDateTime.now();

	/**
	 * File name for Portable Game Notation
	 */
	public static final String pgn = "out.pgn";

	/**
	 * Output {@link File} for Portable Game Notation
	 */
	public static final File pgn_file;
	
	/**
	 * {@link File} in local directory
	 */
	private static final File local = new File("./");

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

		String icon_name = "./src/resources/icon.png";
		File icon_file = new File(icon_name);
		if (!icon_file.exists()) {
			Chess.logger.info(icon_file.getName() + " created");
			try {
				icon_file.mkdirs();
				icon_file.createNewFile();
				URL url = new URL(
						"https://raw.githubusercontent.com/MrPineapple065/Chess/master/src/resources/icon.png");
				ReadableByteChannel rbc = Channels.newChannel(url.openStream());
				FileOutputStream fout = new FileOutputStream(icon_file);
				fout.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				fout.close();
			} catch (IOException ioe) {
				Chess.logger.throwing("Chess", "static", ioe);
			}

		} else {
			Chess.logger.info(icon_file.getName() + " exists");
		}
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
	private static final void createChessBoard() {
		logger.info("Normal Game");

		String white_name = (String) JOptionPane.showInputDialog(null, "White, enter you name", "White",
				JOptionPane.INFORMATION_MESSAGE, Chess.icon, null, null);

		if (white_name == null || white_name.isEmpty())
			return;

		while (white_name.length() > 49) {
			white_name = (String) JOptionPane.showInputDialog(null, "Name is too long. Try Again", "White",
					JOptionPane.INFORMATION_MESSAGE, Chess.icon, null, null);

			if (white_name == null)
				return;

			if (white_name.isEmpty())
				return;
		}

		String black_name = (String) JOptionPane.showInputDialog(null, "Black, enter you name", "Black",
				JOptionPane.INFORMATION_MESSAGE, Chess.icon, null, null);

		if (black_name == null || black_name.isEmpty())
			return;

		while (black_name.length() > 49) {
			black_name = (String) JOptionPane.showInputDialog(null, "Name is too long. Try Again", "White",
					JOptionPane.INFORMATION_MESSAGE, Chess.icon, null, null);
			if (black_name == null)
				return;

			if (black_name.isEmpty())
				return;
		}

		final Panel panel = new Panel(new Player(white_name, PieceColor.White),
				new Player(black_name, PieceColor.Black));

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

	/**
	 * Create a game of Chess with altered rules
	 */
	private static final void funny() {
		Chess.logger.info("Funny Mode");
		final Panel panel = new Panel(Mode.Funny);

		final JFrame frame = createFrame(panel);
		frame.dispose();
		;
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
			createChessBoard();
			return;
		case 1:
			funny();
			return;
		case 2:
			test();
			return;
		default:
			Chess.logger.info("Picked Illegal Option");
			return;
		}
	}

	/**
	 * Create a game of Chess to allow the user to Place Pieces
	 */
	private static final void test() {
		Chess.logger.info("Test Mode");
		final Panel panel = new Panel(Mode.Debug);
		final JFrame frame = createFrame(panel);
		JFileChooser fc = new JFileChooser(local);
		fc.showOpenDialog(panel);
		
		final File file = fc.getSelectedFile();
		final String name = file.getName(), extension;
		final int i = name.lastIndexOf('.');
		extension = i > 0 ? name.substring(i + 1) : "";
		if (!extension.equals("pgn")) {
			Chess.logger.info("Incorrect file type:\t" + extension);
			frame.dispose();
			return;
		}
		
		
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
			String data = "";
			int c = reader.read();
			while (c != -1) {
				data += (char) c;
				c = reader.read();
			}
			Chess.logger.info(data);
		} catch (FileNotFoundException fnfe) {
			Chess.logger.throwing("Chess", "test", fnfe);
			frame.dispose();
			return;
		} catch (IOException ioe) {
			Chess.logger.throwing("Chess", "test", ioe);
			frame.dispose();
			return;
		} catch (NullPointerException npe) {
			Chess.logger.throwing("Chess", "test", npe);
			frame.dispose();
			return;
		}
	}
}
