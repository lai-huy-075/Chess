package main.listeners;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

import javax.swing.JOptionPane;

import main.Chess;
import main.board.Panel;

/**
 * {@link KeyListener} for all interactable {@link Component} on the
 * {@link Panel}
 * 
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public class Keys implements KeyListener {
	/**
	 * {@link Panel} holding all {@link Component}
	 */
	public final Panel panel;

	/**
	 * Constructor
	 *
	 * @param panel {@link Panel}
	 */
	public Keys(final Panel panel) {
		this.panel = Objects.requireNonNull(panel, "Panel cannot be null.");
	}

	@Override
	public void keyPressed(final KeyEvent e) {
	}

	@Override
	public void keyReleased(final KeyEvent e) {
	}

	@Override
	public void keyTyped(final KeyEvent e) {
		Chess.logger.info(String.format("%s=%d typed", e.getKeyChar(), (int) e.getKeyChar()));
		switch (e.getKeyChar()) {
			case KeyEvent.VK_ESCAPE:
				this.panel.displayMenu();
				return;
			case 'c':
				this.panel.controlsOption();
				return;
			case 'd':
				this.panel.drawOption();
				return;
			case 'e':
				JOptionPane.showMessageDialog(this.panel, "Piece deselected", "", JOptionPane.PLAIN_MESSAGE);
				this.panel.board.resetTiles();
				return;
			case 'f':
				this.panel.resignOption();
				return;
			case 'q':
				this.panel.quitOption();
				return;
			case 'r':
				this.panel.resetOption();
				return;
			case 's':
				this.panel.scoresOption();
				return;
			default:
				return;
		}
	}
}
