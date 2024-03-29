package main.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import main.Chess;
import main.board.Chessboard;
import main.board.Move;

/**
 * Write to PGN {@link File}
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 17 15
 */
public class PGNWriter {
	public static final void write(final Chessboard board) {
		Objects.requireNonNull(board, "Chessboard cannot be null");

		Chess.logger.info("Writing pgn started...");
		final String date = Chess.now.format(Chess.format), result = board.result();
		try (FileWriter writer = new FileWriter(Chess.pgn_file)) {
			writer.write("[Event \"1v1\"]\n");
			writer.write("[Site \"Chess.jar\"]\n");
			writer.write("[Date " + date + "]\n");
			writer.write("[Round \"-\"]\n");
			writer.write("[White \"" + board.white.name + "\"]\n");
			writer.write("[Black \"" + board.black.name + "\"]\n");
			writer.write("[Result \"" + result + "\"]\n");
			writer.write("\n");

			final List<Move> moves = board.getMoves();

			for (int i = 0; i < moves.size(); i += 2) {
				final Move white = moves.get(i);
				final Move black = i + 1 < moves.size() ? moves.get(i + 1) : null;
				final int move = i / 2 + 1;
				writer.write(String.valueOf(move) + ". " + white.move + (black == null ? "" : " " + black.move));
				writer.write(move % 7 == 0 ? "\n" : " ");
			}

			writer.write(result.isEmpty() ? "" : result);
		} catch (final IOException e) {
			Chess.logger.throwing("PGNWriter", "write", e);
			return;
		}
		Chess.logger.info("Writting pgn complete!");
	}
}