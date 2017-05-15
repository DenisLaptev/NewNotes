package ua.a5.newnotes.dto;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class RemindDTO {
    private String title;

    public RemindDTO(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
