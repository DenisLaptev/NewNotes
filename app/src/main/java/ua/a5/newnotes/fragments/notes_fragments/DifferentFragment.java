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
import ua.a5.newnotes.activities.notes_activities.DifferentActivity;
import ua.a5.newnotes.adapter.notesListAdapters.DifferentListAdapter;
import ua.a5.newnotes.dto.notesDTO.DifferentDTO;
import ua.a5.newnotes.fragments.AbstractTabFragment;

/**
 * Created by A5 Android Intern 2 on 28.04.2017.
 */

public class DifferentFragment extends AbstractTabFragment implements DifferentListAdapter.DifferentClickListener {
    public static final String KEY_DIFFERENT_DTO = "key different dto";

    private static final int LAYOUT = R.layout.fragment_different;

    public static DifferentFragment getInstance(Context context) {
        Bundle args = new Bundle();
        DifferentFragment fragment = new DifferentFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_different));
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_different);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DifferentListAdapter adapter = new DifferentListAdapter(context, createMockDifferentListData(), this);
        //adapter = new EventsListAdapter(data);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_notes);
        fab.attachToRecyclerView(recyclerView);

        return view;
    }


    //Метод заглушка, возвращает некий список, в последствии список будет из сервера.
    private List<DifferentDTO> createMockDifferentListData() {
        List<DifferentDTO> differentData = new ArrayList<>();
        differentData.add(new DifferentDTO("Title 1", "description 1", "Date 1"));
        differentData.add(new DifferentDTO("Title 2", "description 2", "Date 2"));
        differentData.add(new DifferentDTO("Title 3", "description 3", "Date 3"));
        differentData.add(new DifferentDTO("Title 4", "description 4", "Date 4"));
        differentData.add(new DifferentDTO("Title 5", "description 5", "Date 5"));
        differentData.add(new DifferentDTO("Title 6", "description 6", "Date 6"));
        return differentData;
    }

    @Override
    public void onClick(DifferentDTO differentDTO) {
        Intent intent = new Intent(getContext(), DifferentActivity.class);
        intent.putExtra(KEY_DIFFERENT_DTO, differentDTO);
        startActivity(intent);
        Toast.makeText(getContext(), differentDTO.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
