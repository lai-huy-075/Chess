package net.chess.chess.board;

import javax.swing.SwingConstants;

import javafx.geometry.Dimension2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.chess.chess.ChessApplication;
import net.chess.chess.piece.Piece;

import static net.chess.chess.ChessApplication.TILE_SIZE;

/**
 * Individual Tiles on the {@link Chessboard}
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public final class Tile extends Button {
	/**
	 * Default {@link Font}
	 */
	private static final Font DEFAULT_FONT = new Font("Segoe UI Symbol", 30);

	/**
	 * {@link Color} for Raw Text
	 */
	public static final Color text = Color.rgb(0x33, 0x33, 0x33);

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
	public Tile(final int rank, final int file) {
		super("", null);

		if (file < 0 || file > 7)
			throw new IndexOutOfBoundsException("Illegal column: " + file);
		this.file = file;

		if (rank < 0 || rank > 7)
			throw new IndexOutOfBoundsException("Illegal rank: " + rank);
		this.rank = rank;

		this.color = this.rank % 2 == 0 && this.file % 2 == 0 || this.rank % 2 == 1 && this.file % 2 == 1
				? TileColor.Light
				: TileColor.Dark;

		// Set GUI Elements
		this.setText("");
		this.setFont(DEFAULT_FONT);
		this.setBackground(this.color.standard);
		this.setAlignment(Pos.CENTER);
		this.setFocused(false);
		this.setBorder(null);
		this.setPrefSize(TILE_SIZE, TILE_SIZE);
		this.setFocusTraversable(true);
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Tile tile))
			return false;
        return (this.rank == tile.rank) && (this.file == tile.file);
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
		this.setBackground(this.color.standard);
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
		this.setBackground(this.color.standard);
		this.setTextFill(this.piece.color.color);
		this.setText(this.piece.toFigure());
	}
}
