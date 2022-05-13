package main;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import main.board.Panel;
import main.listeners.Window;
import main.logging.CustomFormatter;
import main.piece.Piece.PieceColor;
import main.player.Player;

/**
 * Main class
 */
public class Chess {
	/**
	 * {@link String} file name for Forsyth-Edwards Notation 
	 */
	public static final String fen = "out.fen";
	
	/**
	 * Output {@link File} for Forsyth-Edwards Notation
	 */
	public static final File fen_file;
	
	/**
	 * {@link DateTimeFormatter} of Patern yyyy.MM.dd
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

	static {
		FileHandler file = null;
		try {
			file = new FileHandler("./out.log", false);
		} catch (SecurityException e) {
			Chess.logger.throwing("Chess", "static", e);
		} catch (IOException e) {
			Chess.logger.throwing("Chess", "static", e);
		}
		file.setFormatter(new CustomFormatter());

		logger = Logger.getLogger("Chess");
		logger.setLevel(Level.ALL);
		logger.setUseParentHandlers(false);
		logger.addHandler(file);

		icon = new ImageIcon("./src/resources/icon.png");
		icon_image = icon.getImage();

		File pgn_dir = new File("./pgn/");
		if (!pgn_dir.exists())
			pgn_dir.mkdir();

		pgn_file = new File("./pgn/" + pgn);
		if (!pgn_file.exists())
			try {
				pgn_file.createNewFile();
			} catch (IOException e) {
				Chess.logger.throwing("Chess", "static", e);
			}

		try {
			Chess.logger.info(pgn_file.getCanonicalPath());
		} catch (IOException e) {
			Chess.logger.throwing("Chess", "static", e);
		}

		File fen_dir = new File("./fen/");
		if (!fen_dir.exists())
			fen_dir.mkdir();

		fen_file = new File("./fen/" + fen);
		if (!fen_file.exists())
			try {
				fen_file.createNewFile();
			} catch (IOException e) {
				Chess.logger.throwing("Chess", "static", e);
			}

		try {
			Chess.logger.info(fen_file.getCanonicalPath());
		} catch (IOException e) {
			Chess.logger.throwing("Chess", "static", e);
		}
	}
	
	/**
	 * Create a standard game of chess
	 */
	private static final void createChessBoard() {
		logger.info("Normal Game");

		String white_name = JOptionPane.showInputDialog(null, "White, enter you name", "White",
				JOptionPane.INFORMATION_MESSAGE);
		if (white_name == null || white_name.isEmpty())
			return;

		String black_name = JOptionPane.showInputDialog(null, "Black, enter you name", "Black",
				JOptionPane.INFORMATION_MESSAGE);
		if (black_name == null || black_name.isEmpty())
			return;

		Panel panel = new Panel(new Player(white_name, PieceColor.White), new Player(black_name, PieceColor.Black));

		createFrame(panel);
	}
	
	/**
	 * Display the Main Menu
	 */
	private static final void mainMenu() {
		switch (JOptionPane.showOptionDialog(null, "Pick an option", "Welcome to Chess!", JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, icon, new String[] { "New Standard Game", "Funny Game", "Place pieces" },
				"New Standard Game")) {
		case 0:
			createChessBoard();
			return;
		case 1:
			funny();
			return;
		case 2:
			placePieces();
			return;
		default:
			Chess.logger.info("Picked Illegal Option");
			return;
		}
	}
	
	/**
	 * Create a game of Chess with altered rules
	 */
	private static final void funny() {
		Chess.logger.info("Funny Mode");
		Panel panel = new Panel(Panel.Mode.Funny);

		createFrame(panel);
	}
	
	/**
	 * Create a game of Chess to allow the user to Place Pieces
	 */
	private static final void placePieces() {
		Chess.logger.info("Debug Mode");
		Panel panel = new Panel(Panel.Mode.Debug);

		createFrame(panel);
	}
	
	/**
	 * Create a {@link JFrame} and display it. 
	 * 
	 * @param panel {@link Panel} to add to created JFrame
	 */
	private static final void createFrame(Panel panel) {
		Chess.logger.info("Creating JFrame");
		JFrame frame = new JFrame("Chess");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setIconImage(icon_image);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addWindowListener(new Window(panel));
	}
	
	/**
	 * Main function
	 * @param args primitive type array of {@link String}
	 */
	public static void main(String[] args) {
		Chess.logger.info("Args:\t" + Arrays.deepToString(args));
		mainMenu();
	}
}
