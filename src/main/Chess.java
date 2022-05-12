package main;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

public class Chess {
	public static final String fen = "out.fen";
	public static final File fen_file;
	public static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy.MM.dd");
	public static final ImageIcon icon;
	public static final Image icon_image;
	public static final Logger logger;
	public static final LocalDateTime now = LocalDateTime.now();
	public static final String pgn = "out.pgn";
	public static final File pgn_file;

	static {
		FileHandler file = null;
		try {
			file = new FileHandler("./out.log", false);
		} catch (SecurityException e) {
			Chess.logger.severe(e.getLocalizedMessage());
		} catch (IOException e) {
			Chess.logger.severe(e.getLocalizedMessage());
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
				Chess.logger.severe(e.getLocalizedMessage());
			}

		try {
			Chess.logger.info(pgn_file.getCanonicalPath());
		} catch (IOException e) {
			Chess.logger.severe(e.getLocalizedMessage());
		}

		File fen_dir = new File("./fen/");
		if (!fen_dir.exists())
			fen_dir.mkdir();

		fen_file = new File("./fen/" + fen);
		if (!fen_file.exists())
			try {
				fen_file.createNewFile();
			} catch (IOException e) {
				Chess.logger.severe(e.getLocalizedMessage());
			}

		try {
			Chess.logger.info(fen_file.getCanonicalPath());
		} catch (IOException e) {
			for (StackTraceElement element : e.getStackTrace())
				Chess.logger.severe(element.toString());
		}
	}

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

	private static final void funny() {
		Chess.logger.info("Funny Mode");
		Panel panel = new Panel(Panel.Mode.Funny);

		createFrame(panel);
	}

	private static final void placePieces() {
		logger.info("Debug Mode");
		Panel panel = new Panel(Panel.Mode.Debug);

		createFrame(panel);
	}

	private static final void createFrame(Panel panel) {
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

	public static void main(String[] args) {
		mainMenu();
	}
}
