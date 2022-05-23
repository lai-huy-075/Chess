package main.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Custom {@link Formatter} for {@link main.Chess#logger }
 */
public class CustomFormatter extends Formatter {
    @Override
    public String format(final LogRecord record) {
	final String line = String.format("[%s] %s%n", record.getLevel(), record.getMessage());
	System.out.print(line);
	return line;
    }
}
