package fr.klemek.logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * Simple logger for this application.
 *
 * @author Clement Gouin
 */
public final class Logger {

    private static java.util.logging.Logger appLogger = java.util.logging.Logger.getLogger(Utils.getString("app_name","Unkown"));
    private static final String APP_NAME = Utils.getString("app_name","Unkown");
    private static final String BASE_PACKAGE = Utils.getString("default_package",null);
    private static final int DEFAULT_DEPTH = 4;

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
            appLogger = java.util.logging.Logger.getLogger(APP_NAME);
        } catch (IOException e) {
            Logger.log(e);
        }
    }

    /**
     * Log an exception.
     *
     * @param e the exception to log
     */
    public static void log(Exception e) {
        Logger.log(Logger.DEFAULT_DEPTH, Level.SEVERE, e.toString(), e);
    }

    /**
     * Log an exception.
     *
     * @param lvl the level of logging
     * @param e   the exception to log
     */
    public static void log(Level lvl, Exception e) {
        Logger.log(Logger.DEFAULT_DEPTH, lvl, e.toString(), e);
    }

    /**
     * Log an exception.
     *
     * @param e   the exception to log
     * @param msg the exception msg
     */
    public static void log(Exception e, String msg) {
        Logger.log(Logger.DEFAULT_DEPTH, Level.SEVERE, msg + " : {0}", e);
    }

    /**
     * Log an exception.
     *
     * @param lvl the level of logging
     * @param e   the exception to log
     * @param msg the exception msg
     */
    public static void log(Level lvl, Exception e, String msg) {
        Logger.log(Logger.DEFAULT_DEPTH, lvl, msg + " : {0}", e);
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
        if(!initialized) {
            Logger.log(Level.WARNING, "Logger was not initialized please do so before using.");
            initialized = true;
        }
        message = String.format("[%s-%s] %s", APP_NAME, Utils.getCallingClassName(depth), message);
        appLogger.log(lvl, message, objects);
        if (lvl == Level.SEVERE && objects.length > 0 && objects[0] instanceof Exception) {
            boolean inPackage = false;
            for (StackTraceElement ste : ((Exception) objects[0]).getStackTrace()) {
                Logger.log(depth + 1, Level.SEVERE, "\t {0}", ste);
                if(Logger.BASE_PACKAGE != null){
                    if (!inPackage && ste.getClassName().startsWith(Logger.BASE_PACKAGE))
                        inPackage = true;
                    else if (inPackage && !ste.getClassName().startsWith(Logger.BASE_PACKAGE))
                        break;
                }
            }
        }
    }
}
