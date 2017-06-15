package ua.a5.newnotes.utils.utils_spannable_string;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentDay;


/**
 * Created by Lenovo on 24.05.2017.
 */

public class UtilsWords {

    public static List<String> generateDateRegExps() {
        List<String> dateRegExps = new ArrayList<>();

        //12 апр 2000, 8 марта, 31 декабря 2001.
        String regExp1 = "(\\d{1,2})(\\s*)(января|янв|февраля|фев|марта|мар|апреля|апр|май|мая|июня|июн|июля|июл|августа|авг|сентября|сен|октября|окт|ноября|ноя|декабря|дек)(\\s*)(\\d{2,4}){0,1}";
        dateRegExps.add(regExp1);

        //12-04, 12.04, 31.12.2001.
        String regExp2 = "(\\d{1,2})(\\.|-)(\\d{1,2})(\\.|-){0,1}(\\d{2,4}){0,1}";
        dateRegExps.add(regExp2);


        return dateRegExps;
    }

    public static Map<String, Integer> generateTimeWords() {
        Map<String, Integer> timeWords = new HashMap<>();


        timeWords.put("позавчера", getCurrentDay() - 2);
        timeWords.put("Позавчера", getCurrentDay() - 2);
        timeWords.put("ПОЗАВЧЕРА", getCurrentDay() - 2);

        timeWords.put("day before yesterday", getCurrentDay() - 2);
        timeWords.put("Day before yesterday", getCurrentDay() - 2);
        timeWords.put("DAY BEFORE YESTERDAY", getCurrentDay() - 2);
        //////////////////////////////////////////////////////////////////////


        timeWords.put("вчера", getCurrentDay() - 1);
        timeWords.put("Вчера", getCurrentDay() - 1);
        timeWords.put("ВЧЕРА", getCurrentDay() - 1);

        timeWords.put("yesterday", getCurrentDay() - 1);
        timeWords.put("Yesterday", getCurrentDay() - 1);
        timeWords.put("YESTERDAY", getCurrentDay() - 1);
        //////////////////////////////////////////////////////////////////////


        timeWords.put("сегодня", getCurrentDay());
        timeWords.put("Сегодня", getCurrentDay());
        timeWords.put("СЕГОДНЯ", getCurrentDay());

        timeWords.put("today", getCurrentDay());
        timeWords.put("Today", getCurrentDay());
        timeWords.put("TODAY", getCurrentDay());

        //timeWords.put("(.*)(\\s)(\\d{1,2}) (янв|января|фев|февраля|мар|марта|апр|апреля|май|мая|июн|июня|июл|июля|авг|августа|сен|сентября|окт|октября|ноя|ноября|дек|декабря) (\\d{2,4}){0,1}(\\s)(.*)", getCurrentDay());
        //////////////////////////////////////////////////////////////////////


        timeWords.put(" завтра", getCurrentDay() + 1);
        timeWords.put("Завтра", getCurrentDay() + 1);
        timeWords.put("ЗАВТРА", getCurrentDay() + 1);

        timeWords.put("tomorrow", getCurrentDay() + 1);
        timeWords.put("Tomorrow", getCurrentDay() + 1);
        timeWords.put("TOMORROW", getCurrentDay() + 1);
        //////////////////////////////////////////////////////////////////////


        timeWords.put("послезавтра", getCurrentDay() + 2);
        timeWords.put("Послезавтра", getCurrentDay() + 2);
        timeWords.put("ПОСЛЕЗАВТРА", getCurrentDay() + 2);

        timeWords.put("day after tomorrow", getCurrentDay() + 2);
        timeWords.put("Day after tomorrow", getCurrentDay() + 2);
        timeWords.put("DAY AFTER TOMORROW", getCurrentDay() + 2);
        //////////////////////////////////////////////////////////////////////
        return timeWords;
    }

