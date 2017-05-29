package ua.a5.newnotes.activities;

import android.app.Activity;
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
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.a5.newnotes.DAO.DBHelper;
import ua.a5.newnotes.R;
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

    public static final String LOG_TAG = "log";

    public static SpannableString bufferSpannableString = null;

    String initialWord;
    String strRegExp;

    public static String DEFAULT_DAY = String.valueOf(Calendar.getInstance().get(Calendar.DATE));
    public static String DEFAULT_MONTH = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
    public static String DEFAULT_YEAR = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    public static String DEFAULT_HOUR = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
    public static String DEFAULT_MINUTE = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));


    EditText etCreateEventTitle;
    EditText etCreateEventLocation;

    EditText etCreateEventBeginDay;
    EditText etCreateEventBeginMonth;
    EditText etCreateEventBeginHour;
    EditText etCreateEventBeginMinute;

    EditText etCreateEventEndDay;
    EditText etCreateEventEndMonth;
    EditText etCreateEventEndHour;
    EditText etCreateEventEndMinute;


    EditText etEventDescription;


    Button btnCreateEventSave;
    Button btnCreateEventEventsMenu;
    Button btnCreateEventCalendar;


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


                beginHour = etCreateEventBeginHour.getText().toString();
                beginMinute = etCreateEventBeginMinute.getText().toString();


                endDay = etCreateEventEndDay.getText().toString();
                endMonth = etCreateEventEndMonth.getText().toString();


                endHour = etCreateEventEndHour.getText().toString();
                endMinute = etCreateEventEndMinute.getText().toString();


                description = etEventDescription.getText().toString();


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
                location = etCreateEventLocation.getText().toString();
                description = etEventDescription.getText().toString();


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
                    finish();
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
                    finish();
                }
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
}
