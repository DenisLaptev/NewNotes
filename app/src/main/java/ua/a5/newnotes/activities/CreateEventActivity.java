package ua.a5.newnotes.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.a5.newnotes.DAO.DBHelper;
import ua.a5.newnotes.R;
import ua.a5.newnotes.activities.events_activities.EventActivity;
import ua.a5.newnotes.dto.eventsDTO.EventDTO;

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
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_LOCATION;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_KEY_TITLE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_EVENTS_NAME;
import static ua.a5.newnotes.utils.Constants.KEY_EVENT_DTO;
import static ua.a5.newnotes.utils.Constants.KEY_UPDATE_EVENTS;
import static ua.a5.newnotes.utils.Constants.LOG_TAG;
import static ua.a5.newnotes.utils.Constants.isCardForUpdate;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.DATE_REGEXPS;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.DAYS_OF_THE_WEEK;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.TIME_WORDS;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentDay;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentHour;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentMinute;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentMonth;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentYear;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getDifferenceBetweenPlannedDayAndToday;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsWords.getIntMonthFromString;

public class CreateEventActivity extends AppCompatActivity {

    boolean isSavedFlagEvent;

    private int mYear;
    private int mMonth;
    private int mDay;
    private TextView tvEventsDeadline;
    private Button btnEventsDeadline;
    static final int DATE_DIALOG_ID = 2;

    public static SpannableString bufferSpannableString = null;

    String initialWord;
    String strRegExp;

    EditText etCreateEventTitle;
    EditText etEventDescription;


    Button btnCreateEventSave;
    Button btnCreateEventEventsMenu;
    Button btnCreateEventCalendar;


    //для работы с БД.
    DBHelper dbHelper;

    EventDTO event;
    String title;
    String location;

    int beginDay = getCurrentDay();
    int beginMonth = getCurrentMonth();
    String stringBeginMonth = null;
    int beginYear = getCurrentYear();

    String beginHour = String.valueOf(getCurrentHour());
    String beginMinute = String.valueOf(getCurrentMinute());

    int endDay = getCurrentDay();
    int endMonth = getCurrentMonth();

    String endHour = String.valueOf(getCurrentHour());
    String endMinute = String.valueOf(getCurrentMinute());

    String description;


    EventDTO eventDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        isSavedFlagEvent = false;


        description = null;

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


        btnEventsDeadline = (Button) findViewById(R.id.btn_events_date_picker);
        btnEventsDeadline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        tvEventsDeadline = (TextView) findViewById(R.id.tv_events_deadline_date);
        // display the current date
        updateDisplay();

