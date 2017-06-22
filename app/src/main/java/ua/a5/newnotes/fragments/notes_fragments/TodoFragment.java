package ua.a5.newnotes.fragments.notes_fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import ua.a5.newnotes.DAO.DBHelper;
import ua.a5.newnotes.R;
import ua.a5.newnotes.activities.CreateNoteBirthdaysActivity;
import ua.a5.newnotes.activities.CreateNoteDifferentActivity;
import ua.a5.newnotes.activities.CreateNoteIdeasActivity;
import ua.a5.newnotes.activities.CreateNoteTODOActivity;
import ua.a5.newnotes.activities.notes_activities.TodoActivity;
import ua.a5.newnotes.adapter.notesListAdapters.TodoListAdapter;
import ua.a5.newnotes.dto.notesDTO.TodoDTO;
import ua.a5.newnotes.fragments.AbstractTabFragment;

import static ua.a5.newnotes.utils.Constants.KEY_TODO_DTO;
import static ua.a5.newnotes.utils.Constants.LOG_TAG;


/**
 * Created by A5 Android Intern 2 on 28.04.2017.
 */

public class TodoFragment extends AbstractTabFragment implements TodoListAdapter.TodoClickListener {

    FloatingActionsMenu menuMultipleActions;

    private static final int LAYOUT = R.layout.fragment_todo;

    //для работы с БД.
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    String orderBy;
    String strConsoleOutput = "";

    RecyclerView recyclerView;
    TodoListAdapter adapter;

    public static TodoFragment getInstance(Context context) {
        Bundle args = new Bundle();
        TodoFragment fragment = new TodoFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_todo));
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new TodoListAdapter(context, createTodoNotesList(), this);
        recyclerView.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TodoListAdapter(context, createTodoNotesList(), this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton actionTodo = (FloatingActionButton) getActivity().findViewById(R.id.action_todo);
        actionTodo.setTitle("new TODO Note");
        actionTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateNoteTODOActivity.class);
                startActivity(intent);
            }
        });


        FloatingActionButton actionIdea = (FloatingActionButton) getActivity().findViewById(R.id.action_ideas);
        actionIdea.setTitle("new IDEA Note");
        actionIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateNoteIdeasActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton actionBirthday = (FloatingActionButton) getActivity().findViewById(R.id.action_birthdays);
        actionBirthday.setTitle("new BIRTHDAY Note");
        actionBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateNoteBirthdaysActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton actionDifferent = (FloatingActionButton) getActivity().findViewById(R.id.action_different);
        actionDifferent.setTitle("new DIFFERENT Note");
        actionDifferent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateNoteDifferentActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton actionMainmenu = (FloatingActionButton) getActivity().findViewById(R.id.action_notes_mainmenu);
        actionMainmenu.setTitle("Main Menu");
        actionMainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });



        final FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.frame_layout_notes);
        frameLayout.getBackground().setAlpha(0);
        menuMultipleActions = (FloatingActionsMenu) getActivity().findViewById(R.id.multiple_actions_notes);

        menuMultipleActions.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                frameLayout.getBackground().setAlpha(240);
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        menuMultipleActions.collapse();
                        return true;
                    }
                });
            }
            @Override
            public void onMenuCollapsed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }
        });
        return view;
    }


    private List<TodoDTO> createTodoNotesList() {
        List<TodoDTO> todoNotes = new ArrayList<>();

//////////////////---------------------->

        //для работы с БД.
        dbHelper = new DBHelper(getActivity());

        //класс SQLiteDatabase предназначен для управления БД SQLite.
        //если БД не существует, dbHelper вызовет метод onCreate(),
        //если версия БД изменилась, dbHelper вызовет метод onUpgrade().

        //в любом случае вернётся существующая, толькочто созданная или обновлённая БД.
        sqLiteDatabase = dbHelper.getWritableDatabase();

        //метод rawQuery() возвращает объект типа Cursor,
        //его можно рассматривать как набор строк с данными.

        cursor = sqLiteDatabase.query(dbHelper.TABLE_NOTES_TODO_NAME,null,null,null,null,null,null);
        //метод cursor.moveToFirst() делает 1-ю запись в cursor активной
        //и проверяет, есть ли в cursor что-то.
        if (cursor.moveToFirst()) {

            //получаем порядковые номера столбцов по их именам.
            int idIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_TODO_KEY_ID);
            int titleIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_TODO_KEY_TITLE);
            int todoIsDoneIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_TODO_KEY_ISDONE);
            int dayIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_TODO_KEY_DAY);
            int monthIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_TODO_KEY_MONTH);
            int yearIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_TODO_KEY_YEAR);
            int descriptionIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_TODO_KEY_DESCRIPTION);


            //с помощью метода .moveToNext() перебираем все строки в cursor-е.
            do {
                todoNotes.add(new TodoDTO(
                        cursor.getString(titleIndex),
                        cursor.getInt(todoIsDoneIndex),
                        cursor.getInt(dayIndex),
                        cursor.getInt(monthIndex),
                        cursor.getInt(yearIndex),
                        cursor.getString(descriptionIndex)));
            } while (cursor.moveToNext());

        } else {
            Log.d(LOG_TAG, "0 rows");
        }

        //в конце закрываем cursor. Освобождаем ресурс.
        cursor.close();

        //закрываем соединение с БД.
        dbHelper.close();

//////////////////---------------------->

        return todoNotes;
    }


    @Override
    public void onClick(TodoDTO todoDTO) {
        Intent intent = new Intent(getContext(), TodoActivity.class);
        intent.putExtra(KEY_TODO_DTO, todoDTO);
        startActivity(intent);
    }


    @Override
    public void onStop() {
        super.onStop();
        menuMultipleActions.collapse();
    }
}
