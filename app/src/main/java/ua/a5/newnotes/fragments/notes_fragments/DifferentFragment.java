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
import ua.a5.newnotes.activities.notes_activities.DifferentActivity;
import ua.a5.newnotes.adapter.notesListAdapters.DifferentListAdapter;
import ua.a5.newnotes.dto.notesDTO.DifferentDTO;
import ua.a5.newnotes.fragments.AbstractTabFragment;

import static ua.a5.newnotes.utils.Constants.KEY_DIFFERENT_DTO;
import static ua.a5.newnotes.utils.Constants.LOG_TAG;

/**
 * Created by A5 Android Intern 2 on 28.04.2017.
 */

public class DifferentFragment extends AbstractTabFragment implements DifferentListAdapter.DifferentClickListener {

    private static final int LAYOUT = R.layout.fragment_different;

    RecyclerView recyclerView;
    DifferentListAdapter adapter;
    FloatingActionsMenu menuMultipleActions;

    //для работы с БД.
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    String orderBy;
    String strConsoleOutput = "";


    public static DifferentFragment getInstance(Context context) {
        Bundle args = new Bundle();
        DifferentFragment fragment = new DifferentFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_different));
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter = new DifferentListAdapter(context, createDifferentNotesList(), this);
        recyclerView.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_different);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new DifferentListAdapter(context, createDifferentNotesList(), this);
        recyclerView.setAdapter(adapter);
        menuMultipleActions = (FloatingActionsMenu) getActivity().findViewById(R.id.multiple_actions_notes);
        return view;
    }

    private List<DifferentDTO> createDifferentNotesList() {
        List<DifferentDTO> differentNotes = new ArrayList<>();

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

        cursor = sqLiteDatabase.query(dbHelper.TABLE_NOTES_DIFFERENT_NAME, null, null, null, null, null, null);
        //метод cursor.moveToFirst() делает 1-ю запись в cursor активной
        //и проверяет, есть ли в cursor что-то.
        if (cursor.moveToFirst()) {

            //получаем порядковые номера столбцов по их именам.
            int idIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_DIFFERENT_KEY_ID);
            int titleIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_DIFFERENT_KEY_TITLE);
            int dateIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_DIFFERENT_KEY_DATE);
            int descriptionIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_DIFFERENT_KEY_DESCRIPTION);


            //с помощью метода .moveToNext() перебираем все строки в cursor-е.
            do {
                differentNotes.add(new DifferentDTO(
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

        return differentNotes;
    }


    @Override
    public void onClick(DifferentDTO differentDTO) {
        Intent intent = new Intent(getContext(), DifferentActivity.class);
        intent.putExtra(KEY_DIFFERENT_DTO, differentDTO);
        startActivity(intent);
        }

    @Override
    public void onStop() {
        super.onStop();
        menuMultipleActions.collapse();
    }
}
