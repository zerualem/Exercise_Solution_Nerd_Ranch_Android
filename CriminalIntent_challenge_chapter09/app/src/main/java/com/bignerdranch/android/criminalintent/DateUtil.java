package com.bignerdranch.android.criminalintent;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by andre on 13.09.17.
 */

public class DateUtil {

    public static String getStringOfDate(Date date){
        return DateFormat.getDateInstance().format(date);
    }

    public static String getWeekDayOfDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        return getDayOfWeek(day);
    }

    private static String getDayOfWeek(int value) {
        String day = "";
        switch (value) {
            case 1:
                day = "Sunday";
                break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            case 7:
                day = "Saturday";
                break;
        }
        return day;
    }
}
