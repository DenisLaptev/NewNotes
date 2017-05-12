package ua.a5.newnotes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ua.a5.newnotes.DAO.DBHelper;
import ua.a5.newnotes.model.Event;

import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_BEGIN_DAY;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_BEGIN_HOUR;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_BEGIN_MINUTE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_BEGIN_MONTH;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_DESCRIPTION;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_END_DAY;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_END_HOUR;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_END_MINUTE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_END_MONTH;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_IMPORTANCE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_LOCATION;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_TITLE;

public class CreateEventActivity extends AppCompatActivity implements TextWatcher {
    //implements TextWatcher
    //Интерфейс TextWatcher нужен для работы с Т9 (AutoCompleteTextView actvNote)

    public static final String LOG_TAG = "log";

    public static final String[] WORDS = {"today", "tomorrow", "day before"};


    public static String DEFAULT_DAY = String.valueOf(Calendar.getInstance().get(Calendar.DATE));
    public static String DEFAULT_MONTH = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
    public static String DEFAULT_YEAR = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    public static String DEFAULT_HOUR = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
    public static String DEFAULT_MINUTE = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));


    EditText etCreateEventTitle;
    EditText etCreateEventLocation;

    //CheckBox chbCreateEventImportant;

    EditText etCreateEventBeginDay;
    EditText etCreateEventBeginMonth;
    EditText etCreateEventBeginHour;
    EditText etCreateEventBeginMinute;

    EditText etCreateEventEndDay;
    EditText etCreateEventEndMonth;
    EditText etCreateEventEndHour;
    EditText etCreateEventEndMinute;


    AutoCompleteTextView actvCreateEventEvent;
    TextView tvCreateEventEvent;

    Button btnCreateEventSave;
    Button btnCreateEventEventsMenu;
    Button btnCreateEventCalendar;

    //Button btnCreateEventLaunchCalendar;
    //Button btnCreateEventLaunchEvent;

    //Button btnCreateEventTest;


    //для работы с БД.
    DBHelper dbHelper;

    Event event;
    String title;
    String location;
    int isImportant;

    String beginDay = DEFAULT_DAY;
    String beginMonth = DEFAULT_MONTH;

    String beginHour = DEFAULT_HOUR;
    String beginMinute = DEFAULT_MINUTE;

    String endDay = DEFAULT_DAY;
    String endMonth = DEFAULT_MONTH;

    String endHour = DEFAULT_HOUR;
    String endMinute = DEFAULT_MINUTE;


    String description;

    @Override
    protected void onResume() {
        super.onResume();
        tvCreateEventEvent.setText(description);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        description = null;
        setDefaulDateAndTime();

        //для работы с БД.
        dbHelper = new DBHelper(this);

        etCreateEventTitle = (EditText) findViewById(R.id.event_etCreateEventTitle);
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        etCreateEventTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        etCreateEventLocation = (EditText) findViewById(R.id.event_etCreateEventLocation);
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        etCreateEventLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        //chbCreateEventImportant = (CheckBox) findViewById(R.id.event_chbCreateEventImportant);


        etCreateEventBeginDay = (EditText) findViewById(R.id.event_etCreateEventBeginDay);
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        etCreateEventBeginDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etCreateEventBeginMonth = (EditText) findViewById(R.id.event_etCreateEventBeginMonth);
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        etCreateEventBeginMonth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etCreateEventBeginHour = (EditText) findViewById(R.id.event_etCreateEventBeginHour);
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        etCreateEventBeginHour.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etCreateEventBeginMinute = (EditText) findViewById(R.id.event_etCreateEventBeginMinute);
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        etCreateEventBeginMinute.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etCreateEventEndDay = (EditText) findViewById(R.id.event_etCreateEventEndDay);
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        etCreateEventEndDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etCreateEventEndMonth = (EditText) findViewById(R.id.event_etCreateEventEndMonth);
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        etCreateEventEndMonth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etCreateEventEndHour = (EditText) findViewById(R.id.event_etCreateEventEndHour);
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        etCreateEventEndHour.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etCreateEventEndMinute = (EditText) findViewById(R.id.event_etCreateEventEndMinute);
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        etCreateEventEndMinute.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        actvCreateEventEvent = (AutoCompleteTextView) findViewById(R.id.event_actvCreateEventEvent);
        actvCreateEventEvent.addTextChangedListener(this);
        actvCreateEventEvent.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, WORDS));
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        actvCreateEventEvent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        tvCreateEventEvent = (TextView) findViewById(R.id.note_tvCreateNoteNote);
        tvCreateEventEvent.setMovementMethod(LinkMovementMethod.getInstance());


        btnCreateEventSave = (Button) findViewById(R.id.event_btnCreateEventSave);
        btnCreateEventSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////////
                //заполняем БД данными.

                //класс SQLiteDatabase предназначен для управления БД SQLite.
                //если БД не существует, dbHelper вызовет метод onCreate(),
                //если версия БД изменилась, dbHelper вызовет метод onUpgrade().

                //в любом случае вернётся существующая, толькочто созданная или обновлённая БД.
                SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

                //класс ContentValues используется для добавления новых строк в таблицу.
                //каждый объект этого класса представляет собой одну строку таблицы и
                //выглядит, как массив с именами столбцов и значениями, которые им соответствуют.
                ContentValues contentValues = new ContentValues();

                //добавляем пары ключ-значение.


                title = etCreateEventTitle.getText().toString();
                location = etCreateEventLocation.getText().toString();


                beginDay = etCreateEventBeginDay.getText().toString();
                beginMonth = etCreateEventBeginMonth.getText().toString();
                /*
                System.out.println("beginDay=" + beginDay);
                System.out.println("beginMonth=" + beginMonth);
                */


                beginHour = etCreateEventBeginHour.getText().toString();
                beginMinute = etCreateEventBeginMinute.getText().toString();
                /*
                System.out.println("beginHour=" + beginHour);
                System.out.println("beginMinute=" + beginMinute);
                */

                endDay = etCreateEventEndDay.getText().toString();
                endMonth = etCreateEventEndMonth.getText().toString();
                /*
                System.out.println("endDay=" + endDay);
                System.out.println("endMonth=" + endMonth);
                */

                endHour = etCreateEventEndHour.getText().toString();
                endMinute = etCreateEventEndMinute.getText().toString();
                /*
                System.out.println("endHour=" + endHour);
                System.out.println("endMinute=" + endMinute);
                */

                description = tvCreateEventEvent.getText().toString();


                event = new Event(title, location, isImportant,
                        beginDay, beginMonth, beginHour, beginMinute,
                        endDay, endMonth, endHour, endMinute, description);
                System.out.println(event);


                contentValues.put(TABLE_EVENTS_KEY_TITLE, title);
                contentValues.put(TABLE_EVENTS_KEY_LOCATION, location);
                contentValues.put(TABLE_EVENTS_KEY_IMPORTANCE, isImportant);
                contentValues.put(TABLE_EVENTS_KEY_BEGIN_DAY, beginDay);
                contentValues.put(TABLE_EVENTS_KEY_BEGIN_MONTH, beginMonth);
                contentValues.put(TABLE_EVENTS_KEY_BEGIN_HOUR, beginHour);
                contentValues.put(TABLE_EVENTS_KEY_BEGIN_MINUTE, beginMinute);
                contentValues.put(TABLE_EVENTS_KEY_END_DAY, endDay);
                contentValues.put(TABLE_EVENTS_KEY_END_MONTH, endMonth);
                contentValues.put(TABLE_EVENTS_KEY_END_HOUR, endHour);
                contentValues.put(TABLE_EVENTS_KEY_END_MINUTE, endMinute);
                contentValues.put(TABLE_EVENTS_KEY_DESCRIPTION, description);
                //id заполнится автоматически.

                //вставляем подготовленные строки в таблицу.
                //второй аргумент используется для вставки пустой строки,
                //сейчас он нам не нужен, поэтому он = null.
                sqLiteDatabase.insert(DBHelper.TABLE_EVENTS_NAME, null, contentValues);
                Log.d(LOG_TAG, "Date inserted");

                System.out.println(contentValues);
                //закрываем соединение с БД.
                dbHelper.close();
