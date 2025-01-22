package antip2w.tools.util;

import antip2w.tools.util.functional_interface.QuadConsumer;
import antip2w.tools.util.functional_interface.TriConsumer;
import net.minecraft.util.StringIdentifiable;
import org.slf4j.Logger;

import java.util.function.BiConsumer;

public enum LogLevel implements StringIdentifiable {

    INFO("Info", Logger::info, Logger::info, Logger::info, (logger, format, args) -> logger.info(format, args), Logger::info),
    WARN("Warn", Logger::warn, Logger::warn, Logger::warn, (logger, format, args) -> logger.warn(format, args), Logger::warn),
    ERROR("Error", Logger::error, Logger::error, Logger::error, (logger, format, args) -> logger.error(format, args), Logger::error);

    private final String label;
    private final BiConsumer<Logger, String> logMessage;
    private final TriConsumer<Logger, String, Object> logFormatted1;
    private final QuadConsumer<Logger, String, Object, Object> logFormatted2;
    private final TriConsumer<Logger, String, Object[]> logFormatted3;
    private final TriConsumer<Logger, String, Throwable> logFormatted4;

    LogLevel(
        String label,
        BiConsumer<Logger, String> logMessage,
        TriConsumer<Logger, String, Object> logFormatted1,
        QuadConsumer<Logger, String, Object, Object> logFormatted2,
        TriConsumer<Logger, String, Object[]> logFormatted3,
        TriConsumer<Logger, String, Throwable> logFormatted4
    ) {
        this.label = label;
        this.logMessage = logMessage;
        this.logFormatted1 = logFormatted1;
        this.logFormatted2 = logFormatted2;
        this.logFormatted3 = logFormatted3;
        this.logFormatted4 = logFormatted4;
    }

    @Override
    public String asString() {
        return label;
    }

    public void log(Logger logger, String msg) {
        logMessage.accept(logger, msg);
    }

    public void log(Logger logger, String format, Object arg) {
        logFormatted1.accept(logger, format, arg);
    }

    public void log(Logger logger, String format, Object arg1, Object arg2) {
        logFormatted2.accept(logger, format, arg1, arg2);
    }

    public void log(Logger logger, String format, Object... args) {
        logFormatted3.accept(logger, format, args);
    }

    public void log(Logger logger, String message, Throwable t) {
        logFormatted4.accept(logger, message, t);
    }

}
