package ua.a5.newnotes.dto.notesDTO;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class BirthdaysDTO {
    private String name;
    private String date;

    public BirthdaysDTO(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
