package main.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import main.piece.Piece;

/**
 * Individual Tiles on the {@link Chessboard}
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public final class Tile extends JButton {
	/**
	 * Default {@link Font}
	 */
	private static final Font default_font = new Font("", Font.PLAIN, 60);

	/**
	 * {@link Dimension} of all elements in this.
	 */
	public static final Dimension dimension = new Dimension(80, 80);

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3879009674513449749L;

	/**
	 * {@link Color} for Raw Text
	 */
	public static final Color text = new Color(0x333333);

	/**
	 * {@link TileColor} for the Background
	 */
	public final TileColor color;

	/**
	 * {@link Tile} located directly down from this
	 */
	private Tile down;

	/**
	 * Column this is in
	 */
	public final int file;

	/**
	 * {@link Piece} this currently has on it.<br>
	 * Null if there is no piece
	 */
	private Piece piece;

	/**
	 * Row this is in
	 */
	public final int rank;

	/**
	 * {@link Tile} located directly up from this
	 */
	private Tile up;

	/**
	 * Constructor
	 *
	 * @param rank rank that this tile is in
	 * @param file column that this tile is in
	 */
	public Tile(final int row, final int col) {
		super("", null);

		if (col < 0 || col > 7)
			throw new IndexOutOfBoundsException("Illegal column: " + col);
		this.file = col;

		if (row < 0 || row > 7)
			throw new IndexOutOfBoundsException("Illegal rank: " + row);
		this.rank = row;

		this.color = this.rank % 2 == 0 && this.file % 2 == 0 || this.rank % 2 == 1 && this.file % 2 == 1
				? TileColor.Light
				: TileColor.Dark;

		// Set GUI Elements
		this.setFont(default_font);
		this.setBackground(this.color.standard);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setFocusPainted(false);
		this.setBorder(null);
		this.setPreferredSize(dimension);
		this.setFocusable(true);
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Tile))
			return false;
		final Tile tile = (Tile) other;
		if ((this.rank != tile.rank) || (this.file != tile.file))
			return false;
		return true;
	}

	/**
	 * Converts {@link #file} to {@link String}
	 *
	 * @return String representation of {@link #file}
	 */
	public String fileToString() {
		return String.valueOf((char) ('a' + this.file));
	}

	/**
	 * Get {@link #down}
	 *
	 * @return {@link #down}
	 */
	public Tile getDown() {
		return this.down;
	}

	/**
	 * Retrieves {@link #piece}
	 *
	 * @return {@link #piece}
	 */
	public Piece getPiece() {
		return this.piece;
	}

	/**
	 * Get {@link #up}
	 *
	 * @return {@link #up}
	 */
	public Tile getUp() {
		return this.up;
	}

	/**
	 * Converts {@link #rank} to {@link String}
	 *
	 * @return String representation of {@link #rank}
	 */
	public String rankToString() {
		return String.valueOf(8 - this.rank);
	}

	/**
	 * Reset the tile
	 */
	public void reset() {
		this.piece = null;
		this.setForeground(null);
		this.setText("");
	}

	/**
	 * Set {@link #down}
	 *
	 * @param down new Down {@link Tile}
	 */
	public void setDown(final Tile down) {
		this.down = down;
	}

	/**
	 * Set {@link #up}
	 *
	 * @param up new Up {@link Tile}
	 */
	public void setUp(final Tile up) {
		this.up = up;
	}

	@Override
	public String toString() {
		return this.fileToString() + this.rankToString();
	}

	/**
	 * Update {@link #piece} and GUI elements
	 *
	 * @param piece Piece to update
	 */
	public void updatePiece(final Piece piece) {
		if (piece == null) {
			this.reset();
			return;
		}

		this.piece = piece;
		this.piece.setTile(this);
		this.setForeground(this.piece.color.color);
		this.setText(this.piece.toFigure());
	}
}
