package ua.a5.newnotes.dto.notesDTO;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class TodoDTO {
    private String title;
    private String todo;
    private String date;

    public TodoDTO(String title, String todo, String date) {
        this.title = title;
        this.todo = todo;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
