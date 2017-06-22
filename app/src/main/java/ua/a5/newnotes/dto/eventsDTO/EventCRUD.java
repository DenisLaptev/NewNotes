package ua.a5.newnotes.dto.eventsDTO;

import java.util.ArrayList;

/**
 * Created by A5 Android Intern 2 on 15.06.2017.
 */

public class EventCRUD {
    ArrayList<EventDTO> eventDTOs;

    public EventCRUD(ArrayList<EventDTO> eventDTOs) {
        this.eventDTOs = eventDTOs;
    }


    //ADD (CREATE)
    public boolean addNew(EventDTO eventDTO) {
        try {
            eventDTOs.add(eventDTO);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    //RETRIEVE (извлекать) (READ)
    public ArrayList<EventDTO> getEventDTOs() {
        return eventDTOs;
    }


    //UPDATE
    public boolean update(int position, EventDTO newEventDTO) {
        try {
            eventDTOs.remove(position);
            eventDTOs.add(position, newEventDTO);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    //DELETE
    public boolean delete(int position) {
        try {
            eventDTOs.remove(position);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}











