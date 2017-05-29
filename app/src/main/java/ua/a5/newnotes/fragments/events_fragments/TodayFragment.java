package ua.a5.newnotes.fragments.events_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import ua.a5.newnotes.R;
import ua.a5.newnotes.activities.CreateEventActivity;
import ua.a5.newnotes.activities.events_activities.EventActivity;
import ua.a5.newnotes.adapter.eventsListAdapters.EventsListAdapter;
import ua.a5.newnotes.dto.eventsDTO.EventDTO;
import ua.a5.newnotes.fragments.AbstractTabFragment;


/**
 * Created by A5 Android Intern 2 on 28.04.2017.
 */

public class TodayFragment extends AbstractTabFragment implements EventsListAdapter.EventClickListener {
    public static final String KEY_EVENT_DTO = "key event dto";

    private static final int LAYOUT = R.layout.fragment_events;


    public static TodayFragment getInstance(Context context) {
        Bundle args = new Bundle();
        TodayFragment fragment = new TodayFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.menu_events_item_today));
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        EventsListAdapter adapter = new EventsListAdapter(context, createMockEventsListData(), this);
        recyclerView.setAdapter(adapter);


        FloatingActionButton actionEvent = (FloatingActionButton) getActivity().findViewById(R.id.action_event);
        actionEvent.setTitle("new EVENT");
        actionEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateEventActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        FloatingActionButton actionMainmenu = (FloatingActionButton) getActivity().findViewById(R.id.action_events_mainmenu);
        actionMainmenu.setTitle("Main Menu");
        actionMainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) getActivity().findViewById(R.id.multiple_actions_notes);
        //menuMultipleActions.addButton(actionTodo);
        //menuMultipleActions.addButton(actionIdea);
        //menuMultipleActions.addButton(actionBirthday);
        //menuMultipleActions.addButton(actionDifferent);



        return view;
    }

    //Метод заглушка, возвращает некий список, в последствии список будет из сервера.
    private List<EventDTO> createMockEventsListData() {
        List<EventDTO> eventsData = new ArrayList<>();
        eventsData.add(new EventDTO("Title 1", "description 1", "Date 1"));
        eventsData.add(new EventDTO("Title 2", "description 2", "Date 2"));
        eventsData.add(new EventDTO("Title 3", "description 3", "Date 3"));
        eventsData.add(new EventDTO("Title 4", "description 4", "Date 4"));
        eventsData.add(new EventDTO("Title 5", "description 5", "Date 5"));
        eventsData.add(new EventDTO("Title 6", "description 6", "Date 6"));
        return eventsData;
    }

    @Override
    public void onClick(EventDTO eventDTO) {
        Intent intent = new Intent(getContext(), EventActivity.class);
        intent.putExtra(KEY_EVENT_DTO, eventDTO);
        startActivity(intent);
        Toast.makeText(getContext(), eventDTO.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
