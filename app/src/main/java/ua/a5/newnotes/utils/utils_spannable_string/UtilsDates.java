package ua.a5.newnotes.utils.utils_spannable_string;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static ua.a5.newnotes.utils.utils_spannable_string.UtilsWords.generateDateRegExps;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsWords.generateDaysOfTeWeekMap;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsWords.generateTimeWords;

/**
 * Created by Lenovo on 24.05.2017.
 */

public class UtilsDates {

    public static Map<String, Integer> DAYS_OF_THE_WEEK = generateDaysOfTeWeekMap();
    public static Map<String, Integer> TIME_WORDS = generateTimeWords();
    public static List<String> DATE_REGEXPS = generateDateRegExps();

    public static int getCurrentDay() {
        int currentDay;
        currentDay = java.util.Calendar.getInstance().get(java.util.Calendar.DATE);
        return currentDay;
    }

    public static int getCurrentDayOfWeek() {
        int currentDayOfWeek;
        currentDayOfWeek = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK) - 1;
        return currentDayOfWeek;
    }

    public static int getCurrentWeek() {
        int currentDay;
        currentDay = java.util.Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        return currentDay;
    }

    public static int getDifferenceBetweenPlannedDayAndToday(String strDay) {
        int differenceBetweenPlannedDayAndToday = 0;
        for (String s : DAYS_OF_THE_WEEK.keySet()) {
            if (s.equals(strDay)) {
                differenceBetweenPlannedDayAndToday = DAYS_OF_THE_WEEK.get(s) - getCurrentDayOfWeek();
                break;
            }
        }

        for (String s : TIME_WORDS.keySet()) {
            if (s.equals(strDay)) {
                differenceBetweenPlannedDayAndToday = TIME_WORDS.get(s) - getCurrentDay();
                break;
            }
        }
        return differenceBetweenPlannedDayAndToday;
    }

    public static int getCurrentMonth() {
        int currentMonth;
        currentMonth = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
        return currentMonth;
    }

    public static int getCurrentYear() {
        int currentYear;
        currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        return currentYear;
    }

    public static int getCurrentHour() {
        int currentHour;
        currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        return currentHour;
    }

    public static int getCurrentMinute() {
        int currentMinute;
        currentMinute = java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE);
        return currentMinute;
    }
}
