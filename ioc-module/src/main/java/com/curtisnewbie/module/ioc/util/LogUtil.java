package com.curtisnewbie.module.ioc.util;


import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Util class for logging
 *
 * @author yongjie.zhuang
 */
public final class LogUtil {

    private static final String EMPTY_METHOD_NAME = "";

    /** Get logger of name clazz.getName() */
    public static Logger getLogger(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return Logger.getLogger(clazz.getName());
    }

    /**
     * Log info message using {@link String#format(String, Object...)}
     * <p>
     * The log level will be INFO, the source class for the message will be the logger's name, and the source method
     * will be empty string
     * </p>
     */
    public static void logFormatted(Logger logger, String formatStr, Object... args) {
        if (args.length < 1)
            logger.logp(Level.INFO, logger.getName(), EMPTY_METHOD_NAME, formatStr);
        else
            logger.logp(Level.INFO, logger.getName(), EMPTY_METHOD_NAME, String.format(formatStr, args));
    }

}