    public static Map<String, Integer> generateDaysOfTeWeekMap() {
        Map<String, Integer> daysOfTheWeek = new HashMap<>();

        daysOfTheWeek.put("пн", 1);
        daysOfTheWeek.put("пон", 1);
        daysOfTheWeek.put("пнд", 1);
        daysOfTheWeek.put("понедельник", 1);
        daysOfTheWeek.put("понедельника", 1);
        daysOfTheWeek.put("понедельнику", 1);
        daysOfTheWeek.put("Пн", 1);
        daysOfTheWeek.put("Пон", 1);
        daysOfTheWeek.put("Пнд", 1);
        daysOfTheWeek.put("Понедельник", 1);
        daysOfTheWeek.put("ПН", 1);
        daysOfTheWeek.put("ПОН", 1);
        daysOfTheWeek.put("ПНД", 1);
        daysOfTheWeek.put("ПОНЕДЕЛЬНИК", 1);

        daysOfTheWeek.put("Mo", 1);
        daysOfTheWeek.put("mon", 1);
        daysOfTheWeek.put("monday", 1);
        daysOfTheWeek.put("Mon", 1);
        daysOfTheWeek.put("Monday", 1);
        daysOfTheWeek.put("MON", 1);
        daysOfTheWeek.put("MONDAY", 1);
        //////////////////////////////////////////////////////////////////////

        /*
        daysOfTheWeek.put("вт", 2);
        daysOfTheWeek.put("вто", 2);
        daysOfTheWeek.put("втр", 2);
        daysOfTheWeek.put("вторник", 2);
        daysOfTheWeek.put("вторника", 2);
        daysOfTheWeek.put("вторнику", 2);
        daysOfTheWeek.put("Вт", 2);
        daysOfTheWeek.put("Вто", 2);
        daysOfTheWeek.put("Втр", 2);
        daysOfTheWeek.put("Вторник", 2);
        daysOfTheWeek.put("ВТ", 2);
        daysOfTheWeek.put("ВТО", 2);
        daysOfTheWeek.put("ВТР", 2);
        daysOfTheWeek.put("ВТОРНИК", 2);

        daysOfTheWeek.put("Tu", 2);
        daysOfTheWeek.put("tue", 2);
        daysOfTheWeek.put("tues", 2);
        daysOfTheWeek.put("tuesday", 2);
        daysOfTheWeek.put("Tue", 2);
        daysOfTheWeek.put("Tues", 2);
        daysOfTheWeek.put("Tuesday", 2);
        daysOfTheWeek.put("TUE", 2);
        daysOfTheWeek.put("TUES", 2);
        daysOfTheWeek.put("TUESDAY", 2);
        //////////////////////////////////////////////////////////////////////
        */

        daysOfTheWeek.put(" вт", 2);
        daysOfTheWeek.put(" вто", 2);
        daysOfTheWeek.put(" втр", 2);
        daysOfTheWeek.put(" вторник", 2);
        daysOfTheWeek.put(" вторника", 2);
        daysOfTheWeek.put(" вторнику", 2);
        daysOfTheWeek.put(" Вт", 2);
        daysOfTheWeek.put(" Вто", 2);
        daysOfTheWeek.put(" Втр", 2);
        daysOfTheWeek.put(" Вторник", 2);
        daysOfTheWeek.put(" ВТ", 2);
        daysOfTheWeek.put(" ВТО", 2);
        daysOfTheWeek.put(" ВТР", 2);
        daysOfTheWeek.put(" ВТОРНИК", 2);

        daysOfTheWeek.put(" Tu", 2);
        daysOfTheWeek.put(" tue", 2);
        daysOfTheWeek.put(" tues", 2);
        daysOfTheWeek.put(" tuesday", 2);
        daysOfTheWeek.put(" Tue", 2);
        daysOfTheWeek.put(" Tues", 2);
        daysOfTheWeek.put(" Tuesday", 2);
        daysOfTheWeek.put(" TUE", 2);
        daysOfTheWeek.put(" TUES", 2);
        daysOfTheWeek.put(" TUESDAY", 2);




        daysOfTheWeek.put("вт ", 2);
        daysOfTheWeek.put("вто ", 2);
        daysOfTheWeek.put("втр ", 2);
        daysOfTheWeek.put("вторник ", 2);
        daysOfTheWeek.put("вторника ", 2);
        daysOfTheWeek.put("вторнику ", 2);
        daysOfTheWeek.put("Вт ", 2);
        daysOfTheWeek.put("Вто ", 2);
        daysOfTheWeek.put("Втр ", 2);
        daysOfTheWeek.put("Вторник ", 2);
        daysOfTheWeek.put("ВТ ", 2);
        daysOfTheWeek.put("ВТО ", 2);
        daysOfTheWeek.put("ВТР ", 2);
        daysOfTheWeek.put("ВТОРНИК ", 2);

        daysOfTheWeek.put("Tu ", 2);
        daysOfTheWeek.put("tue ", 2);
        daysOfTheWeek.put("tues ", 2);
        daysOfTheWeek.put("tuesday ", 2);
        daysOfTheWeek.put("Tue ", 2);
        daysOfTheWeek.put("Tues ", 2);
        daysOfTheWeek.put("Tuesday ", 2);
        daysOfTheWeek.put("TUE ", 2);
        daysOfTheWeek.put("TUES ", 2);
        daysOfTheWeek.put("TUESDAY ", 2);
        //////////////////////////////////////////////////////////////////////




        daysOfTheWeek.put("ср", 3);
        daysOfTheWeek.put("сре", 3);
        daysOfTheWeek.put("срд", 3);
        daysOfTheWeek.put("среда", 3);
        daysOfTheWeek.put("среде", 3);
        daysOfTheWeek.put("среду", 3);
        daysOfTheWeek.put("среды", 3);
        daysOfTheWeek.put("Ср", 3);
        daysOfTheWeek.put("Сре", 3);
        daysOfTheWeek.put("Срд", 3);
        daysOfTheWeek.put("Среда", 3);
        daysOfTheWeek.put("СР", 3);
        daysOfTheWeek.put("СРЕ", 3);
        daysOfTheWeek.put("СРД", 3);
        daysOfTheWeek.put("СРЕДА", 3);

        daysOfTheWeek.put("We", 3);
        daysOfTheWeek.put("wed", 3);
        daysOfTheWeek.put("wednesday", 3);
        daysOfTheWeek.put("Wed", 3);
        daysOfTheWeek.put("Wednesday", 3);
        daysOfTheWeek.put("WED", 3);
        daysOfTheWeek.put("WEDNESDAY", 3);
        //////////////////////////////////////////////////////////////////////


        daysOfTheWeek.put("чт", 4);
        daysOfTheWeek.put("чет", 4);
        daysOfTheWeek.put("чтв", 4);
        daysOfTheWeek.put("четверг", 4);
        daysOfTheWeek.put("четверга", 4);
        daysOfTheWeek.put("четвергу", 4);
        daysOfTheWeek.put("Чт", 4);
        daysOfTheWeek.put("Чет", 4);
        daysOfTheWeek.put("Чтв", 4);
        daysOfTheWeek.put("Четверг", 4);
        daysOfTheWeek.put("ЧТ", 4);
        daysOfTheWeek.put("ЧЕТ", 4);
        daysOfTheWeek.put("ЧТВ", 4);
        daysOfTheWeek.put("ЧЕТВЕРГ", 4);

        daysOfTheWeek.put("Th", 4);
        daysOfTheWeek.put("thu", 4);
        daysOfTheWeek.put("thur", 4);
        daysOfTheWeek.put("thursday", 4);
        daysOfTheWeek.put("Thu", 4);
        daysOfTheWeek.put("Thur", 4);
        daysOfTheWeek.put("Thursday", 4);
        daysOfTheWeek.put("THU", 4);
        daysOfTheWeek.put("THUR", 4);
        daysOfTheWeek.put("THURSDAY", 4);
        //////////////////////////////////////////////////////////////////////


        daysOfTheWeek.put("пт", 5);
        daysOfTheWeek.put("пят", 5);
        daysOfTheWeek.put("птн", 5);
        daysOfTheWeek.put("пятница", 5);
        daysOfTheWeek.put("пятнице", 5);
        daysOfTheWeek.put("пятницы", 5);
        daysOfTheWeek.put("пятницу", 5);
        daysOfTheWeek.put("Пт", 5);
        daysOfTheWeek.put("Пят", 5);
        daysOfTheWeek.put("Птн", 5);
        daysOfTheWeek.put("Пятница", 5);
        daysOfTheWeek.put("ПТ", 5);
        daysOfTheWeek.put("ПЯТ", 5);
        daysOfTheWeek.put("ПТН", 5);
        daysOfTheWeek.put("ПЯТНИЦА", 5);

        daysOfTheWeek.put("Fr", 1);
        daysOfTheWeek.put("fri", 5);
        daysOfTheWeek.put("friday", 5);
        daysOfTheWeek.put("Fri", 5);
        daysOfTheWeek.put("Friday", 5);
        daysOfTheWeek.put("FRI", 5);
        daysOfTheWeek.put("FRIDAY", 5);
        //////////////////////////////////////////////////////////////////////


        daysOfTheWeek.put("сбб", 6);
        daysOfTheWeek.put("сб", 6);
        daysOfTheWeek.put("суб", 6);
        daysOfTheWeek.put("сбт", 6);
        daysOfTheWeek.put("суббота", 6);
        daysOfTheWeek.put("субботе", 6);
        daysOfTheWeek.put("субботы", 6);
        daysOfTheWeek.put("субботу", 6);
        daysOfTheWeek.put("Сбб", 6);
        daysOfTheWeek.put("Сб", 6);
        daysOfTheWeek.put("Суб", 6);
        daysOfTheWeek.put("Сбт", 6);
        daysOfTheWeek.put("Суббота", 6);
        daysOfTheWeek.put("СББ", 6);
        daysOfTheWeek.put("СБ", 6);
        daysOfTheWeek.put("СУБ", 6);
        daysOfTheWeek.put("СБТ", 6);
        daysOfTheWeek.put("СУББОТА", 6);

        daysOfTheWeek.put("Sa", 6);
        daysOfTheWeek.put("sat", 6);
        daysOfTheWeek.put("saturday", 6);
        daysOfTheWeek.put("Sat", 6);
        daysOfTheWeek.put("Saturday", 6);
        daysOfTheWeek.put("SAT", 6);
        daysOfTheWeek.put("SATURDAY", 6);
        //////////////////////////////////////////////////////////////////////


        daysOfTheWeek.put("вс", 7);
        daysOfTheWeek.put("вос", 7);
        daysOfTheWeek.put("вск", 7);
        daysOfTheWeek.put("воскресенье", 7);
        daysOfTheWeek.put("воскресенью", 7);
        daysOfTheWeek.put("воскресенья", 7);
        daysOfTheWeek.put("Вс", 7);
        daysOfTheWeek.put("Вос", 7);
        daysOfTheWeek.put("Вск", 7);
        daysOfTheWeek.put("Воскресенье", 7);
        daysOfTheWeek.put("ВС", 7);
        daysOfTheWeek.put("ВОС", 7);
        daysOfTheWeek.put("ВСК", 7);
        daysOfTheWeek.put("ВОСКРЕСЕНЬЕ", 7);

        daysOfTheWeek.put("Su", 7);
        daysOfTheWeek.put("sun", 7);
        daysOfTheWeek.put("sunday", 7);
        daysOfTheWeek.put("Sun", 7);
        daysOfTheWeek.put("Sunday", 7);
        daysOfTheWeek.put("SUN", 7);
        daysOfTheWeek.put("SUNDAY", 7);
        //////////////////////////////////////////////////////////////////////
        return daysOfTheWeek;
    }


