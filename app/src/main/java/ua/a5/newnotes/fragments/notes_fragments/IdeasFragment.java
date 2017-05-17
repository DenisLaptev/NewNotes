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
import ua.a5.newnotes.activities.notes_activities.IdeaActivity;
import ua.a5.newnotes.adapter.notesListAdapters.IdeasListAdapter;
import ua.a5.newnotes.dto.notesDTO.IdeaDTO;
import ua.a5.newnotes.fragments.AbstractTabFragment;


/**
 * Created by A5 Android Intern 2 on 28.04.2017.
 */

public class IdeasFragment extends AbstractTabFragment implements IdeasListAdapter.IdeaClickListener {
    public static final String KEY_IDEA_DTO = "key idea dto";

    private static final int LAYOUT = R.layout.fragment_ideas;


    public static IdeasFragment getInstance(Context context) {
        Bundle args = new Bundle();
        IdeasFragment fragment = new IdeasFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_ideas));
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_ideas);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        IdeasListAdapter adapter = new IdeasListAdapter(context, createMockIdeasListData(), this);
        //adapter = new EventsListAdapter(data);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_notes);
        fab.attachToRecyclerView(recyclerView);

        return view;
    }

    //Метод заглушка, возвращает некий список, в последствии список будет из сервера.
    private List<IdeaDTO> createMockIdeasListData() {
        List<IdeaDTO> ideasData = new ArrayList<>();
        ideasData.add(new IdeaDTO("Title 1", "description 1", "Date 1"));
        ideasData.add(new IdeaDTO("Title 2", "description 2", "Date 2"));
        ideasData.add(new IdeaDTO("Title 3", "description 3", "Date 3"));
        ideasData.add(new IdeaDTO("Title 4", "description 4", "Date 4"));
        ideasData.add(new IdeaDTO("Title 5", "description 5", "Date 5"));
        ideasData.add(new IdeaDTO("Title 6", "description 6", "Date 6"));
        return ideasData;
    }

    @Override
    public void onClick(IdeaDTO ideaDTO) {
        Intent intent = new Intent(getContext(), IdeaActivity.class);
        intent.putExtra(KEY_IDEA_DTO, ideaDTO);
        startActivity(intent);
        Toast.makeText(getContext(), ideaDTO.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
