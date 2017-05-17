package ua.a5.newnotes.model;

import java.io.Serializable;

/**
 * Created by A5 Android Intern 2 on 12.05.2017.
 */

public class Note implements Serializable {

    private String date;
    private String time;
    private String category;
    private String title;
    private int isImportant;
    private String fullText;
    private String shortText;


    public Note(String category, String title, int isImportant, String fullText) {
        this.category = category;
        this.title = title;
        this.isImportant = isImportant;
        this.fullText = fullText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(int isImportant) {
        this.isImportant = isImportant;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }


    @Override
    public String toString() {
        return "Note{" +
                " date=" + date +
                ", time=" + time +
                ", category=" + category +
                ", title=" + title +
                ", isImportant=" + isImportant +
                ", fullText=" + fullText +
                ", shortText=" + shortText +
                '}';
    }
}
