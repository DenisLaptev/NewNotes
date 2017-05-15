package ua.a5.newnotes.fragments.notesFragments;

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
import ua.a5.newnotes.adapter.notesListAdapters.BirthdaysListAdapter;
import ua.a5.newnotes.dto.notesDTO.BirthdaysDTO;
import ua.a5.newnotes.fragments.AbstractTabFragment;


/**
 * Created by A5 Android Intern 2 on 28.04.2017.
 */

public class BirthdaysFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_birthdays;


    public static BirthdaysFragment getInstance(Context context) {
        Bundle args = new Bundle();
        BirthdaysFragment fragment = new BirthdaysFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_birthdays));
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_birthdays);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        BirthdaysListAdapter adapter = new BirthdaysListAdapter(createMockBirthdaysListData());
        //adapter = new EventsListAdapter(data);
        recyclerView.setAdapter(adapter);

        return view;
    }

    //Метод заглушка, возвращает некий список, в последствии список будет из сервера.
    private List<BirthdaysDTO> createMockBirthdaysListData() {
        List<BirthdaysDTO> birthdaysData = new ArrayList<>();
        birthdaysData.add(new BirthdaysDTO("Name 1", "Date 1"));
        birthdaysData.add(new BirthdaysDTO("Name 2", "Date 2"));
        birthdaysData.add(new BirthdaysDTO("Name 3", "Date 3"));
        birthdaysData.add(new BirthdaysDTO("Name 4", "Date 4"));
        birthdaysData.add(new BirthdaysDTO("Name 5", "Date 5"));
        birthdaysData.add(new BirthdaysDTO("Name 6", "Date 6"));
        return birthdaysData;
    }

}
