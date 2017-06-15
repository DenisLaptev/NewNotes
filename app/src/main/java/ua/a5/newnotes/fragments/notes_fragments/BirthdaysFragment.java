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
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import ua.a5.newnotes.DAO.DBHelper;
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
    public static final String LOG_TAG = "log";

    FloatingActionsMenu menuMultipleActions;

    private static final int LAYOUT = R.layout.fragment_birthdays;

    //для работы с БД.
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    String orderBy;
    String strConsoleOutput = "";

    RecyclerView recyclerView;
    BirthdaysListAdapter adapter;


    public static BirthdaysFragment getInstance(Context context) {
        Bundle args = new Bundle();
        BirthdaysFragment fragment = new BirthdaysFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_birthdays));
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new BirthdaysListAdapter(context, createBirthdaysNotesList(), this);
        recyclerView.setAdapter(adapter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_birthdays);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new BirthdaysListAdapter(context, createBirthdaysNotesList(), this);
        recyclerView.setAdapter(adapter);

        menuMultipleActions = (FloatingActionsMenu) getActivity().findViewById(R.id.multiple_actions_notes);

        return view;
    }

    private List<BirthdayDTO> createBirthdaysNotesList() {
        List<BirthdayDTO> birthdaysNotes = new ArrayList<>();

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

        cursor = sqLiteDatabase.query(dbHelper.TABLE_NOTES_BIRTHDAYS_NAME, null, null, null, null, null, null);
        //метод cursor.moveToFirst() делает 1-ю запись в cursor активной
        //и проверяет, есть ли в cursor что-то.
        if (cursor.moveToFirst()) {

            //получаем порядковые номера столбцов по их именам.
            int idIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_BIRTHDAYS_KEY_ID);
            int nameIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_BIRTHDAYS_KEY_NAME);
            int dayIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_BIRTHDAYS_KEY_DAY);
            int monthIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_BIRTHDAYS_KEY_MONTH);
            int yearIndex = cursor.getColumnIndex(dbHelper.TABLE_NOTES_BIRTHDAYS_KEY_YEAR);


            //с помощью метода .moveToNext() перебираем все строки в cursor-е.
            do {
                birthdaysNotes.add(new BirthdayDTO(
                        cursor.getString(nameIndex),
                        cursor.getInt(dayIndex),
                        cursor.getInt(monthIndex),
                        cursor.getInt(yearIndex)

                ));
            } while (cursor.moveToNext());

        } else {
            Log.d(LOG_TAG, "0 rows");
        }

        //в конце закрываем cursor. Освобождаем ресурс.
        cursor.close();

        //закрываем соединение с БД.
        dbHelper.close();

//////////////////---------------------->


        return birthdaysNotes;
    }

    @Override
    public void onClick(BirthdayDTO birthdayDTO) {
        Intent intent = new Intent(getContext(), BirthdayActivity.class);
        intent.putExtra(KEY_BIRTHDAY_DTO, birthdayDTO);
        startActivity(intent);
        Toast.makeText(getContext(), birthdayDTO.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        menuMultipleActions.collapse();
    }
}
