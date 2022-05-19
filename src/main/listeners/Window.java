package main.listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Objects;

import main.Chess;
import main.board.Panel;

/**
 * {@link WindowListener}
 */
public class Window implements WindowListener {
	/**
	 * {@link Panel} to use when closing the {@link java.awt.Window}
	 */
	public final Panel panel;
	
	/**
	 * Constructor
	 * @param panel {@link Panel} of this 
	 */
	public Window(Panel panel) {
		this.panel = Objects.requireNonNull(panel, "Panel cannot be null");
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
		Chess.logger.info("Window Activated");
	}

	@Override
	public void windowClosed(WindowEvent e) {
		Chess.logger.info("Window Closed");
	}

	@Override
	public void windowClosing(WindowEvent e) {
		Chess.logger.info("Window Closing");
		this.panel.quitOption();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		Chess.logger.info("Window Deactivated");
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		Chess.logger.info("Window Deiconified");
	}

	@Override
	public void windowIconified(WindowEvent e) {
		Chess.logger.info("Window Iconified");
	}

	@Override
	public void windowOpened(WindowEvent e) {
		Chess.logger.info("Window Opened");
	}
}
