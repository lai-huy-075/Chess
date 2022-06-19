package main.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Custom {@link Formatter} for {@link main.Chess#logger}
 * 
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public class CustomFormatter extends Formatter {
    @Override
    public String format(final LogRecord record) {
        final String line = String.format("[%s] %s%n", record.getLevel(), record.getMessage());
        System.out.print(line);
        return line;
    }
}
