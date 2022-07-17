package main.file;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PGNFileFilter extends FileFilter {
	@Override
	public boolean accept(final File file) {
		if (file == null)
			return false;
		return isEndWithPGN(file);
	}

	@Override
	public String getDescription() {
		return "PGN files";
	}

	private boolean isEndWithPGN(final File file) {
		return file.getName().toLowerCase().endsWith("pgn") ? isPGNExtension(file) : false;
	}

	private boolean isPGNExtension(final File file) {
		int x = file.getName().indexOf('.');
		return file.getName().substring(x + 1, x + 4).equalsIgnoreCase("pgn");
	}
}
