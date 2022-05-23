package main.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

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

    /**
     * Constructor
     *
     * @param row row that this tile is in
     * @param col column that this tile is in
     */
    public Tile(final int row, final int col) {
	super("", null);

	if (col < 0 || col > 7)
	    throw new IndexOutOfBoundsException("Illegal column: " + col);
	this.col = col;

	if (row < 0 || row > 7)
	    throw new IndexOutOfBoundsException("Illegal row: " + row);
	this.row = row;

	this.color = this.row % 2 == 0 && this.col % 2 == 0 || this.row % 2 == 1 && this.col % 2 == 1 ? TileColor.Light
		: TileColor.Dark;

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

    /**
     * Converts {@link #col} to {@link String}
     *
     * @return String representation of {@link #col}
     */
    public String colToString() {
	return String.valueOf((char) ('a' + this.col));
    }

    @Override
    public boolean equals(final Object other) {
	if (this == other)
	    return true;
	if (!(other instanceof Tile))
	    return false;
	final Tile tile = (Tile) other;
	if (this.row != tile.row)
	    return false;
	if (this.col != tile.col)
	    return false;
	return true;
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
     * Reset the tile
     */
    public void reset() {
	this.piece = null;
	this.setForeground(null);
	this.setText("");
    }

    /**
     * Converts {@link #row} to {@link String}
     *
     * @return String representation of {@link #row}
     */
    public String rowToString() {
	return String.valueOf(8 - this.row);
    }

    /**
     * The the {@link Piece} on this
     *
     * @param piece Piece to set
     */
    @Deprecated
    public void setPiece(final Piece piece) {
	this.piece = piece;
    }

    @Override
    public String toString() {
	return this.colToString() + this.rowToString();
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
	this.setForeground(this.piece.color.color);
	this.setText(this.piece.toString());
    }
}
