package ua.a5.newnotes.dto.eventsDTO;

import java.util.ArrayList;

/**
 * Created by A5 Android Intern 2 on 15.06.2017.
 */

public class EventDTOCollection {
    static ArrayList<EventDTO> eventDTOs = new ArrayList<>();

    public static ArrayList<EventDTO> getEventDTOs(){
        return eventDTOs;
    }
}
