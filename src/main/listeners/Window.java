package main.listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Objects;

import main.Chess;
import main.board.Panel;

/**
 * {@link WindowListener}
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public class Window implements WindowListener {
	/**
	 * {@link Panel} to use when closing the {@link java.awt.Window}
	 */
	public final Panel panel;

	/**
	 * Constructor
	 *
	 * @param panel {@link Panel} of this
	 */
	public Window(final Panel panel) {
		this.panel = Objects.requireNonNull(panel, "Panel cannot be null");
	}

	@Override
	public void windowActivated(final WindowEvent e) {
		Chess.logger.info("Window Activated");
	}

	@Override
	public void windowClosed(final WindowEvent e) {
		Chess.logger.info("Window Closed");
	}

	@Override
	public void windowClosing(final WindowEvent e) {
		Chess.logger.info("Window Closing");
		this.panel.quitOption();
	}

	@Override
	public void windowDeactivated(final WindowEvent e) {
		Chess.logger.info("Window Deactivated");
	}

	@Override
	public void windowDeiconified(final WindowEvent e) {
		Chess.logger.info("Window Deiconified");
	}

	@Override
	public void windowIconified(final WindowEvent e) {
		Chess.logger.info("Window Iconified");
	}

	@Override
	public void windowOpened(final WindowEvent e) {
		Chess.logger.info("Window Opened");
	}
}
