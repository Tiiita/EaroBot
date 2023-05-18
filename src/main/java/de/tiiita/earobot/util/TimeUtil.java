package de.tiiita.earobot.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created on März 19, 2023 | 18:39:44
 * (●'◡'●)
 */
public class TimeUtil {

    public static String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy - HH:mm a");
        Date date = new Date(System.currentTimeMillis());

        return dateFormat.format(date);
    }
}
