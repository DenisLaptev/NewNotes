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
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import ua.a5.newnotes.DAO.DBHelper;
import ua.a5.newnotes.R;
import ua.a5.newnotes.activities.notes_activities.IdeaActivity;
import ua.a5.newnotes.adapter.notesListAdapters.IdeasListAdapter;
import ua.a5.newnotes.dto.notesDTO.IdeaDTO;
import ua.a5.newnotes.fragments.AbstractTabFragment;

import static ua.a5.newnotes.utils.Constants.KEY_IDEA_DTO;
import static ua.a5.newnotes.utils.Constants.LOG_TAG;


/**
 * Created by A5 Android Intern 2 on 28.04.2017.
 */

public class IdeasFragment extends AbstractTabFragment implements IdeasListAdapter.IdeaClickListener {

    private static final int LAYOUT = R.layout.fragment_ideas;

    RecyclerView recyclerView;
    IdeasListAdapter adapter;
    FloatingActionsMenu menuMultipleActions;

    //для работы с БД.
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    String orderBy;
    String strConsoleOutput = "";


    public static IdeasFragment getInstance(Context context) {
        Bundle args = new Bundle();
        IdeasFragment fragment = new IdeasFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_ideas));
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new IdeasListAdapter(context, createIdeasNotesList(), this);
        recyclerView.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_ideas);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new IdeasListAdapter(context, createIdeasNotesList(), this);
        recyclerView.setAdapter(adapter);
        menuMultipleActions = (FloatingActionsMenu) getActivity().findViewById(R.id.multiple_actions_notes);
        return view;
    }

    private List<IdeaDTO> createIdeasNotesList() {
        List<IdeaDTO> ideasNotes = new ArrayList<>();

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

        cursor = sqLiteDatabase.query(dbHelper.TABLE_NOTES_IDEAS_NAME, null, null, null, null, null, null);
        //метод cursor.moveToFirst() делает 1-ю запись в cursor активной
        //и проверяет, есть ли в cursor что-то.
        if (cursor.moveToFirst()) {


            //получаем порядковые номера столбцов по их именам.
            int idIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_IDEAS_KEY_ID);
            int titleIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_IDEAS_KEY_TITLE);
            int dateIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_IDEAS_KEY_DATE);
            int descriptionIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_IDEAS_KEY_DESCRIPTION);


            //с помощью метода .moveToNext() перебираем все строки в cursor-е.
            do {
                ideasNotes.add(new IdeaDTO(
                        cursor.getString(titleIndex),
                        cursor.getString(descriptionIndex),
                        cursor.getString(dateIndex)));
            } while (cursor.moveToNext());

        } else {
            Log.d(LOG_TAG, "0 rows");
        }

        //в конце закрываем cursor. Освобождаем ресурс.
        cursor.close();

        //закрываем соединение с БД.
        dbHelper.close();

//////////////////---------------------->

        return ideasNotes;
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
    }

    @Override
    public void onStop() {
        super.onStop();
        menuMultipleActions.collapse();
    }
}
