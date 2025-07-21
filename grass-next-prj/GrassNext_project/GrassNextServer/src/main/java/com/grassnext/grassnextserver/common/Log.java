package com.grassnext.grassnextserver.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Log class provides utility functionality for displaying custom log messages to the console.
 *
 */
public class Log {
    /**
     * A logger instance used for logging messages, warnings, and errors.
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Log.class);
    /**
     * Displays a formatted log message to the console.
     *
     * @param msg the message to be logged, which will be prefixed with a custom tag.
     */
    public static void show(String msg) {
        System.out.println("--->[GRASSNEXT-LOG] " + msg);
    }
}
