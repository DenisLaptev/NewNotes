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

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import ua.a5.newnotes.R;
import ua.a5.newnotes.activities.CreateNoteActivity;
import ua.a5.newnotes.activities.notes_activities.TodoActivity;
import ua.a5.newnotes.adapter.notesListAdapters.TodoListAdapter;
import ua.a5.newnotes.dto.notesDTO.TodoDTO;
import ua.a5.newnotes.fragments.AbstractTabFragment;


/**
 * Created by A5 Android Intern 2 on 28.04.2017.
 */

public class TodoFragment extends AbstractTabFragment implements TodoListAdapter.TodoClickListener {
    public static final String KEY_TODO_DTO = "key todo dto";

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
        TodoListAdapter adapter = new TodoListAdapter(context, createMockTodoListData(), this);
        recyclerView.setAdapter(adapter);

        /*
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_notes);
        fab.attachToRecyclerView(recyclerView);
        */


        FloatingActionButton actionTodo = (FloatingActionButton) getActivity().findViewById(R.id.action_todo);
        actionTodo.setTitle("new TODO Note");
        actionTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "search", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), CreateNoteActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton actionIdea = (FloatingActionButton) getActivity().findViewById(R.id.action_ideas);
        actionIdea.setTitle("new IDEA Note");
        actionIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "search", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), CreateNoteActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton actionBirthday = (FloatingActionButton) getActivity().findViewById(R.id.action_birthdays);
        actionBirthday.setTitle("new BIRTHDAY Note");
        actionBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "search", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), CreateNoteActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton actionDifferent = (FloatingActionButton) getActivity().findViewById(R.id.action_different);
        actionDifferent.setTitle("new DIFFERENT Note");
        actionDifferent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "search", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), CreateNoteActivity.class);
                startActivity(intent);
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


    @Override
    public void onClick(TodoDTO todoDTO) {
        Intent intent = new Intent(getContext(), TodoActivity.class);
        intent.putExtra(KEY_TODO_DTO, todoDTO);
        startActivity(intent);
        Toast.makeText(getContext(), todoDTO.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
