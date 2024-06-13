package net.chess.chess.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Custom {@link Formatter} for {@link net.chess.chess.ChessApplication#logger}
 *
 * @author lai.huy.075
 * @version 2024-06-13
 */
public class CustomFormatter extends Formatter {
    /**
     * Constructor
     */
    public CustomFormatter() {
    }

    @Override
    public String format(final LogRecord record) {
        final String line = String.format("[%s] %s%n", record.getLevel(), record.getMessage());
        System.out.print(line);
        return line;
    }
}