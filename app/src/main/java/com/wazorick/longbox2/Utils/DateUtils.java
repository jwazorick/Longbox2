package com.wazorick.longbox2.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String convertLongToDateString(long epochDate) {
        Date date = new Date(epochDate);
        DateFormat dateFormat = new SimpleDateFormat("MM:dd:yyyy", Locale.US);
        return dateFormat.format(date);
    }
}
