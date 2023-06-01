package de.tiiita.earobot.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created on März 19, 2023 | 18:39:44
 * (●'◡'●)
 */
public class TimeUtil {


    /**
     *
     * @param pattern the pattern you want the time in. Type null if you want the default one.
     * @return a string with the time in the wanted pattern.
     */
    public static String getTime(String pattern) {
        String defaultPattern = "MM/dd/yyyy - hh:mm a";
        DateFormat dateFormat;
        if (pattern == null) {
               dateFormat = new SimpleDateFormat(defaultPattern);
        } else dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }
}
