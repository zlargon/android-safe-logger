package com.zlargon.utility.logger;

/**
 *
 * @author zlargon
 * @email  zlargon@icloud.com
 *
 * Improve android.util.Log Static Method
 *
 * 1. check null and empty string
 * 2. support String Format with indefinite parameter 'objs'
 * 3. support to print large string (over than 4000)
 *
 */
public class Log {
    private static final String TAG = "Logger";
    private static final int LOG_MAX_SIZE = 4000;

    // Log Level
    public static final int VERBOSE = 1 << 0;
    public static final int DEBUG   = 1 << 1;
    public static final int INFO    = 1 << 2;
    public static final int WARN    = 1 << 3;
    public static final int ERROR   = 1 << 4;
    public static final int ALL_LEVEL = VERBOSE | DEBUG | INFO | WARN | ERROR;

    /**
     * 1. Setup Log Level (default enable all the log level)
     *
     * @param logLevel
     *
     * Sample of setting Log Level with 'Debug' and 'Info'
     *
     * Log.setLevel(Log.DEBUG | Log.INFO);
     *
     */
    private static int LOG_LEVEL = ALL_LEVEL;  // Default All Level
    public static void setLevel(int logLevel) {
        // check log level
        if (logLevel < 0 || logLevel > ALL_LEVEL) {
            printByAndroidLog(ERROR, TAG, strict("[Error] log level (%d) is out of range 0 ~ %d", logLevel, ALL_LEVEL));
            return;
        }
        LOG_LEVEL = logLevel;
    }


    /**
     * 2. Log.x(tag, message)
     *
     * @param tag
     * @param message
     *
     */
    public static void v (final String tag, final String message) {
        print(VERBOSE, tag, strict(message));
    }
    public static void d (final String tag, final String message) {
        print(DEBUG, tag, strict(message));
    }
    public static void i (final String tag, final String message) {
        print(INFO, tag, strict(message));
    }
    public static void w (final String tag, final String message) {
        print(WARN, tag, strict(message));
    }
    public static void e (final String tag, final String message) {
        print(ERROR, tag, strict(message));
    }


    /**
     * 3. Log.x(tag, format, obj1, obj2, ... )
     *
     * @param tag
     * @param format/string
     * @param ... objs [option]
     * @throws IllegalFormatException
     *
     * Sample Code:
     *
     *   Log.d(TAG, "%s %d", "Hello", 2014);
     *
     *   output >> Hello 2014
     */
    public static void v (final String tag, final String format, final Object ... objs) {
        print(VERBOSE, tag, strict(format, objs));
    }
    public static void d (final String tag, final String format, final Object ... objs) {
        print(DEBUG, tag, strict(format, objs));
    }
    public static void i (final String tag, final String format, final Object ... objs) {
        print(INFO, tag, strict(format, objs));
    }
    public static void w (final String tag, final String format, final Object ... objs) {
        print(WARN, tag, strict(format, objs));
    }
    public static void e (final String tag, final String format, final Object ... objs) {
        print(ERROR, tag, strict(format, objs));
    }


    /**
     * The Sub-Function of Log.x()
     *
     * Strict the string, and never return null or ""
     *
     * @param format
     * @param objs [option]
     * @return
     */
    private static String strict(final String format, final Object ... objs) {
        if (format == null) {
            return " ";
        }

        String s = String.format(format, objs);  // throw IllegalFormatException
        return strict(s);
    }
    // @Polymorphism
    private static String strict(final String string) {
        if (string == null || string.length() == 0) {
            return " ";
        }
        return string;
    }


    /**
     * The Sub-Function of 'Log.x'
     *
     * filter the log level, and handle the large message (over than 4000)
     *
     * @param logLevel
     * @param tag
     * @param message
     */
    private static void print(final int logLevel, final String tag, final String message) {

        // message has already been strict, so we don't check again here

        // filter the disabled log level
        if ((logLevel & LOG_LEVEL) == 0) {
            return;
        }

        // 1. Small Message
        // print by android log directly
        if (message.length() <= LOG_MAX_SIZE) {
            printByAndroidLog(logLevel, tag, message);
            return;
        }

        // 2. Large Message
        // cut the message into each small size, and print in order
        int chunkCount = message.length() / LOG_MAX_SIZE;
        for (int i = 0; i <= chunkCount; i++) {

            // last string length is remainder of string.length() / LOG_MAX_SIZE
            int stringLength = i == chunkCount ? message.length() % LOG_MAX_SIZE : LOG_MAX_SIZE;

            int start = LOG_MAX_SIZE * i,
                end = start + stringLength;

            printByAndroidLog(logLevel, tag, message.substring(start, end));
        }
    }

    /**
     * The Sub-Function of 'print'
     *
     * print the message by android.util.Log
     *
     * @param logLevel
     * @param tag
     * @param message
     * @return message length
     */
    private static int printByAndroidLog(final int logLevel, final String tag, final String message) {

        // All the arguments has been check in function 'print'
        // so we don't check again here

        switch (logLevel) {
            case VERBOSE:  return android.util.Log.v(tag, message);
            case DEBUG:    return android.util.Log.d(tag, message);
            case INFO:     return android.util.Log.i(tag, message);
            case WARN:     return android.util.Log.w(tag, message);
            case ERROR:    return android.util.Log.e(tag, message);
            default:
                android.util.Log.w(TAG, strict("[Error] Unknown Log Level (%d)", logLevel));
                android.util.Log.e(tag, message);
                return 0;
        }
    };
}
