package ua.a5.newnotes.fragments.eventsFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ua.a5.newnotes.R;
import ua.a5.newnotes.adapter.eventsListAdapters.EventsListAdapter;
import ua.a5.newnotes.dto.eventsDTO.EventsDTO;
import ua.a5.newnotes.fragments.AbstractTabFragment;


/**
 * Created by A5 Android Intern 2 on 28.04.2017.
 */

public class ThisMonthFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_events;


    public static ThisMonthFragment getInstance(Context context) {
        Bundle args = new Bundle();
        ThisMonthFragment fragment = new ThisMonthFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.menu_events_item));
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        EventsListAdapter adapter = new EventsListAdapter(createMockEventsListData());
        //adapter = new EventsListAdapter(data);
        recyclerView.setAdapter(adapter);

        return view;
    }

    //Метод заглушка, возвращает некий список, в последствии список будет из сервера.
    private List<EventsDTO> createMockEventsListData() {
        List<EventsDTO> eventsData = new ArrayList<>();
        eventsData.add(new EventsDTO("Title 1", "description 1", "Date 1"));
        eventsData.add(new EventsDTO("Title 2", "description 2", "Date 2"));
        eventsData.add(new EventsDTO("Title 3", "description 3", "Date 3"));
        eventsData.add(new EventsDTO("Title 4", "description 4", "Date 4"));
        eventsData.add(new EventsDTO("Title 5", "description 5", "Date 5"));
        eventsData.add(new EventsDTO("Title 6", "description 6", "Date 6"));
        return eventsData;
    }
}
