package main.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import main.piece.Piece;

public final class Tile extends JButton {
	/**
	 * {@link Color} for each tile
	 */
	public static enum TileColor {
		Dark(new Color(0x73463c), new Color(0xe06954)),
		Light(new Color(0xc89669), new Color(0xec7964));
		
		/**
		 * {@link Color} displayed when selected by {@link MouseEvent#BUTTON3}
		 */
		public final Color selected;
		
		/**
		 * {@link Color} displayed when not selected
		 */
		public final Color standard;
		
		private TileColor(Color standard, Color selected) {
			this.standard = Objects.requireNonNull(standard, "Standard Color cannot be null");
			this.selected = Objects.requireNonNull(selected, "Selected Color cannot be null");
		}
	}
	
	/**
	 * Default {@link Font}
	 */
	private static final Font default_font = new Font("", Font.PLAIN, 60);
	
	/**
	 * {@link Dimension} of all elements in this.
	 */
	public static final Dimension dim = new Dimension(80, 80);
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3879009674513449749L;
	
	/**
	 * {@link Color} for Raw Text
	 */
	public static final Color text = new Color(0x333333);

	/**
	 * Column this is in
	 */
	public final int col;
	
	/**
	 * {@link TileColor} for the Background
	 */
	public final TileColor color;
	
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
		
		this.color = (this.row % 2 == 0 && this.col % 2 == 0) || (this.row % 2 == 1 && this.col % 2 == 1) ? TileColor.Light : TileColor.Dark;

		// Set GUI Elements
		this.setFont(default_font);
		this.setBackground(this.color.standard);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setFocusPainted(false);
		this.setBorder(null);
		this.setPreferredSize(dim);
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
