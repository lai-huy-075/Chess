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
	public static final Image icon_image;
	public static final LocalDateTime now = LocalDateTime.now();
	public static final String pgn = "out.pgn";
	public static final File pgn_file;
	
	public static final Logger logger;

	static {
		FileHandler file = null;
		try {
			file = new FileHandler("./out.log", false);
		} catch (SecurityException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		file.setFormatter(new CustomFormatter());
		
		logger = Logger.getLogger("Chess");
		logger.setLevel(Level.ALL);
		logger.setUseParentHandlers(false);
		logger.addHandler(file);
		
		icon_image = new ImageIcon("./src/resources/icon.png").getImage();

		File pgn_dir = new File("./pgn/");
		if (!pgn_dir.exists())
			pgn_dir.mkdir();

		pgn_file = new File("./pgn/" + pgn);
		if (!pgn_file.exists())
			try {
				pgn_file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		try {
			Chess.logger.info(pgn_file.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

		File fen_dir = new File("./fen/");
		if (!fen_dir.exists())
			fen_dir.mkdir();

		fen_file = new File("./fen/" + fen);
		if (!fen_file.exists())
			try {
				fen_file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		try {
			Chess.logger.info(fen_file.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final void createChessBoard() {
		String white_name = JOptionPane.showInputDialog(null, "White, enter you name", "White", JOptionPane.INFORMATION_MESSAGE);
		if (white_name == null || white_name.isEmpty())
			return;
		
		String black_name = JOptionPane.showInputDialog(null, "Black, enter you name", "Black", JOptionPane.INFORMATION_MESSAGE);
		if (black_name == null || black_name.isEmpty())
			return;

		Panel panel = new Panel(new Player(white_name, PieceColor.White), new Player(black_name, PieceColor.Black));

		JFrame frame = new JFrame("Chess");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setIconImage(icon_image);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addWindowListener(new Window());
	}

	public static void main(String[] args) {
		createChessBoard();
	}
}
