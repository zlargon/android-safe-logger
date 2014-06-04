BetterAndroidLogger
===================

Improved `android.util.Log` static function

### 1. check null and empty string

    android.util.Log.d(TAG, null);  // Throw NullPointException
    android.util.Log.d(TAG, "");    // Show nothing in adb logcat → we don't even know whether the code is executed or not

This logger will print `null` as `"(null)"`, and print `""` as a space blank

    Log.d(TAG, null);  // android.util.Log.d(TAG, "(null)");
    Log.d(TAG, "");    // android.util.Log.d(TAG, " ");

### 2. support string format with indefinite parameter 'objs'

you can use this logger just like `String.format`

    Log.d(TAG, "%s %d", "Hello", 2014);  // output >> Hello 2014
   
### 3. support to print large size message

`android.util.Log` can not print the message over than 4000 characters in `adb logcat`

This logger can help you to cut the large message into small size substring, and print them in the order.

### 4. support to configure log level

There are 6 level `Log.VERBOSE`, `Log.DEBUG`, `Log.INFO`, `Log.WARN`, `Log.ERROR`, `Log.ALL_LEVEL` to configure.

    Log.setLevel(Log.DEBUG | Log.INFO);  // show Debug and Info log level only
