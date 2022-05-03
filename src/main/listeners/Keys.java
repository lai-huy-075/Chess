package main.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

import javax.swing.JOptionPane;

import main.Chess;
import main.board.Panel;

public class Keys implements KeyListener {
	public final Panel panel;
	
	public Keys(Panel panel) {
		this.panel = Objects.requireNonNull(panel, "Panel cannot be null.");
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		Chess.logger.info(e.getKeyChar() + " pressed");
		switch (e.getKeyChar()) {
		case KeyEvent.VK_ESCAPE:
			this.panel.displayMenu();
			return;
		case 'e':
			JOptionPane.showMessageDialog(this.panel, "Piece deselected", "", JOptionPane.PLAIN_MESSAGE);
			this.panel.board.resetTiles();
			return;
		case 's':
			this.panel.scoresOption();
			return;
		case 'r':
			this.panel.resetOption();
			return;
		case 'q':
			Panel.quitOption();
			return;
		case 'f':
			this.panel.resignOption();
			return;
		case 'c':
			Panel.controlsOption();
			return;
		case 'b':
			this.panel.drawOption();
		default:
			return;
		}
	}
}
