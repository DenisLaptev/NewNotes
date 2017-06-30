package ua.a5.newnotes.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Calendar;

import ua.a5.newnotes.DAO.DBHelper;
import ua.a5.newnotes.R;
import ua.a5.newnotes.activities.notes_activities.DifferentActivity;
import ua.a5.newnotes.dto.notesDTO.DifferentDTO;

import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_DIFFERENT_KEY_DATE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_DIFFERENT_KEY_DESCRIPTION;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_DIFFERENT_KEY_TITLE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_DIFFERENT_NAME;
import static ua.a5.newnotes.activities.OptionsMenuActivity.mIsPremium;
import static ua.a5.newnotes.utils.Constants.KEY_DIFFERENT_DTO;
import static ua.a5.newnotes.utils.Constants.KEY_UPDATE_DIFFERENT;
import static ua.a5.newnotes.utils.Constants.LOG_TAG;
import static ua.a5.newnotes.utils.Constants.isCardForUpdate;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.DATE_REGEXPS;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.DAYS_OF_THE_WEEK;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.TIME_WORDS;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentDay;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentMonth;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentYear;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsMethods.convertDateRegExps;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsMethods.convertString;

public class CreateNoteDifferentActivity extends AppCompatActivity {
    public static SpannableString bufferSpannableString = null;

    boolean isSavedFlagDifferent;

    String initialWord;
    String strRegExp;

    DifferentDTO differentDTO;
    DifferentDTO note;
    String noteCategory;
    String noteTitle;
    String noteDescription;
    String noteDate;

    TextView tvCreateNoteDate;
    TextView tvCreateNoteTime;

    EditText etCreateNoteTitle;
    EditText etNoteDescription;

    Button btnCreateNoteSave;
    Button btnCreateNoteNotesMenu;

    //для работы с БД.
    DBHelper dbHelper;


    //для баннера////////////////////////////////////////////////////
    protected AdView mAdView;
    //для баннера////////////////////////////////////////////////////

    //для Interstitial////////////////////////////////////////////////////
    InterstitialAd mInterstitialAd;
    //для Interstitial////////////////////////////////////////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note_different);

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

        isSavedFlagDifferent = false;

        //для работы с БД.
        dbHelper = new DBHelper(this);
        setDateAndTime();


        etCreateNoteTitle = (EditText) findViewById(R.id.et_diff_title);
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        etCreateNoteTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        etNoteDescription = (EditText) findViewById(R.id.et_diff_description);
        etNoteDescription.addTextChangedListener(onTextChangedListener());
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        etNoteDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        if (isCardForUpdate == true && getIntent() != null) {
            differentDTO = (DifferentDTO) getIntent().getSerializableExtra(KEY_UPDATE_DIFFERENT);
            etCreateNoteTitle.setText(differentDTO.getTitle());
            etNoteDescription.setText(differentDTO.getDescription());
        }else {
            differentDTO = new DifferentDTO(
                    new String(""),
                    new String(""),
                    new String(
                            getCurrentDay() + "-" +
                                    getCurrentMonth() + "-" +
                                    getCurrentYear()
                    )
            );
        }

