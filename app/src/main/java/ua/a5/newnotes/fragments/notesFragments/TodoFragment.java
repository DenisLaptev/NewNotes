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
import ua.a5.newnotes.adapter.notesListAdapters.TodoListAdapter;
import ua.a5.newnotes.dto.notesDTO.TodoDTO;
import ua.a5.newnotes.fragments.AbstractTabFragment;


/**
 * Created by A5 Android Intern 2 on 28.04.2017.
 */

public class TodoFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_todo;


    public static TodoFragment getInstance(Context context) {
        Bundle args = new Bundle();
        TodoFragment fragment = new TodoFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_todo));
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        TodoListAdapter adapter = new TodoListAdapter(createMockTodoListData());
        recyclerView.setAdapter(adapter);

        return view;
    }

    //Метод заглушка, возвращает некий список, в последствии список будет из сервера.
    private List<TodoDTO> createMockTodoListData() {
        List<TodoDTO> todoData = new ArrayList<>();
        todoData.add(new TodoDTO("Title 1", "todo 1", "Date 1"));
        todoData.add(new TodoDTO("Title 2", "todo 2", "Date 2"));
        todoData.add(new TodoDTO("Title 3", "todo 3", "Date 3"));
        todoData.add(new TodoDTO("Title 4", "todo 4", "Date 4"));
        todoData.add(new TodoDTO("Title 5", "todo 5", "Date 5"));
        todoData.add(new TodoDTO("Title 6", "todo 6", "Date 6"));
        return todoData;
    }


}
