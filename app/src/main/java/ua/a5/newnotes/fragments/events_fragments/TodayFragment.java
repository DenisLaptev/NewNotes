package ua.a5.newnotes.fragments.events_fragments;

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
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import ua.a5.newnotes.DAO.DBHelper;
import ua.a5.newnotes.R;
import ua.a5.newnotes.activities.CreateEventActivity;
import ua.a5.newnotes.activities.events_activities.EventActivity;
import ua.a5.newnotes.adapter.eventsListAdapters.EventsListAdapter;
import ua.a5.newnotes.dto.eventsDTO.EventCRUD;
import ua.a5.newnotes.dto.eventsDTO.EventDTO;
import ua.a5.newnotes.dto.eventsDTO.EventDTOCollection;
import ua.a5.newnotes.fragments.AbstractTabFragment;

import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_BEGIN_DAY;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_BEGIN_HOUR;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_BEGIN_MINUTE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_BEGIN_MONTH;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_BEGIN_YEAR;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_DESCRIPTION;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_END_DAY;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_END_HOUR;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_END_MINUTE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_END_MONTH;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_ID;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_LOCATION;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_STRING_BEGIN_MONTH;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_TITLE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_NAME;
import static ua.a5.newnotes.utils.Constants.KEY_EVENT_DTO;
import static ua.a5.newnotes.utils.Constants.LOG_TAG;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentDay;


/**
 * Created by A5 Android Intern 2 on 28.04.2017.
 */

public class TodayFragment extends AbstractTabFragment implements EventsListAdapter.EventClickListener {



    FloatingActionsMenu menuMultipleActions;

    private static final int LAYOUT = R.layout.fragment_events;

    //для работы с БД.
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    String orderBy;
    String strConsoleOutput = "";


    EventCRUD eventCRUD;
    RecyclerView recyclerView;
    EventsListAdapter adapter;




    public static TodayFragment getInstance(Context context) {
    //public TodayFragment getInstance(Context context) {
        Bundle args = new Bundle();
        TodayFragment fragment = new TodayFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.menu_events_item_today));

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new EventsListAdapter(context, getTodayEventsList(), this);
        recyclerView.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        eventCRUD = new EventCRUD(EventDTOCollection.getEventDTOs());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new EventsListAdapter(context, getTodayEventsList(), this);
        recyclerView.setAdapter(adapter);


        FloatingActionButton actionEvent = (FloatingActionButton) getActivity().findViewById(R.id.action_event);
        actionEvent.setTitle("new EVENT");
        actionEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateEventActivity.class);
                startActivity(intent);
            }
        });


        FloatingActionButton actionMainmenu = (FloatingActionButton) getActivity().findViewById(R.id.action_events_mainmenu);
        actionMainmenu.setTitle("Main Menu");
        actionMainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        final FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.frame_layout_events);
        frameLayout.getBackground().setAlpha(0);
        menuMultipleActions = (FloatingActionsMenu) getActivity().findViewById(R.id.multiple_actions_events);

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


    private List<EventDTO> getTodayEventsList() {
        List<EventDTO> eventsData = new ArrayList<>();


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

        cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM "
                        + TABLE_EVENTS_NAME
                        + " WHERE " + TABLE_EVENTS_KEY_BEGIN_DAY +" = ? ", new String[]{String.valueOf(getCurrentDay())});
        //метод cursor.moveToFirst() делает 1-ю запись в cursor активной
        //и проверяет, есть ли в cursor что-то.
        if (cursor.moveToFirst()) {
            //получаем порядковые номера столбцов по их именам.
            int idIndex = cursor.getColumnIndex(TABLE_EVENTS_KEY_ID);
            int titleIndex = cursor.getColumnIndex(TABLE_EVENTS_KEY_TITLE);
            int locationIndex = cursor.getColumnIndex(TABLE_EVENTS_KEY_LOCATION);
            int begindayIndex = cursor.getColumnIndex(TABLE_EVENTS_KEY_BEGIN_DAY);
            int beginmonthIndex = cursor.getColumnIndex(TABLE_EVENTS_KEY_BEGIN_MONTH);
            int stringBeginmonthIndex = cursor.getColumnIndex(TABLE_EVENTS_KEY_STRING_BEGIN_MONTH);
            int beginyearIndex = cursor.getColumnIndex(TABLE_EVENTS_KEY_BEGIN_YEAR);

            int beginhourIndex = cursor.getColumnIndex(TABLE_EVENTS_KEY_BEGIN_HOUR);
            int beginminuteIndex = cursor.getColumnIndex(TABLE_EVENTS_KEY_BEGIN_MINUTE);
            int enddayIndex = cursor.getColumnIndex(TABLE_EVENTS_KEY_END_DAY);
            int endmonthIndex = cursor.getColumnIndex(TABLE_EVENTS_KEY_END_MONTH);
            int endhourIndex = cursor.getColumnIndex(TABLE_EVENTS_KEY_END_HOUR);
            int endminuteIndex = cursor.getColumnIndex(TABLE_EVENTS_KEY_END_MINUTE);
            int descriptionIndex = cursor.getColumnIndex(TABLE_EVENTS_KEY_DESCRIPTION);


            //с помощью метода .moveToNext() перебираем все строки в cursor-е.
            do {
                eventsData.add(new EventDTO(
                        cursor.getString(titleIndex),
                        cursor.getString(descriptionIndex),
                        cursor.getInt(begindayIndex),
                        cursor.getInt(beginmonthIndex),
                        cursor.getInt(beginyearIndex)
                ));
            }
            while (cursor.moveToNext());

        } else {
            Log.d(LOG_TAG, "0 rows");
        }
        System.out.println(strConsoleOutput);

        //в конце закрываем cursor. Освобождаем ресурс.
        cursor.close();

        //закрываем соединение с БД.
        dbHelper.close();

//////////////////---------------------->


        return eventsData;
    }

    @Override
    public void onClick(EventDTO eventDTO) {
        Intent intent = new Intent(getContext(), EventActivity.class);
        intent.putExtra(KEY_EVENT_DTO, eventDTO);
        startActivity(intent);
        Toast.makeText(getContext(), eventDTO.getTitle(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStop() {
        super.onStop();
        menuMultipleActions.collapse();
    }
}
