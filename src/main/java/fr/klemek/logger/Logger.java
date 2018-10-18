package fr.klemek.logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.SimpleFormatter;

/**
 * Simple logger for this application.
 *
 * @author Clement Gouin
 */
public final class Logger {

    private static final int DEFAULT_DEPTH = 4;
    private static java.util.logging.Logger appLogger = java.util.logging.Logger.getLogger("Unknown");
    private static String appName = "Unknown";
    private static String basePackage = null;
    private static boolean initialized = false;


    private Logger() {
    }

    /**
     * @return the current logger level;
     */
    public static Level getLevel() {
        return appLogger.getLevel();
    }

    /**
     * Change the log level of this logger.
     *
     * @param newLevel the level of log to show
     */
    public static void setLevel(Level newLevel) {
        appLogger.setLevel(newLevel);
    }

    /**
     * Load a config file for the logger and set locale to english.
     *
     * @param relativePath the path in resources of the config file
     */
    public static void init(String relativePath) {
        init(relativePath, Level.INFO);
    }

    /**
     * Load a config file for the logger and set locale to english.
     *
     * @param relativePath the path in resources of the config file
     * @param level        the level to set the logger to
     */
    public static void init(String relativePath, Level level) {
        Locale.setDefault(Locale.ENGLISH);
        loadConfigFromFile(relativePath);
        Logger.setLevel(level);
        Logger.initialized = true;
    }

    /**
     * Load a config file for the logger.
     *
     * @param relativePath the path in resources of the config file
     */
    private static void loadConfigFromFile(String relativePath) {
        try {
            InputStream is = Logger.class.getClassLoader().getResourceAsStream(relativePath);
            if (is == null) {
                Logger.log(Level.SEVERE, "Logger config file not found at path {0}", relativePath);
                return;
            }
            LogManager.getLogManager().readConfiguration(is);

            appName = Utils.getString(relativePath, "app_name", "Unknown");
            basePackage = Utils.getString(relativePath, "default_package", null);
            String outputFile = Utils.getString(relativePath, "output_file", null);

            appLogger = java.util.logging.Logger.getLogger(appName);

            if (outputFile != null) {
                Handler handler = new FileHandler(outputFile, true);
                handler.setFormatter(new SimpleFormatter());
                appLogger.addHandler(handler);
            }

        } catch (IOException e) {
            Logger.log(e);
        }
    }

    /**
     * Log an exception.
     *
     * @param e the exception to log
     */
    public static void log(Throwable e) {
        Logger.log(Logger.DEFAULT_DEPTH, Level.SEVERE, e.toString(), e);
    }

    /**
     * Log an exception.
     *
     * @param lvl the level of logging
     * @param e   the exception to log
     */
    public static void log(Level lvl, Throwable e) {
        Logger.log(Logger.DEFAULT_DEPTH, lvl, e.toString(), e);
    }

    /**
     * Log an exception.
     *
     * @param e   the exception to log
     * @param msg the exception msg
     */
    public static void log(Throwable e, String msg) {
        Logger.log(Logger.DEFAULT_DEPTH, Level.SEVERE, msg + ": {0}", e);
    }

    /**
     * Log an exception.
     *
     * @param lvl the level of logging
     * @param e   the exception to log
     * @param msg the exception msg
     */
    public static void log(Level lvl, Throwable e, String msg) {
        Logger.log(Logger.DEFAULT_DEPTH, lvl, msg + ": {0}", e);
    }

    /**
     * Log an INFO message.
     *
     * @param message the message
     * @param objects the object for the message formatting
     */
    public static void log(String message, Object... objects) {
        Logger.log(Logger.DEFAULT_DEPTH, Level.INFO, message, objects);
    }

    /**
     * Log a message.
     *
     * @param lvl     the level of logging
     * @param message the message
     * @param objects the object for the message formatting
     */
    public static void log(Level lvl, String message, Object... objects) {
        Logger.log(Logger.DEFAULT_DEPTH, lvl, message, objects);
    }

    /**
     * Log a message.
     *
     * @param depth   the source depth in stack
     * @param lvl     the level of logging
     * @param message the message
     * @param objects the object for the message formatting
     */
    private static void log(int depth, Level lvl, String message, Object... objects) {
        if (!initialized) {
            initialized = true;
            Logger.log(Level.WARNING, "Logger was not initialized please do so before using.");
        }
        if (lvl.intValue() < appLogger.getLevel().intValue())
            return;
        message = String.format("[%s-%s] %s", appName, Utils.getCallingClassName(depth), message);
        appLogger.log(lvl, message, objects);
        if (objects.length > 0 && objects[0] instanceof Throwable) {
            Throwable throwable = (Throwable) objects[0];
            if (lvl == Level.SEVERE) {
                boolean inPackage = false;
                for (StackTraceElement ste : throwable.getStackTrace()) {
                    Logger.log(depth + 1, Level.SEVERE, "\t {0}", ste);
                    if (Logger.basePackage != null) {
                        if (!inPackage && ste.getClassName().startsWith(Logger.basePackage))
                            inPackage = true;
                        else if (inPackage && !ste.getClassName().startsWith(Logger.basePackage))
                            break;
                    }
                }
            }
            if (throwable.getCause() != null)
                Logger.log(depth + 1, lvl, "Caused by: {0}", throwable.getCause());
        }

    }
}
