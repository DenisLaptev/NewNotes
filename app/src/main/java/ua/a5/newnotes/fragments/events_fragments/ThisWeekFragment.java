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

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import ua.a5.newnotes.R;
import ua.a5.newnotes.activities.events_activities.EventActivity;
import ua.a5.newnotes.adapter.eventsListAdapters.EventsListAdapter;
import ua.a5.newnotes.dto.eventsDTO.EventDTO;
import ua.a5.newnotes.fragments.AbstractTabFragment;


/**
 * Created by A5 Android Intern 2 on 28.04.2017.
 */

public class ThisWeekFragment extends AbstractTabFragment implements EventsListAdapter.EventClickListener {
    public static final String KEY_EVENT_DTO = "key event dto";

    private static final int LAYOUT = R.layout.fragment_events;


    public static ThisWeekFragment getInstance(Context context) {
        Bundle args = new Bundle();
        ThisWeekFragment fragment = new ThisWeekFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.menu_events_item_thisweek));
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        EventsListAdapter adapter = new EventsListAdapter(context, createMockEventsListData(), this);
        //adapter = new EventsListAdapter(data);
        recyclerView.setAdapter(adapter);

        FloatingActionsMenu fab = (FloatingActionsMenu) getActivity().findViewById(R.id.multiple_actions_events);

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
