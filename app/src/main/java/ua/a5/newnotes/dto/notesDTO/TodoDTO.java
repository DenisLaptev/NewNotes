package ua.a5.newnotes.dto.notesDTO;

import java.io.Serializable;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class TodoDTO implements Serializable {
    private String title;
    private int isDone;
    private int day;
    private int month;
    private int year;
    private String description;

    public TodoDTO(String title, int isDone, int day, int month, int year, String description) {
        this.title = title;
        this.isDone = isDone;
        this.day = day;
        this.month = month;
        this.year = year;
        this.description = description;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
