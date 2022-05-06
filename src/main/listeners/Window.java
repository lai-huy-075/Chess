package main.listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Objects;

import main.board.Panel;

public class Window implements WindowListener {
	public final Panel panel;
	public Window(Panel panel) {
		this.panel = Objects.requireNonNull(panel, "Panel cannot be null");
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.panel.quitOption();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
}
