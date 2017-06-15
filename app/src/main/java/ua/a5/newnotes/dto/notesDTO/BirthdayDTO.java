package ua.a5.newnotes.dto.notesDTO;

import java.io.Serializable;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class BirthdayDTO implements Serializable {
    private String name;
    private int day;
    private int month;
    private String stringMonth;
    private int year;

    public BirthdayDTO(String name, int day, int month, int year) {
        this.name = name;
        this.day = day;
        this.month = month;
        stringMonth = generateStringMonth(month);
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getStringMonth() {
        return stringMonth;
    }

    public void setStringMonth(String stringMonth) {
        this.stringMonth = stringMonth;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    public String generateStringMonth(int month){
        String stringMonth = null;
        switch (month){
            case 0:
                stringMonth = "января";
                break;

            case 1:
                stringMonth = "февраля";
                break;

            case 2:
                stringMonth = "марта";
                break;

            case 3:
                stringMonth = "апреля";
                break;

            case 4:
                stringMonth = "мая";
                break;

            case 5:
                stringMonth = "июня";
                break;

            case 6:
                stringMonth = "июля";
                break;

            case 7:
                stringMonth = "августа";
                break;

            case 8:
                stringMonth = "сентября";
                break;

            case 9:
                stringMonth = "октября";
                break;

            case 10:
                stringMonth = "ноября";
                break;

            case 11:
                stringMonth = "декабря";
                break;


        }
        return stringMonth;
    }

}
