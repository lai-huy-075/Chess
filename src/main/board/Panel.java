package main.board;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import main.Chess;
import main.listeners.Keys;
import main.listeners.Mouse;
import main.piece.Piece.PieceColor;
import main.player.Player;

public final class Panel extends JLayeredPane implements ActionListener {
	/**
	 * Game mode
	 */
	public static enum Mode {
		Debug, Funny, Normal, Over;
	}

	/**
	 * Arial {@link Font}
	 */
	public static final Font arial = new Font("Arial", Font.PLAIN, 30);
	
	private static final Font font = new Font("Arial", Font.PLAIN, 15);

	/**
	 * Primitive type array of {@link String} holding column names
	 */
	private static final String[] columns = new String[] { "a", "b", "c", "d", "e", "f", "g", "h" };

	/**
	 * {@link Map} mapping {@link #options} to their respective keyboard shortcuts
	 */
	private static final Map<String, String> controlMap;

	/**
	 * {@link JTextArea} to display keyboard shortcutes
	 */
	public static final JTextArea controls;

	/**
	 * Default {@link Font}
	 */
	public static final Font default_font = new Font("", Font.PLAIN, 30);

	/**
	 * Grid Layout
	 */
	private static final GridBagLayout grid = new GridBagLayout();

	/**
	 * {@link Insets}
	 */
	private static final Insets inset = new Insets(0, 0, 0, 0);

	/**
	 * {@link JButton} that handles displaying the menu
	 */
	private static final JButton menuButton;

	/**
	 * Primitive type array of {@link String} to pick from when
	 * {@link #displayMenu()} is called
	 */
	private static final String[] options;

	/**
	 * Primitive type array of {@link String} holding row names
	 */
	private static final String[] rows = new String[] { "1", "2", "3", "4", "5", "6", "7", "8" };

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5953984732261423629L;

	/**
	 * Versus {@link JLabel}
	 */
	private static final JLabel vs = new JLabel("vs", SwingConstants.CENTER);

	static {
		controlMap = new HashMap<>();
		controlMap.put("Pause", "Escape");
		controlMap.put("Scores", "s");
		controlMap.put("Reset", "r");
		controlMap.put("Draw", "d");
		controlMap.put("Resign", "f");
		controlMap.put("Quit", "q");
		controlMap.put("Controls", "c");
		controlMap.put("Deslect Piece", "e");

		String cont = "";
		for (String key : controlMap.keySet())
			cont += key + "\t" + controlMap.get(key) + "\n";

		options = new String[] { "Controls", "Reset", "Resign", "Draw", "Quit", "Deslect Piece", "Scores" };

		vs.setFont(arial);
		vs.setPreferredSize(Tile.dim);
		vs.setForeground(Color.black);

		controls = new JTextArea(cont);
		controls.setColumns(15);
		controls.setOpaque(false);
		controls.setEditable(false);
		controls.setFont(arial);

		menuButton = new JButton("\u2261");
		menuButton.setFont(arial);
		menuButton.setOpaque(false);
		menuButton.setContentAreaFilled(false);
		menuButton.setPreferredSize(Tile.dim);
	}