        btnCreateNoteSave = (Button) findViewById(R.id.btn_diff_save);
        btnCreateNoteSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCardForUpdate == true) {
                    deleteItemFromTable(differentDTO);
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

                noteCategory = "different";
                noteTitle = etCreateNoteTitle.getText().toString();
                noteDescription = etNoteDescription.getText().toString();
                noteDate = tvCreateNoteDate.getText().toString();



                note = new DifferentDTO(noteTitle, noteDescription, noteDate);
                System.out.println(note);


                contentValues.put(TABLE_NOTES_DIFFERENT_KEY_TITLE, noteTitle);
                contentValues.put(TABLE_NOTES_DIFFERENT_KEY_DATE, noteDate);
                contentValues.put(TABLE_NOTES_DIFFERENT_KEY_DESCRIPTION, noteDescription);
                //id заполнится автоматически.

                //вставляем подготовленные строки в таблицу.
                //второй аргумент используется для вставки пустой строки,
                //сейчас он нам не нужен, поэтому он = null.
                sqLiteDatabase.insert(DBHelper.TABLE_NOTES_DIFFERENT_NAME, null, contentValues);
                Log.d(LOG_TAG, "Date inserted");
                //закрываем соединение с БД.
                dbHelper.close();
////////////////////////

                isSavedFlagDifferent = true;
            }
        });

        btnCreateNoteNotesMenu = (Button) findViewById(R.id.btn_diff_allnotes);
        btnCreateNoteNotesMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCardForUpdate = false;
                onBackPressed();
                finish();
            }
        });

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void deleteItemFromTable(DifferentDTO differentDTO) {
//////////////////---------------------->
        //для работы с БД.
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_NOTES_DIFFERENT_NAME,
                TABLE_NOTES_DIFFERENT_KEY_TITLE + " = ? AND "
                        + TABLE_NOTES_DIFFERENT_KEY_DATE + " = ? AND "
                        + TABLE_NOTES_DIFFERENT_KEY_DESCRIPTION + " = ? ",
                new String[]{
                        differentDTO.getTitle(),
                        String.valueOf(differentDTO.getDate()),
                        String.valueOf(differentDTO.getDescription())
                });

        //закрываем соединение с БД.
        dbHelper.close();
//////////////////---------------------->
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

                etNoteDescription.removeTextChangedListener(this);

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

                    etNoteDescription.setText(bufferSpannableString);

                    etNoteDescription.setLinksClickable(true);
                    etNoteDescription.setMovementMethod(LinkMovementMethod.getInstance());
                    etNoteDescription.setSelection(etNoteDescription.getText().length());


                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                etNoteDescription.addTextChangedListener(this);
            }
        };
    }


    //метод, для убирания клавиатуры EditText при нажатии на пустое пространство.
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void setDateAndTime() {
        setTime();
        setDate();
    }

    private void setDate() {
        Calendar cal;

        cal = Calendar.getInstance();
        int dayofyear = cal.get(Calendar.DAY_OF_YEAR);

        int year = cal.get(Calendar.YEAR);
        String strYear = String.valueOf(year);

        int month = cal.get(Calendar.MONTH);
        String strMonth = null;
        if (month <= 8) {
            strMonth = "0" + (++month);
        } else {
            strMonth = String.valueOf(month);
        }

        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);

        int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);
        String strDayOfMonth = String.valueOf(dayofmonth);

        System.out.println("Date: (" + dayofweek + ") " + dayofmonth + "-" + (++month) + "-" + year);

        tvCreateNoteDate = (TextView) findViewById(R.id.note_tvCreateNoteDifferentDate);
        tvCreateNoteDate.setText(strDayOfMonth + "-" + strMonth + "-" + strYear);
    }

    private void setTime() {
        Calendar cal = Calendar.getInstance();
        int millisecond = cal.get(Calendar.MILLISECOND);
        int second = cal.get(Calendar.SECOND);
        String strSecond = null;
        if (second <= 9) {
            strSecond = "0" + second;
        } else {
            strSecond = String.valueOf(second);
        }
        int minute = cal.get(Calendar.MINUTE);
        String strMinute = null;
        if (minute <= 9) {
            strMinute = "0" + minute;
        } else {
            strMinute = String.valueOf(minute);
        }
        //12 hour format
        int hour = cal.get(Calendar.HOUR);
        //24 hour format
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);
        String strHourOfDay = String.valueOf(hourofday);
        System.out.println("Time: " + hourofday + ":" + minute + ":" + second);

        tvCreateNoteTime = (TextView) findViewById(R.id.note_tvCreateNoteDifferentTime);
        tvCreateNoteTime.setText(strHourOfDay + " : " + strMinute + " : " + strSecond);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isCardForUpdate = false;

        if (isSavedFlagDifferent) {
            DifferentDTO newDifferentDTO = new DifferentDTO(
                    etCreateNoteTitle.getText().toString(),
                    etNoteDescription.getText().toString(),
                    tvCreateNoteDate.getText().toString()

            );

            Intent intent = new Intent(this, DifferentActivity.class);
            intent.putExtra(KEY_DIFFERENT_DTO, newDifferentDTO);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, DifferentActivity.class);
            intent.putExtra(KEY_DIFFERENT_DTO, differentDTO);
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
