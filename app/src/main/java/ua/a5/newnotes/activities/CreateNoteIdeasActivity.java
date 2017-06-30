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
import ua.a5.newnotes.activities.notes_activities.IdeaActivity;
import ua.a5.newnotes.dto.notesDTO.IdeaDTO;

import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_IDEAS_KEY_DATE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_IDEAS_KEY_DESCRIPTION;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_IDEAS_KEY_TITLE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_IDEAS_NAME;
import static ua.a5.newnotes.activities.OptionsMenuActivity.mIsPremium;
import static ua.a5.newnotes.utils.Constants.KEY_IDEA_DTO;
import static ua.a5.newnotes.utils.Constants.KEY_UPDATE_IDEAS;
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

public class CreateNoteIdeasActivity extends AppCompatActivity {
    public static SpannableString bufferSpannableString = null;

    boolean isSavedFlagIdea;

    String initialWord;
    String strRegExp;

    IdeaDTO ideaDTO;
    IdeaDTO note;
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
        setContentView(R.layout.activity_create_note_ideas);

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

        isSavedFlagIdea = false;

        //для работы с БД.
        dbHelper = new DBHelper(this);
        setDateAndTime();

        noteCategory = "ideas";

        etCreateNoteTitle = (EditText) findViewById(R.id.note_etCreateNoteIdeasTitle);
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


        etNoteDescription = (EditText) findViewById(R.id.et_note_ideas_description);
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
            ideaDTO = (IdeaDTO) getIntent().getSerializableExtra(KEY_UPDATE_IDEAS);
            etCreateNoteTitle.setText(ideaDTO.getTitle());
            etNoteDescription.setText(ideaDTO.getDescription());
        } else {
            ideaDTO = new IdeaDTO(
                    new String(""),
                    new String(""),
                    new String(
                            getCurrentDay() + "-" +
                            getCurrentMonth() + "-" +
                            getCurrentYear()
                    )
            );
        }

        btnCreateNoteSave = (Button) findViewById(R.id.note_btnCreateNoteIdeasSave);
        btnCreateNoteSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCardForUpdate == true) {
                    deleteItemFromTable(ideaDTO);
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


                noteTitle = etCreateNoteTitle.getText().toString();
                noteDescription = etNoteDescription.getText().toString();
                noteDate = tvCreateNoteDate.getText().toString();

                note = new IdeaDTO(noteTitle, noteDescription, noteDate);

                contentValues.put(TABLE_NOTES_IDEAS_KEY_TITLE, noteTitle);
                contentValues.put(TABLE_NOTES_IDEAS_KEY_DATE, noteDate);
                contentValues.put(TABLE_NOTES_IDEAS_KEY_DESCRIPTION, noteDescription);
                //id заполнится автоматически.

                //вставляем подготовленные строки в таблицу.
                //второй аргумент используется для вставки пустой строки,
                //сейчас он нам не нужен, поэтому он = null.
                sqLiteDatabase.insert(DBHelper.TABLE_NOTES_IDEAS_NAME, null, contentValues);
                Log.d(LOG_TAG, "Date inserted");
                //закрываем соединение с БД.
                dbHelper.close();
////////////////////////
                isSavedFlagIdea = true;

            }
        });

        btnCreateNoteNotesMenu = (Button) findViewById(R.id.note_btnCreateNoteIdeasNotesMenu);
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


    private void deleteItemFromTable(IdeaDTO ideaDTO) {
//////////////////---------------------->
        //для работы с БД.
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_NOTES_IDEAS_NAME,
                TABLE_NOTES_IDEAS_KEY_TITLE + " = ? AND "
                        + TABLE_NOTES_IDEAS_KEY_DATE + " = ? AND "
                        + TABLE_NOTES_IDEAS_KEY_DESCRIPTION + " = ? ",
                new String[]{
                        ideaDTO.getTitle(),
                        String.valueOf(ideaDTO.getDate()),
                        String.valueOf(ideaDTO.getDescription())
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


        tvCreateNoteDate = (TextView) findViewById(R.id.note_tvCreateNoteIdeasDate);
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

        tvCreateNoteTime = (TextView) findViewById(R.id.note_tvCreateNoteIdeasTime);
        tvCreateNoteTime.setText(strHourOfDay + " : " + strMinute + " : " + strSecond);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isCardForUpdate = false;


        if (isSavedFlagIdea) {
            IdeaDTO newIdeaDTO = new IdeaDTO(
                    etCreateNoteTitle.getText().toString(),
                    etNoteDescription.getText().toString(),
                    tvCreateNoteDate.getText().toString()

            );

            Intent intent = new Intent(this, IdeaActivity.class);
            intent.putExtra(KEY_IDEA_DTO, newIdeaDTO);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, IdeaActivity.class);
            intent.putExtra(KEY_IDEA_DTO, ideaDTO);
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