    public static int getIntMonthFromString(String month) {
        int intMonth = 0;
        switch (month) {
            case "янв": {
                intMonth = 0;
                break;
            }
            case "января": {
                intMonth = 0;
                break;
            }
            case "01": {
                intMonth = 0;
                break;
            }
            case "1": {
                intMonth = 0;
                break;
            }
            case "фев": {
                intMonth = 1;
                break;
            }
            case "февраля": {
                intMonth = 1;
                break;
            }
            case "02": {
                intMonth = 1;
                break;
            }
            case "2": {
                intMonth = 1;
                break;
            }
            case "мар": {
                intMonth = 2;
                break;
            }
            case "марта": {
                intMonth = 2;
                break;
            }
            case "03": {
                intMonth = 2;
                break;
            }
            case "3": {
                intMonth = 2;
                break;
            }
            case "апр": {
                intMonth = 3;
                break;
            }
            case "апреля": {
                intMonth = 3;
                break;
            }
            case "04": {
                intMonth = 3;
                break;
            }
            case "4": {
                intMonth = 3;
                break;
            }
            case "май": {
                intMonth = 4;
                break;
            }
            case "мая": {
                intMonth = 4;
                break;
            }
            case "05": {
                intMonth = 4;
                break;
            }
            case "5": {
                intMonth = 4;
                break;
            }
            case "июн": {
                intMonth = 5;
                break;
            }
            case "июня": {
                intMonth = 5;
                break;
            }
            case "06": {
                intMonth = 5;
                break;
            }
            case "6": {
                intMonth = 5;
                break;
            }
            case "июл": {
                intMonth = 6;
                break;
            }
            case "июля": {
                intMonth = 6;
                break;
            }
            case "07": {
                intMonth = 6;
                break;
            }
            case "7": {
                intMonth = 6;
                break;
            }
            case "авг": {
                intMonth = 7;
                break;
            }
            case "августа": {
                intMonth = 7;
                break;
            }
            case "08": {
                intMonth = 7;
                break;
            }
            case "8": {
                intMonth = 7;
                break;
            }
            case "сен": {
                intMonth = 8;
                break;
            }
            case "сентября": {
                intMonth = 8;
                break;
            }
            case "09": {
                intMonth = 8;
                break;
            }
            case "9": {
                intMonth = 8;
                break;
            }
            case "окт": {
                intMonth = 9;
                break;
            }
            case "октября": {
                intMonth = 9;
                break;
            }
            case "10": {
                intMonth = 9;
                break;
            }
            case "ноя": {
                intMonth = 10;
                break;
            }
            case "ноября": {
                intMonth = 10;
                break;
            }
            case "11": {
                intMonth = 10;
                break;
            }
            case "дек": {
                intMonth = 11;
                break;
            }
            case "декабря": {
                intMonth = 11;
                break;
            }
            case "12": {
                intMonth = 11;
                break;
            }
        }
        return intMonth;
    }
}
