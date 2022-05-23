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
	if (e.getButton() != MouseEvent.BUTTON1)
	    return;

	Chess.logger.info("Pressed :\t" + this.tile.toString());
	this.board.updateSource(this.tile);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
	this.tile.getModel().setPressed(false);

	if (e.getButton() != MouseEvent.BUTTON1)
	    return;

	final int x = Math.floorDiv(e.getX(), (int) Tile.dim.getWidth());
	final int y = Math.floorDiv(e.getY(), (int) Tile.dim.getHeight());
	Chess.logger.info("Offset:\t(" + x + "," + y + ")");
	Tile t;
	try {
	    t = this.board.getTileOffset(this.tile, x, y);
	    Chess.logger.info("Released:\t" + t.toString());
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