	/**
	 * {@link JLabel} holding {@link Chessboard#black}'s name.
	 */
	private JLabel black;

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
	 * {@link JLabel} for {@link Chessboard#white}i
	 */
	private JLabel white;
	
	/**
	 * Constructor
	 * 
	 * @param mode {@link Mode} of the game
	 */
	public Panel(Mode mode) {
		this.mode = Objects.requireNonNull(mode);
		this.board = new Chessboard(Player.default_white, Player.default_black);
		this.keys = new Keys(this);

		this.setLayout(grid);
		this.setDefaultGUIElements();

		// Creates other GUI elements
		this.createLabels();
		this.createTiles();
	}
	
	/**
	 * Constructor 
	 * @param mode {@link Mode} of the game
	 * @param white {@link PieceColor#White} {@link Player}
	 * @param black {@link PieceColor#Black} {@link Player}
	 */
	public Panel(Mode mode, Player white, Player black) {
		this.mode = Objects.requireNonNull(mode);
		this.board = new Chessboard(white, black);
		this.keys = new Keys(this);

		// Set Default GUI Elements
		this.setLayout(grid);
		this.setDefaultGUIElements();

		// Creates other GUI elements
		this.createLabels();
		this.createTiles();
	}
	
	/**
	 * constructor
	 * 
	 * @param white {@link PieceColor#White} {@link Player}
	 * @param black {@link PieceColor#Black} {@link Player}
	 */
	public Panel(Player white, Player black) {
		this(Mode.Normal, white, black);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Chess.logger.info(e.getActionCommand());
		displayMenu();
	}

	/**
	 * Display the controls of the game.
	 */
	public void controlsOption() {
		Chess.logger.info("Display Controls Option");
		JOptionPane.showMessageDialog(this, controls, "Controls", JOptionPane.PLAIN_MESSAGE, Chess.icon);
	}

	/**
	 * Create {@link JLabel} to add to the {@link ChessBoardPanel}.
	 */
	private void createLabels() {
		this.white = new JLabel(this.board.white.name, SwingConstants.CENTER);
		this.black = new JLabel(this.board.black.name, SwingConstants.CENTER);

		this.add(this.white, new GridBagConstraints(1, 0, 3, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, inset, 0, 0));
		this.add(vs, new GridBagConstraints(4, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				inset, 0, 0));
		this.add(this.black, new GridBagConstraints(5, 0, 3, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, inset, 0, 0));
	}

	/**
	 * Adds {@link Tile} to this.
	 */
	private void createTiles() {
		int num = columns.length - 1;
		

		Tile[][] board = this.board.getBoard();
		for (int i = 0; i < board.length; ++i) {
			JLabel col = new JLabel(rows[num], SwingConstants.LEFT);
			col.setVerticalAlignment(SwingConstants.TOP);
			col.setVerticalTextPosition(SwingConstants.TOP);
			col.setFont(font);
			--num;
			this.add(col, new GridBagConstraints(1, 1 + i, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
					GridBagConstraints.HORIZONTAL, inset, 0, 0));
			this.setLayer(col, 1);

			for (int j = 0; j < board[i].length; ++j) {
				Tile tile = board[i][j];
				tile.addKeyListener(this.keys);
				tile.addMouseListener(new Mouse(this.board, tile));
				this.add(tile, new GridBagConstraints(1 + j, 1 + i, 1, 1, 0, 0, GridBagConstraints.CENTER,
						GridBagConstraints.CENTER, inset, 0, 0));
				this.setLayer(tile, 0);
			}
		}

		menuButton.addActionListener(this);
		menuButton.addKeyListener(this.keys);
		this.add(menuButton, new GridBagConstraints(0, 10, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.CENTER, inset, 0, 0));
		this.setLayer(menuButton, 0);

		for (int i = 0; i < columns.length; ++i) {
			JLabel row = new JLabel(columns[i], SwingConstants.RIGHT);
			row.setVerticalAlignment(SwingConstants.BOTTOM);
			row.setVerticalTextPosition(SwingConstants.BOTTOM);
			row.setFont(font);
			
			this.add(row, new GridBagConstraints(1 + i, 8, 1, 1, 0, 0, GridBagConstraints.SOUTHEAST,
					GridBagConstraints.CENTER, inset, 0, 0));
			this.setLayer(row, 2);
		}
	}

	/**
	 * Display menu
	 */
	public void displayMenu() {
		Chess.logger.info("Display Menu");
		final int selection = JOptionPane.showOptionDialog(this, "Pick an option", "Menu", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, Chess.icon, options, "Controls");

		try {
			Chess.logger.info(options[selection] + " selected");
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			return;
		}

		switch (selection) {
		case 0:
			this.controlsOption();
			return;
		case 1:
			this.resetOption();
			return;
		case 2:
			this.resignOption();
			return;
		case 3:
			this.drawOption();
			return;
		case 4:
			this.quitOption();
			return;
		case 5:
			this.board.resetTiles();
			return;
		case 6:
			this.scoresOption();
			return;
		default:
			return;
		}
	}

	/**
	 * Draw the game
	 */
	public void drawOption() {
		Chess.logger.info("Draw offered.");
		switch (JOptionPane.showConfirmDialog(this, "Would you like to accept the draw offer?", "Draw Offered",
				JOptionPane.YES_NO_OPTION)) {
		case JOptionPane.YES_OPTION:
			Chess.logger.info("Draw accepted.");
			this.board.draw();
			return;
		default:
			Chess.logger.info("Draw declined");
			return;
		}
	}

	/**
	 * Quit the game.
	 */
	public void quitOption() {
		Chess.logger.info("Quit offered.");
		switch (JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, Chess.icon)) {
		case JOptionPane.YES_OPTION:
			Chess.logger.info("Quit accepted.");
			this.removeAll();
			System.exit(0);
		default:
			Chess.logger.info("Quit declined.");
			return;
		}
	}

	/**
	 * Reset {@link #board}.
	 */
	public void resetOption() {
		Chess.logger.info("Reset offered.");
		switch (JOptionPane.showConfirmDialog(this, "Are you sure you want to reset?", "", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, Chess.icon)) {
		case JOptionPane.YES_OPTION:
			Chess.logger.info("Reset accepted.");
			this.board.reset();
			JOptionPane.showMessageDialog(this, "Board has been Reset", "", JOptionPane.PLAIN_MESSAGE, Chess.icon);
			return;
		default:
			Chess.logger.info("Reset declined.");
			return;
		}
	}

	/**
	 * A {@link Player} has resigned.
	 */
	public void resignOption() {
		if (this.board.isGameOver())
			return;

		Chess.logger.info("Resign offered");
		switch (JOptionPane.showConfirmDialog(this,
				this.board.getCurrentPlayer().name + ", are you sure you want to resign?", "",
				JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, Chess.icon)) {
		case JOptionPane.YES_OPTION:
			Chess.logger.info("Resign accepted");
			this.board.setMode(Mode.Over);
			JOptionPane.showMessageDialog(this, this.board.getNextPlayer().name + " wins!", "",
					JOptionPane.PLAIN_MESSAGE, Chess.icon);
		default:
			Chess.logger.info("Resign declined");
			return;
		}
	}

	/**
	 * Display the current scores of the {@link Player}
	 */
	public void scoresOption() {
		Chess.logger.info("Display Scores");
		JOptionPane.showMessageDialog(this,
				String.format("%s%n%s", this.board.white.toString(), this.board.black.toString()), "Scores",
				JOptionPane.PLAIN_MESSAGE, Chess.icon);
	}

	/**
	 * Set default GUI elements using {@link UIManager#put}
	 */
	private void setDefaultGUIElements() {
		UIManager.put("OptionPane.messageFont", arial);
		UIManager.put("OptionPane.buttonFont", arial);
		UIManager.put("Button.font", arial);
		UIManager.put("Label.font", arial);
		UIManager.put("Label.background", null);
		UIManager.put("Label.foreground", Color.BLACK);
	}
}
