package main.board;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import main.piece.Piece;

public final class Tile extends JButton {
	/**
	 * {@link Color} of the Dark Squares
	 */
	public static final Color dark = new Color(0x73463C);
	
	/**
	 * Default {@link Font}
	 */
	private static final Font default_font = new Font("", Font.PLAIN, 60);
	
	/**
	 * {@link Color} of the Light Squares
	 */
	public static final Color light = new Color(0xC89669);
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3879009674513449749L;
	
	/**
	 * {@link Color} of text to display on this
	 */
	public static final Color text = new Color(0x333333);

	/**
	 * Column this is in
	 */
	public final int col;
	
	/**
	 * {@link Piece} this currently has on it.<br>
	 * Null if there is no piece
	 */
	private Piece piece;
	
	/**
	 * Row this is in
	 */
	public final int row;

	public Tile(int row, int col) {
		super("", null);
		if (col < 0 || col > 7)
			throw new IndexOutOfBoundsException("Illegal column: " + col);
		else
			this.col = col;
		if (row < 0 || row > 7)
			throw new IndexOutOfBoundsException("Illegal row: " + row);
		else
			this.row = row;

		// Set GUI Elements
		this.setFont(default_font);
		this.setBackground(
				(this.row % 2 == 0 && this.col % 2 == 0) || (this.row % 2 == 1 && this.col % 2 == 1) ? light : dark);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setFocusPainted(false);
		this.setBorder(null);
		this.setPreferredSize(Panel.dim);

		this.setFocusable(true);
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Tile))
			return false;
		Tile tile = (Tile) other;
		if (this.row != tile.row)
			return false;
		if (this.col != tile.col)
			return false;
		return true;
	}

	/**
	 * Retrieves {@link #piece}
	 * @return {@link #piece}
	 */
	public Piece getPiece() {
		return this.piece;
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
	 * The the {@link Piece} on this
	 * @param piece Piece to set
	 */
	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	@Override
	public String toString() {
		return String.valueOf((char) ('a' + this.col)) + String.valueOf(8 - this.row);
	}
}
