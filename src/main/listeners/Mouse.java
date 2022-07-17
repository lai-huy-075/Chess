package main.listeners;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import main.Chess;
import main.board.Chessboard;
import main.board.Tile;
import main.board.TileColor;

/**
 * {@link MouseListener} for each {@link Tile}
 * 
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public class Mouse implements MouseListener {
	/**
	 * {@link Border} to set when {@link #mouseEntered(MouseEvent)} is called
	 */
	private static final Border border = BorderFactory.createLineBorder(Color.BLACK, 3);

	/**
	 * {@link Chessboard} the {@link Tile} is on
	 */
	public final Chessboard board;

	/**
	 * {@link Tile} this is linked to
	 */
	public final Tile tile;

	/**
	 * Constructor
	 * 
	 * @param board {@link Chessboard} to interact with
	 * @param tile  {@link Tile} to interact with
	 */
	public Mouse(final Chessboard board, final Tile tile) {
		this.board = Objects.requireNonNull(board, "Panel cannot be null");
		this.tile = Objects.requireNonNull(tile, "Tile cannot be null");
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			this.board.tileClicked();
			return;
		case MouseEvent.BUTTON3:
			switch (this.tile.color) {
			case Dark:
				this.tile.setBackground(
						TileColor.Dark.standard.equals(this.tile.getBackground()) ? TileColor.Dark.selected
								: TileColor.Dark.standard);
				return;
			case Light:
				this.tile.setBackground(
						TileColor.Light.standard.equals(this.tile.getBackground()) ? TileColor.Light.selected
								: TileColor.Light.standard);
				return;
			default:
				throw new IllegalStateException("Illegal Tile.TileColor:\t" + this.tile.color.name());
			}
		default:
			return;
		}
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		this.tile.setBorder(border);
	}

	@Override
	public void mouseExited(final MouseEvent e) {
		this.tile.setBorder(null);
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		this.tile.getModel().setPressed(true);
		switch (this.board.getMode()) {
		case Debug:
			return;
		case Funny:
			break;
		case Normal:
			break;
		case Over:
			return;
		default:
			throw new IllegalStateException("Illegal Mode:\t" + this.board.getMode());
		}
		
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			break;
		default:
			return;
		}

		Chess.logger.info("Pressed :\t" + this.tile.toString());
		this.board.updateSource(this.tile);
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		this.tile.getModel().setPressed(false);
		switch (this.board.getMode()) {
		case Debug:
			return;
		case Funny:
			break;
		case Normal:
			break;
		case Over:
			return;
		default:
			throw new IllegalStateException("Illegal Mode:\t" + this.board.getMode());
		}
		
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			break;
		default:
			return;
		}

		final int x = Math.floorDiv(e.getX(), (int) Tile.dimension.getWidth());
		final int y = Math.floorDiv(e.getY(), (int) Tile.dimension.getHeight());
		Chess.logger.info("Offset:\t(" + x + "," + y + ")");
		Tile t;
		try {
			t = this.board.getTileOffset(this.tile, x, y);
			Chess.logger.info("Released:\t" + t.toString() + "\n");
		} catch (final ArrayIndexOutOfBoundsException aioobe) {
			Chess.logger.info("Released:\tOut of Bounds.");
			return;
		}

		if (!this.board.compareSource(t)) {
			this.board.updateDestination(t);
			this.board.tileClicked();
		}
	}
}
