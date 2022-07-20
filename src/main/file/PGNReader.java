package main.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
	 * {@link String} holding regular expression of any valid chess move (hopefully)
	 */
	private static final String move = "[KQRNB]?[a-h]?x?[a-h][1-8](=[QRNB])?[+#]?|O\\-O(\\-O)?[+#]?";

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
	 * Primitive type array of {@link String} holding the moves read from
	 * {@link #file}
	 */
	private String[] moves;

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
		Chess.logger.info("Reading from file started...");
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(this.file))) {
			String data = "";
			int c = reader.read();
			while (c != -1) {
				data += (char) c;
				c = reader.read();
			}

			String[] lines = data.split("\\n");
			for (String line : lines) {
				if (line.matches("\\[White \\\".*\\\"\\]"))
					this.white = new Player(getData(line), PieceColor.White);
				else if (line.matches("\\[Black \\\".*\\\"\\]"))
					this.black = new Player(getData(line), PieceColor.Black);
			}

			this.moves = getAllMatches(data, move);
			Chess.logger.info("White Player:\t" + this.white.name);
			Chess.logger.info("Black Player:\t" + this.black.name);
			Chess.logger.info(Arrays.deepToString(this.moves));
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
		Chess.logger.info("Reading from file complete");
	}

	/**
	 * Remove quotes from data extracted by {@link #getAllMatches(String, String)}
	 * 
	 * @param text {@link String} line to extract from
	 * 
	 * @return data extracted {@link String}
	 */
	private static final String getData(String text) {
		String data = getMatch(text, "\".*\"");
		return data.substring(1, data.length() - 1);
	}
	
	/**
	 * Extract the first match of a regular expression
	 * 
	 * @param text {@link String} to search
	 * @param regex {@link String} Regular Expression
	 * @return first match
	 */
	public static final String getMatch(String text, String regex) {
		final Matcher m = Pattern.compile(regex).matcher(text);
		return m.find() ? m.group() : null;
	}

	/**
	 * Extract any {@link String} matching a regular expression
	 * 
	 * @param text  {@link String} line to extract from
	 * @param regex {@link String} Regular Expression
	 * @return list of matching {@link String}
	 */
	private static final String[] getAllMatches(String text, String regex) {
		List<String> matches = new ArrayList<>();
		Matcher m = Pattern.compile(regex).matcher(text);
		while (m.find())
			matches.add(m.group());
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
	 * Get {@link #moves}
	 * 
	 * @return {@link #moves}
	 */
	public String[] getMoves() {
		return this.moves;
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