package nl.hpfxd.limbo.logging;

import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LimboLogFormatter extends Formatter {
    private static final MessageFormat messageFormat = new MessageFormat("[{3,date,hh:mm:ss.SSS}] [{2}/{1}] [{0}] {4}\n");

    @Override
    public String format(LogRecord record) {
        String[] str = record.getLoggerName().split("\\.");
        Object[] arguments = new Object[]{str[str.length - 1], record.getLevel(), Thread.currentThread().getName(), new Date(record.getMillis()), record.getMessage(), record.getSourceMethodName()};
        return messageFormat.format(arguments);
    }
}
