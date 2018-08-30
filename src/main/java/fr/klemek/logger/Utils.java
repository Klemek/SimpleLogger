package fr.klemek.logger;

import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;

/**
 * Utility class that store useful misc functions.
 *
 * @author Clement Gouin
 */
final class Utils {

    private Utils() {
    }

    /*
     * Configuration utils
     */

    /**
     * Get a configuration string by its key.
     *
     * @param bundlePath the path to the configuration file
     * @param key the key in the config file
     * @param defaultValue the default value to use if not found
     * @return the string or default value if not found
     */
    static String getString(String bundlePath, String key, String defaultValue) {
        int pos = bundlePath.indexOf(".properties");
        try {
            return  ResourceBundle.getBundle(bundlePath.substring(0,pos)).getString(key);
        } catch (MissingResourceException e) {
            return defaultValue;
        }
    }

    /**
     * Return the class name from the calling class in th stack trace.
     *
     * @param stackLevel the level in the stack trace
     * @return the classname of th calling class
     */
    static String getCallingClassName(int stackLevel) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackLevel >= stackTrace.length)
            return null;
        String[] source = stackTrace[stackLevel].getClassName().split("\\.");
        return source[source.length - 1];
    }
}