        etEventDescription = (EditText) findViewById(R.id.et_event_description);
        etEventDescription.addTextChangedListener(onTextChangedListener());
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        etEventDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        if (isCardForUpdate == true && getIntent() != null) {
            eventDTO = (EventDTO) getIntent().getSerializableExtra(KEY_UPDATE_EVENTS);
            etCreateEventTitle.setText(eventDTO.getTitle());
            etEventDescription.setText(eventDTO.getDescription());
            tvEventsDeadline.setText(
                    new StringBuilder()
                            // Month is 0 based so add 1
                            .append(eventDTO.getDay()).append("-")
                            .append(eventDTO.getMonth() + 1).append("-")
                            .append(eventDTO.getYear()).append(" "));

        } else {
            eventDTO = new EventDTO(
                    new String(""),
                    new String(""),
                    getCurrentDay(),
                    getCurrentMonth(),
                    getCurrentYear()
            );
        }
        btnCreateEventSave = (Button) findViewById(R.id.event_btnCreateEventSave);
        btnCreateEventSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isCardForUpdate == true) {
                    deleteItemFromTable(eventDTO);
                }
                isCardForUpdate = false;
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

                beginDay = mDay;
                beginMonth = mMonth;
                beginYear = mYear;
                beginHour = String.valueOf(getCurrentHour());
                beginMinute = String.valueOf(getCurrentMinute());


                endDay = mDay;
                endMonth = mMonth;
                endHour = String.valueOf(getCurrentHour());
                endMinute = String.valueOf(getCurrentMinute());

                description = etEventDescription.getText().toString();

                event = new EventDTO(title, description, beginDay, beginMonth, beginYear);


                contentValues.put(TABLE_EVENTS_KEY_TITLE, title);
                contentValues.put(TABLE_EVENTS_KEY_LOCATION, location);
                contentValues.put(TABLE_EVENTS_KEY_BEGIN_DAY, beginDay);
                contentValues.put(TABLE_EVENTS_KEY_BEGIN_MONTH, beginMonth);
                contentValues.put(TABLE_EVENTS_KEY_BEGIN_YEAR, beginYear);
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

                isSavedFlagEvent = true;
            }
        });

        btnCreateEventEventsMenu = (Button) findViewById(R.id.event_btnCreateEventEventsMenu);
        btnCreateEventEventsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCardForUpdate = false;
                //Intent intent = new Intent(CreateEventActivity.this, EventsActivity.class);
                //startActivity(intent);
                onBackPressed();
                finish();
            }
        });


        btnCreateEventCalendar = (Button) findViewById(R.id.event_btnCreateEventCalendar);
        btnCreateEventCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = etCreateEventTitle.getText().toString();

                beginDay = mDay;
                beginMonth = mMonth + 1;
                beginYear = mYear;
                beginHour = String.valueOf(getCurrentHour());
                beginMinute = String.valueOf(getCurrentMinute());


                endDay = mDay;
                endMonth = mMonth + 1;
                endHour = String.valueOf(getCurrentHour());
                endMinute = String.valueOf(getCurrentMinute());


                description = etEventDescription.getText().toString();


                //The intent to create a calendar event.
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE, title);
                calIntent.putExtra(CalendarContract.Events.DESCRIPTION, description);


                try {
                    GregorianCalendar calDateBegin = new GregorianCalendar(mYear, beginMonth, beginDay);
                    GregorianCalendar calDateEnd = new GregorianCalendar(mYear, endMonth, endDay);

                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                            calDateBegin.getTimeInMillis() + Integer.parseInt(beginHour) * 60 * 60 * 1000 + Integer.parseInt(beginMinute) * 60 * 1000);
                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                            calDateEnd.getTimeInMillis() + Integer.parseInt(endHour) * 60 * 60 * 1000 + Integer.parseInt(endMinute) * 60 * 1000);

                    startActivity(calIntent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();


                    GregorianCalendar calDateBegin = new GregorianCalendar(mYear, beginMonth, beginDay);
                    GregorianCalendar calDateEnd = new GregorianCalendar(mYear, endMonth, endDay);

                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                            calDateBegin.getTimeInMillis() + Integer.parseInt(beginHour) * 60 * 60 * 1000 + Integer.parseInt(beginMinute) * 60 * 1000);
                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                            calDateEnd.getTimeInMillis() + Integer.parseInt(endHour) * 60 * 60 * 1000 + Integer.parseInt(endMinute) * 60 * 1000);

                    startActivity(calIntent);
                    finish();
                }
            }
        });


    }


    private void deleteItemFromTable(EventDTO eventDTO) {

        //////////////////---------------------->
        //для работы с БД.
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_EVENTS_NAME,
                TABLE_EVENTS_KEY_TITLE + " = ? AND "
                        + TABLE_EVENTS_KEY_BEGIN_DAY + " = ? AND "
                        + TABLE_EVENTS_KEY_BEGIN_MONTH + " = ? AND "
                        + TABLE_EVENTS_KEY_BEGIN_YEAR + " = ? AND "
                        + TABLE_EVENTS_KEY_DESCRIPTION + " = ? ",
                new String[]{
                        eventDTO.getTitle(),
                        String.valueOf(eventDTO.getDay()),
                        String.valueOf(eventDTO.getMonth()),
                        String.valueOf(eventDTO.getYear()),
                        eventDTO.getDescription()
                });

        //закрываем соединение с БД.
        dbHelper.close();
