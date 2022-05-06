package main.listeners;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import main.Chess;
import main.board.Panel;
import main.board.Tile;


public class Mouse implements MouseListener {
	private static final Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
	
	public final Panel panel;
	public final Tile tile;
	
	public Mouse(Panel panel, Tile tile) {
		this.panel = Objects.requireNonNull(panel, "Panel cannot be null");
		this.tile = Objects.requireNonNull(tile, "Tile cannot be null");
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			Chess.logger.info("Clicked :\t" + this.tile.toString());
			this.panel.board.tileClicked(this.tile);
			this.panel.board.debugTiles();
			return;
		case MouseEvent.BUTTON2:
			String tileText = this.tile.getText();
			String coords = this.tile.row + ", " + this.tile.col;
			if (tileText.isEmpty() || !coords.equals(tileText)) {
				this.tile.setForeground(Tile.text);
				this.tile.setText(coords);
			} else {
				if (this.tile.getPiece() == null) {
					this.tile.setText("");
					this.tile.setForeground(null);
				} else {
					this.tile.setText(this.tile.getPiece().toString());
					this.tile.setForeground(this.tile.getPiece().color.color);
				}
			}
			return;
		case MouseEvent.BUTTON3:
			String tileText1 = this.tile.getText();
			if (tileText1.isEmpty() || !this.tile.toString().equals(tileText1)) {
				this.tile.setForeground(Tile.text);
				this.tile.setText(this.tile.toString());
			} else {
				if (this.tile.getPiece() == null) {
					this.tile.setText("");
					this.tile.setForeground(null);
				} else {
					this.tile.setText(this.tile.getPiece().toString());
					this.tile.setForeground(this.tile.getPiece().color.color);
				}
			}
			return;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.tile.setBorder(border);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.tile.setBorder(null);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.tile.getModel().setPressed(true);
		if (e.getButton() != MouseEvent.BUTTON1)
			return;
		
		Chess.logger.info("Pressed :\t" + this.tile.toString());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.tile.getModel().setPressed(false);
		
		if (e.getButton() != MouseEvent.BUTTON1)
			return;
		
		int x = (int) (e.getX()/Panel.dim.getWidth());
		int y = (int) (e.getY()/Panel.dim.getHeight());
		Tile t;
		try {
			t = this.panel.board.getTileOffset(this.tile, x, y);
			Chess.logger.info("Released:\t" + t.toString());
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			Chess.logger.info("Released:\tOut of Bounds.");
		}
	}
}
