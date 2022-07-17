package main.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.Chess;
import main.piece.PieceColor;
import main.player.Player;

/**
 * Read from PGN {@link File}
 * 
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 18 15
 */
public class PGNReader {
	/**
	 * PGN {@link File}
	 */
	public final File file;

	/**
	 * {@link PieceColor#White} {@link Player} read from {@link #file}
	 */
	private Player white;

	/**
	 * {@link PieceColor#Black} {@link Player} read from {@link #file}
	 */
	private Player black;

	/**
	 * Constructor
	 * 
	 * @param file {@link File}
	 */
	public PGNReader(final File file) {
		Objects.requireNonNull(file, "File cannot be null");
		PGNFileFilter filter = new PGNFileFilter();
		if (!filter.accept(file))
			throw new IllegalArgumentException("Inputed file is not a PGN file");

		this.file = file;
	}

	/**
	 * Read {@link #file}
	 */
	public void read() {
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(this.file))) {
			String data = "";
			int c = reader.read();
			while (c != -1) {
				data += (char) c;
				c = reader.read();
			}

			String[] lines = data.split("\\n");
			for (String line : lines) {
				if (line.contains("White")) {
					this.white = new Player(getData(line)[0], PieceColor.White);
				} else if (line.contains("Black")) {
					this.black = new Player(getData(line)[0], PieceColor.Black);
				}
			}

			return;
		} catch (FileNotFoundException fnfe) {
			Chess.logger.throwing("PGNReader", "read", fnfe);
			return;
		} catch (IOException ioe) {
			Chess.logger.throwing("PGNReader", "read", ioe);
			return;
		} catch (NullPointerException npe) {
			Chess.logger.throwing("PGNReader", "read", npe);
			return;
		}
	}

	/**
	 * Remove quotes from data extracted by {@link #getAllMatches(String, String)}
	 * 
	 * @param text {@link String} line to extract from
	 * 
	 * @return data extracted {@link String}
	 */
	private static String[] getData(String text) {
		List<String> data = new ArrayList<>();
		for (String string : getAllMatches(text, "\".*\""))
			data.add(string.substring(1, string.length() - 1));
		return data.toArray(new String[data.size()]);
	}

	/**
	 * Extract any {@link String} matching a regular expression
	 * 
	 * @param text  {@link String} line to extract from
	 * @param regex {@link String} Regular Expression
	 * @return list of matching {@link String}
	 */
	private static String[] getAllMatches(String text, String regex) {
		List<String> matches = new ArrayList<>();
		Matcher m = Pattern.compile("(?=(" + regex + "))").matcher(text);
		while (m.find())
			matches.add(m.group(1));
		return matches.toArray(new String[matches.size()]);
	}

	/**
	 * Get {@link #black}
	 * 
	 * @return {@link #black}
	 */
	public Player getBlack() {
		return this.black;
	}

	/**
	 * Get {@link #white}
	 * 
	 * @return {@link #white}
	 */
	public Player getWhite() {
		return this.white;
	}
}