package net.chess.chess.file;

import net.chess.chess.ChessApplication;
import net.chess.chess.piece.PieceColor;
import net.chess.chess.piece.PromoteState;
import net.chess.chess.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Read from PGN {@link File}
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 18 15
 */
public final class PGNReader {
	/**
	 * {@link String} holding regular expression of any valid chess move (hopefully)
	 */
	private static final String move = "[KQRNB]?[a-h]?x?[a-h][1-8](=[QRNB])?[+#]?|O\\-O(\\-O)?[+#]?";

	/**
	 * Determines a {@link PromoteState} from a move
	 * 
	 * @param move {@link String} of the move
	 * @return {@link PromoteState}
	 */
	public static PromoteState extractPromote(final String move) {
        return switch (move.charAt(move.lastIndexOf('=') + 1)) {
            case 'Q' -> PromoteState.Queen;
            case 'R' -> PromoteState.Rook;
            case 'N' -> PromoteState.Knight;
            case 'B' -> PromoteState.Bishop;
            default -> PromoteState.Fail;
        };
	}

	/**
	 * Extract any {@link String} matching a regular expression
	 *
	 * @param text  {@link String} line to extract from
	 * @param regex {@link String} Regular Expression
	 * @return array of matching {@link String}
	 */
	private static String[] getAllMatches(final String text, final String regex) {
		final List<String> matches = new ArrayList<>();
		final Matcher m = Pattern.compile(regex).matcher(text);
		while (m.find())
			matches.add(m.group());
		return matches.toArray(new String[0]);
	}

	/**
	 * Remove quotes from data extracted by {@link #getAllMatches(String, String)}
	 *
	 * @param text {@link String} line to extract from
	 *
	 * @return data extracted {@link String}
	 */
	private static String getData(final String text) {
		final String data = getFirst(text, "\".*\"");
        assert data != null;
        return data.substring(1, data.length() - 1);
	}

	/**
	 * Extract the first match of a regular expression
	 *
	 * @param text  {@link String} to search
	 * @param regex {@link String} Regular Expression
	 * @return first match
	 */
	public static String getFirst(final String text, final String regex) {
		final String[] matches = getAllMatches(text, regex);
		return matches.length == 0 ? null : matches[0];
	}

	/**
	 * Extract the last match of a regular expression
	 *
	 * @param text  {@link String} to search
	 * @param regex {@link String} Regular Expression
	 * @return first match
	 */
	public static String getLast(final String text, final String regex) {
		final String[] matches = getAllMatches(text, regex);
		return matches.length == 0 ? null : matches[matches.length - 1];
	}

	/**
	 * {@link PieceColor#Black} {@link Player} read from {@link #file}
	 */
	private Player black;

	/**
	 * PGN {@link File}
	 */
	public final File file;

	/**
	 * Primitive type array of {@link String} holding the moves read from
	 * {@link #file}
	 */
	private String[] moves;

	/**
	 * {@link String} holding the result of the game read
	 */
	private String result;

	/**
	 * {@link PieceColor#White} {@link Player} read from {@link #file}
	 */
	private Player white;

	/**
	 * Constructor
	 *
	 * @param file {@link File}
	 */
	public PGNReader(final File file) {
		Objects.requireNonNull(file, "File cannot be null");
		this.file = file;
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
	 * Get {@link #result}
	 * 
	 * @return {@link #result}
	 */
	public String getResult() {
		return this.result;
	}

	/**
	 * Get {@link #white}
	 *
	 * @return {@link #white}
	 */
	public Player getWhite() {
		return this.white;
	}

	/**
	 * Read {@link #file}
	 */
	public void read() {
		ChessApplication.logger.info("Reading from file started...");
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(this.file))) {
			StringBuilder data = new StringBuilder();
			int c = reader.read();
			while (c != -1) {
				data.append((char) c);
				c = reader.read();
			}

			final String[] lines = data.toString().split("\\n");
			for (final String line : lines)
				if (line.matches("\\[White \\\".*\\\"\\]"))
					this.white = new Player(getData(line), PieceColor.White);
				else if (line.matches("\\[Black \\\".*\\\"\\]"))
					this.black = new Player(getData(line), PieceColor.Black);
				else if (line.matches("\\[Result \\\".*\\\"\\]"))
					this.result = getData(line);

			this.moves = getAllMatches(data.toString(), move);
			ChessApplication.logger.info("White Player:\t" + this.white.name);
			ChessApplication.logger.info("Black Player:\t" + this.black.name);
			ChessApplication.logger.info(Arrays.deepToString(this.moves));
		} catch (final IOException | NullPointerException ioe) {
			ChessApplication.logger.throwing("PGNReader", "read", ioe);
			return;
		}
        ChessApplication.logger.info("Reading from file complete");
	}
}