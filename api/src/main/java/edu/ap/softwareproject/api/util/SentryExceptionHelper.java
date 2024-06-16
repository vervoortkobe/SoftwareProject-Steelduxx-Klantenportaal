package edu.ap.softwareproject.api.util;

import edu.ap.softwareproject.api.interfaces.ExceptionHelper;
import io.sentry.Sentry;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * An exception helper for use with Sentry.
 * Please use this if you want to log any exceptions/messages!
 * Usage:
 * ExceptionHelper sentry = new SentryExceptionHelper();
 * try {
 * throw new Exception("I am an error!");
 * } catch (Exception e) {
 * sentry.warn(e);
 * }
 */
public final class SentryExceptionHelper implements ExceptionHelper {
    private final Logger logger = LogManager.getLogger(SentryExceptionHelper.class);

    /**
     * Captures an exception to the Sentry dashboard and logs it with Log4J
     *
     * @param exception The exception you want to log.
     */
    @SneakyThrows
    public void warn(Exception exception) {
        if (throwExceptions())
            throw exception;
        logger.warn(stackTraceToString(exception));
        Sentry.captureException(exception);
    }

    /**
     * Captures an exception to the Sentry dashboard and logs it with Log4J
     * + Adds a message as a breadcrum.
     *
     * @param message The message you want to log.
     * @param exception The exception you want to log.
     */
    @SneakyThrows
    public void warn(String message, Exception exception) {
        if (throwExceptions())
            throw exception;
        logger.warn(message);
        logger.warn(stackTraceToString(exception));
        Sentry.addBreadcrumb(message);
        Sentry.captureException(exception);
    }

    /**
     * Logs a message to the console and adds it as a breadcrum for sentry.
     *
     * @param message The message you want to log.
     */
    public void info(String message) {
        logger.info(message);
        Sentry.addBreadcrumb(message);
    }

    /**
     * Logs a message to the console and reports it as an issue to the dashboard.
     *
     * @param message The message you want to log.
     */
    public void infoReport(String message) {
        logger.info(message);
        Sentry.captureMessage(message);
    }

    /**
     * Checks if exceptions can be thrown to the console whenever the "pleasethrowexceptions" environment variable is set to true.
     *
     * @return The current value of "pleasethrowexceptions".
     */
    public boolean throwExceptions() {
        return Boolean.parseBoolean(System.getenv("pleasethrowexceptions"));
    }

    private String stackTraceToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        e.printStackTrace(pw);

        return sw.toString();
    }
}
