package ua.a5.newnotes.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
import static ua.a5.newnotes.activities.OptionsMenuActivity.mIsPremium;
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
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsMethods.convertDateRegExps;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsMethods.convertString;

public class CreateEventActivity extends AppCompatActivity {
    public static SpannableString bufferSpannableString = null;
    public static final int DATE_DIALOG_ID = 2;

    boolean isSavedFlagEvent;

    String initialWord;
    String strRegExp;

    private int mDay;
    private int mMonth;
    private int mYear;

    EventDTO event;
    EventDTO eventDTO;
    String title;
    String location;
    String description;

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

    private TextView tvEventsDeadline;
    private Button btnEventsDeadline;

    EditText etCreateEventTitle;
    EditText etEventDescription;

    Button btnCreateEventSave;
    Button btnCreateEventEventsMenu;
    Button btnCreateEventCalendar;

    //для работы с БД.
    DBHelper dbHeper;

    //для баннера////////////////////////////////////////////////////
    protected AdView mAdView;
    //для баннера////////////////////////////////////////////////////

    //для Interstitial////////////////////////////////////////////////////
    InterstitialAd mInterstitialAd;
    //для Interstitial////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        if (mIsPremium == false) {
//Initializing the Google Mobile Ads SDK
            MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));
//Initializing the Google Mobile Ads SDK

            //для баннера////////////////////////////////////////////////////
            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    // Check the LogCat to get your test device ID
                    .addTestDevice("9E89078D0DC2D94ADC3D89109C5B6E24")
                    .build();
            mAdView.loadAd(adRequest);
            //для баннера////////////////////////////////////////////////////

//для Interstitial////////////////////////////////////////////////////
            mInterstitialAd = new InterstitialAd(this);
            //set the ad unit ID
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

            //Load ads into Interstitial Ads
            mInterstitialAd.loadAd(adRequest);
            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }
            });
//для Interstitial////////////////////////////////////////////////////

        }

        isSavedFlagEvent = false;
        description = null;
        //для работы с БД.
        dbHeper = new DBHelper(this);

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

            if (eventDTO.getMonth() + 1 < 10) {
                tvEventsDeadline.setText(new StringBuilder()
                        // Month is 0 based so add 1
                        .append(eventDTO.getDay()).append("-0")
                        .append(eventDTO.getMonth() + 1).append("-")
                        .append(eventDTO.getYear()).append(" "));
            } else {
                tvEventsDeadline.setText(new StringBuilder()
                        // Month is 0 based so add 1
                        .append(eventDTO.getDay()).append("-")
                        .append(eventDTO.getMonth() + 1).append("-")
                        .append(eventDTO.getYear()).append(" "));
            }

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
                SQLiteDatabase sqLiteDatabase = dbHeper.getWritableDatabase();

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
                dbHeper.close();

////////////////////////

                isSavedFlagEvent = true;
            }
        });

        btnCreateEventEventsMenu = (Button) findViewById(R.id.event_btnCreateEventEventsMenu);
        btnCreateEventEventsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCardForUpdate = false;
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
                beginMonth = mMonth;
                beginYear = mYear;
                beginHour = String.valueOf(getCurrentHour());
                beginMinute = String.valueOf(getCurrentMinute());


                endDay = mDay;
                endMonth = mMonth;
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


    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
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


    //For DatePicker
    private void updateDisplay() {
        if (mMonth + 1 < 10) {
            this.tvEventsDeadline.setText(
                    new StringBuilder()
                            //Month is 0 based so add 1
                            .append(mDay).append("-0")
                            .append(mMonth + 1).append("-")
                            .append(mYear).append(" "));
        } else {
            this.tvEventsDeadline.setText(
                    new StringBuilder()
                            //Month is 0 based so add 1
                            .append(mDay).append("-")
                            .append(mMonth + 1).append("-")
                            .append(mYear).append(" "));
        }
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
            finish();
        } else {
            Intent intent = new Intent(this, EventActivity.class);
            intent.putExtra(KEY_EVENT_DTO, eventDTO);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