//////////////////---------------------->
    }

    //метод, для убирания клавиатуры EditText при нажатии на пустое пространство.
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                etEventDescription.removeTextChangedListener(this);

                try {
                    String initialString = s.toString();
                    SpannableString spannableString = new SpannableString(initialString);
                    bufferSpannableString = new SpannableString(spannableString);


                    for (String days : DAYS_OF_THE_WEEK.keySet()) {
                        initialWord = days;
                        spannableString = convertString(bufferSpannableString, initialWord);
                        bufferSpannableString = spannableString;
                    }


                    for (String timeWords : TIME_WORDS.keySet()) {
                        initialWord = timeWords;
                        spannableString = convertString(bufferSpannableString, initialWord);
                        bufferSpannableString = spannableString;
                    }


                    for (String dateRegExp : DATE_REGEXPS) {
                        strRegExp = dateRegExp;
                        spannableString = convertDateRegExps(bufferSpannableString, strRegExp);
                        bufferSpannableString = spannableString;
                    }


                    etEventDescription.setText(bufferSpannableString);

                    etEventDescription.setLinksClickable(true);
                    etEventDescription.setMovementMethod(LinkMovementMethod.getInstance());
                    etEventDescription.setSelection(etEventDescription.getText().length());


                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                etEventDescription.addTextChangedListener(this);
            }
        };
    }

    public SpannableString convertDateRegExps(SpannableString initialSpannableString, String strRegExp) {

        Pattern p = Pattern.compile(strRegExp);
        Matcher m = p.matcher(initialSpannableString.toString().replaceAll("[\\p{Cf}]", ""));

        SpannableString newSpannableString = new SpannableString(initialSpannableString);
        while (m.find()) {

            final int day = Integer.parseInt(m.group(1));

            final int month;
            if (m.group(3) != null) {
                month = getIntMonthFromString(m.group(3));
            } else {
                month = getCurrentMonth();
            }


            int year1 = getCurrentYear();
            if (m.group(5) != null) {
                year1 = Integer.parseInt(m.group(5));
                if (year1 < 100) {
                    if (year1 < 50) {
                        year1 += 2000;
                    } else {
                        year1 += 1900;
                    }
                }
            }

            final int year = year1;

            final int hour = getCurrentHour();
            final int minute = getCurrentMinute();


//String regExp = "(\\d{1,2})(\\s*)(января|янв|февраля|фев|марта|мар|апреля|апр|май|мая|июня|июн|июля|июл|августа|авг|сентября|сен|октября|окт|ноября|ноя|декабря|дек)(\\s*)(\\d{2,4}){0,1}";


            if (m.group(5) != null) {

                newSpannableString.setSpan(new ClickableSpan() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onClick(View widget) {

                        Intent calIntent = createCalendarIntent(year, month, day, hour, minute);
                        startActivity(calIntent);

                    }
                }, m.start(1), m.end(5), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            } else if (m.group(3) != null) {

                newSpannableString.setSpan(new ClickableSpan() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onClick(View widget) {

                        Intent calIntent = createCalendarIntent(year, month, day, hour, minute);
                        startActivity(calIntent);


                    }
                }, m.start(1), m.end(3), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {

                newSpannableString.setSpan(new ClickableSpan() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onClick(View widget) {

                        Intent calIntent = createCalendarIntent(year, month, day, hour, minute);
                        startActivity(calIntent);

                    }
                }, m.start(1), m.end(1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return newSpannableString;
    }

    @NonNull
    private Intent createCalendarIntent(int year, int month, int day, int hour, int minute) {
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setType("vnd.android.cursor.item/event");

        GregorianCalendar calDateBegin = new GregorianCalendar(
                year,
                month,
                day
        );
        GregorianCalendar calDateEnd = new GregorianCalendar(
                year,
                month,
                day
        );

        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDateBegin.getTimeInMillis() + hour * 60 * 60 * 1000 + minute * 60 * 1000);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calDateEnd.getTimeInMillis() + hour * 60 * 60 * 1000 + minute * 60 * 1000);
        return calIntent;
    }


    public SpannableString convertString(SpannableString initialSpannableString, final String initialWord) {

        List<Integer> indexesOfFirstLetters = getIndexesOfFirstLetters(initialSpannableString, initialWord);

        SpannableString newSpannableString = new SpannableString(initialSpannableString);

        for (Integer indexesOfFirstLetter : indexesOfFirstLetters) {
            newSpannableString.setSpan(new ClickableSpan() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                public void onClick(View widget) {

                    Intent calIntent = new Intent(Intent.ACTION_INSERT);
                    calIntent.setType("vnd.android.cursor.item/event");


                    int beginDay = getCurrentDay();
                    int beginMonth = getCurrentMonth();
                    int beginYear = getCurrentYear();

                    int beginHour = getCurrentHour();
                    int beginMinute = getCurrentMinute();

                    int endDay = getCurrentDay();
                    int endMonth = getCurrentMonth();
                    int endYear = getCurrentYear();

                    int endHour = getCurrentHour();
                    int endMinute = getCurrentMinute();

                    GregorianCalendar calDateBegin = new GregorianCalendar(
                            beginYear,
                            beginMonth,
                            beginDay + getDifferenceBetweenPlannedDayAndToday(initialWord)
                    );
                    GregorianCalendar calDateEnd = new GregorianCalendar(
                            endYear,
                            endMonth,
                            endDay + getDifferenceBetweenPlannedDayAndToday(initialWord)
                    );

                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                            calDateBegin.getTimeInMillis() + beginHour * 60 * 60 * 1000 + beginMinute * 60 * 1000);
                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                            calDateEnd.getTimeInMillis() + endHour * 60 * 60 * 1000 + endMinute * 60 * 1000);

                    startActivity(calIntent);

                }
            }, indexesOfFirstLetter, indexesOfFirstLetter + initialWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return newSpannableString;
    }


    public List<Integer> getIndexesOfFirstLetters(SpannableString initialSpannableString, String initialWord) {
        List<Integer> indexesOfFirstLetters = new ArrayList<Integer>();

        StringBuilder text = new StringBuilder(initialSpannableString);
        StringBuilder newString = new StringBuilder("");
        int intIndex;
        while (text.length() > initialWord.length()) {
            intIndex = text.indexOf(initialWord);
            if (intIndex == -1) {
                newString.append(text);
                break;
            } else {
                indexesOfFirstLetters.add(newString.length() + intIndex);
                newString.append(text.substring(0, intIndex)).append(initialWord);
                text = new StringBuilder(text.substring(intIndex + initialWord.length()));
            }
        }
        return indexesOfFirstLetters;
    }


    //For DatePicker
    private void updateDisplay() {
        this.tvEventsDeadline.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mDay).append("-")
                        .append(mMonth + 1).append("-")
                        .append(mYear).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isCardForUpdate = false;

        if (isSavedFlagEvent) {
            EventDTO newEventDTO = new EventDTO(
                    etCreateEventTitle.getText().toString(),
                    etEventDescription.getText().toString(),
                    mDay,
                    mMonth,
                    mYear
            );

            Intent intent = new Intent(this, EventActivity.class);
            intent.putExtra(KEY_EVENT_DTO, newEventDTO);
            startActivity(intent);
            Toast.makeText(this, newEventDTO.getTitle(), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Intent intent = new Intent(this, EventActivity.class);
            intent.putExtra(KEY_EVENT_DTO, eventDTO);
            startActivity(intent);
            Toast.makeText(this, eventDTO.getTitle(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
