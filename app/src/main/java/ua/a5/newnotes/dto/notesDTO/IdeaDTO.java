package ua.a5.newnotes.dto.notesDTO;

import java.io.Serializable;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class IdeaDTO implements Serializable {
    private String title;
    private String description;
    private String date;

    public IdeaDTO(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