////////////////////////
            }
        });

        btnCreateEventEventsMenu = (Button) findViewById(R.id.event_btnCreateEventEventsMenu);
        btnCreateEventEventsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateEventActivity.this, EventsActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnCreateEventCalendar = (Button) findViewById(R.id.event_btnCreateEventCalendar);
        btnCreateEventCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = etCreateEventTitle.getText().toString();
                location = etCreateEventLocation.getText().toString();
                description = tvCreateEventEvent.getText().toString();


                beginDay = etCreateEventBeginDay.getText().toString();
                beginMonth = etCreateEventBeginMonth.getText().toString();

                beginHour = etCreateEventBeginHour.getText().toString();
                beginMinute = etCreateEventBeginMinute.getText().toString();

                endDay = etCreateEventEndDay.getText().toString();
                endMonth = etCreateEventEndMonth.getText().toString();

                endHour = etCreateEventEndHour.getText().toString();
                endMinute = etCreateEventEndMinute.getText().toString();


                //The intent to create a calendar event.
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE, title);
                calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);
                calIntent.putExtra(CalendarContract.Events.DESCRIPTION, description);


                try {
                    GregorianCalendar calDateBegin = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Integer.parseInt(beginMonth) - 1, Integer.parseInt(beginDay));
                    GregorianCalendar calDateEnd = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Integer.parseInt(endMonth) - 1, Integer.parseInt(endDay));

                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                            calDateBegin.getTimeInMillis() + Integer.parseInt(beginHour) * 60 * 60 * 1000 + Integer.parseInt(beginMinute) * 60 * 1000);
                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                            calDateEnd.getTimeInMillis() + Integer.parseInt(endHour) * 60 * 60 * 1000 + Integer.parseInt(endMinute) * 60 * 1000);

                    startActivity(calIntent);
                } catch (Exception e) {
                    e.printStackTrace();

                    setDefaulDateAndTime();

                    GregorianCalendar calDateBegin = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Integer.parseInt(beginMonth) - 1, Integer.parseInt(beginDay));
                    GregorianCalendar calDateEnd = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Integer.parseInt(endMonth) - 1, Integer.parseInt(endDay));

                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                            calDateBegin.getTimeInMillis() + Integer.parseInt(beginHour) * 60 * 60 * 1000 + Integer.parseInt(beginMinute) * 60 * 1000);
                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                            calDateEnd.getTimeInMillis() + Integer.parseInt(endHour) * 60 * 60 * 1000 + Integer.parseInt(endMinute) * 60 * 1000);

                    startActivity(calIntent);
                }

                /*
                setDefaulDateAndTime();

                GregorianCalendar calDateBegin = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Integer.parseInt(beginMonth) - 1, Integer.parseInt(beginDay));
                GregorianCalendar calDateEnd = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Integer.parseInt(endMonth) - 1, Integer.parseInt(endDay));

                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        calDateBegin.getTimeInMillis() + Integer.parseInt(beginHour) * 60 * 60 * 1000 + Integer.parseInt(beginMinute) * 60 * 1000);
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        calDateEnd.getTimeInMillis() + Integer.parseInt(endHour) * 60 * 60 * 1000 + Integer.parseInt(endMinute) * 60 * 1000);

                startActivity(calIntent);
                */


            }
        });


    }

    private void setDefaulDateAndTime() {
        //Устанавливаем дефолтные константы даты и времени.
        //Если не выбрали дату и время события, то ставим текущую дату и время.
        DEFAULT_DAY = String.valueOf(Calendar.getInstance().get(Calendar.DATE));
        DEFAULT_MONTH = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
        DEFAULT_YEAR = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        DEFAULT_HOUR = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        DEFAULT_MINUTE = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));


        beginDay = DEFAULT_DAY;
        beginMonth = DEFAULT_MONTH;
        beginHour = DEFAULT_HOUR;
        beginMinute = DEFAULT_MINUTE;
        endDay = DEFAULT_DAY;
        endMonth = DEFAULT_MONTH;
        endHour = DEFAULT_HOUR;
        endMinute = DEFAULT_MINUTE;
    }

    //метод, для убирания клавиатуры EditText при нажатии на пустое пространство.
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tvCreateEventEvent.setText(actvCreateEventEvent.getText());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
