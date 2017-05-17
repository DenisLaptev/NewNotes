package ua.a5.newnotes.fragments.notes_fragments;

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

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ua.a5.newnotes.R;
import ua.a5.newnotes.activities.notes_activities.BirthdayActivity;
import ua.a5.newnotes.adapter.notesListAdapters.BirthdaysListAdapter;
import ua.a5.newnotes.dto.notesDTO.BirthdayDTO;
import ua.a5.newnotes.fragments.AbstractTabFragment;


/**
 * Created by A5 Android Intern 2 on 28.04.2017.
 */

public class BirthdaysFragment extends AbstractTabFragment implements BirthdaysListAdapter.BirthdayClickListener {
    public static final String KEY_BIRTHDAY_DTO = "key birthday dto";

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
        BirthdaysListAdapter adapter = new BirthdaysListAdapter(context, createMockBirthdaysListData(), this);
        //adapter = new EventsListAdapter(data);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_notes);
        fab.attachToRecyclerView(recyclerView);

        return view;
    }

    //Метод заглушка, возвращает некий список, в последствии список будет из сервера.
    private List<BirthdayDTO> createMockBirthdaysListData() {
        List<BirthdayDTO> birthdaysData = new ArrayList<>();
        birthdaysData.add(new BirthdayDTO("Name 1", "Date 1"));
        birthdaysData.add(new BirthdayDTO("Name 2", "Date 2"));
        birthdaysData.add(new BirthdayDTO("Name 3", "Date 3"));
        birthdaysData.add(new BirthdayDTO("Name 4", "Date 4"));
        birthdaysData.add(new BirthdayDTO("Name 5", "Date 5"));
        birthdaysData.add(new BirthdayDTO("Name 6", "Date 6"));
        return birthdaysData;
    }

    @Override
    public void onClick(BirthdayDTO birthdayDTO) {
        Intent intent = new Intent(getContext(), BirthdayActivity.class);
        intent.putExtra(KEY_BIRTHDAY_DTO, birthdayDTO);
        startActivity(intent);
        Toast.makeText(getContext(), birthdayDTO.getName(), Toast.LENGTH_SHORT).show();
    }
}
