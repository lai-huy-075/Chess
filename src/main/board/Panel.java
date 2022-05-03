package main.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import main.Chess;
import main.listeners.Keys;
import main.listeners.Mouse;
import main.player.Player;

public class Panel extends JPanel implements ActionListener {

	/**
	 * Standard {@link Font}
	 */
	public static final Font default_font = new Font("", Font.PLAIN, 20);

	public static final Dimension dim = new Dimension(70, 70);
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5953984732261423629L;

	private static final JLabel vs = new JLabel("vs", SwingConstants.CENTER);

	static {
		vs.setPreferredSize(dim);
		vs.setFont(default_font);
	}

	/**
	 * Display the controls of the game.
	 */
	public static void controlsOption() {
		JTextArea jta = new JTextArea(
				"Escape:\tPause\ne:\tdeselect piece\ns:\tScores\nr:\tReset\nq:\tQuit\nf:\tResign\nc:\tControls");
		jta.setOpaque(false);
		jta.setEditable(false);
		jta.setFont(new Font("Arial", Font.PLAIN, 20));
		JOptionPane.showMessageDialog(null, jta, "Controls", JOptionPane.PLAIN_MESSAGE, null);
	}

	/**
	 * Quit the game.
	 */
	public static void quitOption() {
		switch (JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null)) {
		case JOptionPane.YES_OPTION:
			System.exit(0);
		default:
			return;
		}
	}
	private JLabel black;
	public final Chessboard board;

	public final Keys keys;

	private JLabel white;

	public Panel(Player white, Player black) {
		this.board = new Chessboard(white, black);
		this.keys = new Keys(this);
		// Set Default GUI Elements
		this.setLayout(new GridLayout(10, 9));

		UIManager.put("OptionPane.messageFont", default_font);
		UIManager.put("OptionPane.buttonFont", default_font);
		UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 18));
		UIManager.put("Label.font", default_font);
		UIManager.put("Label.background", null);
		UIManager.put("Label.foreground", Color.BLACK);

		// Creates other GUI elements
		this.createLabels();
		this.createTiles();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		displayMenu();
	}

	/**
	 * Create {@link JLabel} to add to the {@link ChessBoardPanel}.
	 */
	private void createLabels() {
		this.white = new JLabel(this.board.white.name, SwingConstants.CENTER);
		this.white.setPreferredSize(dim);
		this.black = new JLabel(this.board.black.name, SwingConstants.CENTER);
		this.black.setPreferredSize(dim);

		// Empty Space
		for (int i = 0; i < 3; ++i) {
			JLabel blank = new JLabel("", SwingConstants.CENTER);
			blank.setPreferredSize(dim);
			blank.setFont(default_font);
			this.add(blank);
		}

		// Display all {@link Player}
		this.add(this.white);
		this.add(vs);
		this.add(this.black);

		// Empty Space
		for (int i = 0; i < 3; ++i) {
			JLabel blank = new JLabel("", SwingConstants.CENTER);
			blank.setPreferredSize(dim);
			blank.setFont(default_font);
			this.add(blank);
		}
	}

	/**
	 * Adds {@link Tile} to this.
	 */
	private void createTiles() {
		char[] columnDictation = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
		int num = columnDictation.length;

		for (Tile[] row : this.board.getBoard()) {
			JLabel col = new JLabel(String.valueOf(num), SwingConstants.CENTER);
			col.setPreferredSize(dim);
			this.add(col);
			--num;
			for (Tile tile : row) {
				tile.addKeyListener(this.keys);
				tile.addMouseListener(new Mouse(this, tile));
				this.add(tile);
			}
		}

		JButton menuButton = new JButton("\u2261");
		menuButton.addActionListener(this);
		menuButton.setFont(default_font);
		menuButton.setOpaque(false);
		menuButton.setContentAreaFilled(false);
		menuButton.setPreferredSize(dim);

		this.add(menuButton);
		for (char c : columnDictation) {
			JLabel row = new JLabel(String.valueOf(c), SwingConstants.CENTER);
			row.setPreferredSize(dim);
			this.add(row);
		}
	}

	public void displayMenu() {
		switch (JOptionPane.showOptionDialog(this, "Pick an option", "Menu", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new String[] { "Scores", "Reset", "Quit", "Resign", "Controls" }, 0)) {
		case 0:
			this.scoresOption();
			return;
		case 1:
			this.resetOption();
			return;
		case 2:
			quitOption();
			return;
		case 3:
			this.resignOption();
			return;
		case 4:
			controlsOption();
			return;
		default:
			return;
		}
	}
	
	public void drawOption() {
		Chess.logger.info("Draw offered.");
		switch (JOptionPane.showConfirmDialog(this, "Would you like to accept the draw offer?", "Draw Offered", JOptionPane.YES_NO_OPTION)) {
		case JOptionPane.YES_OPTION:
			Chess.logger.info("Draw accepted.");
		default:
			Chess.logger.info("Draw declined");
			return;
		}
	}

	/**
	 * Reset {@link #board}.
	 */
	public void resetOption() {
		switch (JOptionPane.showConfirmDialog(null, "Are you sure you want to reset?", "", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null)) {
		case JOptionPane.YES_OPTION:
			this.board.reset();
			JOptionPane.showMessageDialog(null, "Board has been Reset", "", JOptionPane.PLAIN_MESSAGE, null);
		default:
			return;
		}
	}

	/**
	 * A {@link Player} has resigned.
	 */
	public void resignOption() {
		if (!this.board.isGameOver())
			switch (JOptionPane.showConfirmDialog(null,
					this.board.getCurrentPlayer().name + ", are you sure you want to resign?", "",
					JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null)) {
			case JOptionPane.YES_OPTION:
				this.board.setGameOver(true);
				JOptionPane.showMessageDialog(null, this.board.getNextPlayer().name + " wins!");
			default:
				return;
			}
		JOptionPane.showMessageDialog(null, "The Game is Over!", "Game Over!", JOptionPane.PLAIN_MESSAGE, null);
	}

	/**
	 * Display the current scores of the {@link Player}
	 */
	public void scoresOption() {
		JOptionPane.showMessageDialog(null,
				String.format("%s%n%s", this.board.white.toString(), this.board.black.toString()), "Scores",
				JOptionPane.PLAIN_MESSAGE, null);
	}

}
