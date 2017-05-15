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
import ua.a5.newnotes.adapter.notesListAdapters.DifferentListAdapter;
import ua.a5.newnotes.dto.notesDTO.DifferentDTO;
import ua.a5.newnotes.fragments.AbstractTabFragment;

/**
 * Created by A5 Android Intern 2 on 28.04.2017.
 */

public class DifferentFragment extends AbstractTabFragment {
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
        DifferentListAdapter adapter = new DifferentListAdapter(createMockDifferentListData());
        //adapter = new EventsListAdapter(data);
        recyclerView.setAdapter(adapter);

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

}
