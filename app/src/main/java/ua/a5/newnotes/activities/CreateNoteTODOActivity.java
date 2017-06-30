package ua.a5.newnotes.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
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
import ua.a5.newnotes.activities.notes_activities.TodoActivity;
import ua.a5.newnotes.dto.notesDTO.TodoDTO;

import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_DAY;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_DESCRIPTION;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_ISDONE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_MONTH;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_TITLE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_YEAR;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_NAME;
import static ua.a5.newnotes.activities.OptionsMenuActivity.mIsPremium;
import static ua.a5.newnotes.utils.Constants.KEY_TODO_DTO;
import static ua.a5.newnotes.utils.Constants.KEY_UPDATE_TODO;
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

public class CreateNoteTODOActivity extends AppCompatActivity {
    public static SpannableString bufferSpannableString = null;
    static final int DATE_DIALOG_ID = 1;

    boolean isSavedFlagTODO;

    String initialWord;
    String strRegExp;

    private int mYear;
    private int mMonth;
    private int mDay;

    TodoDTO todoDTO;
    TodoDTO note;
    String noteCategory;
    String noteTitle;
    int isDone;
    int noteDay;
    int noteMonth;
    int noteYear;
    String noteDescription;


    private TextView tvTodoDeadline;
    private Button btnDeadline;

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
        setContentView(R.layout.activity_create_note_todo);

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
        isSavedFlagTODO = false;

        //для работы с БД.
        dbHelper = new DBHelper(this);

        noteCategory = "todo";

        etCreateNoteTitle = (EditText) findViewById(R.id.note_etCreateNoteTODOTitle);
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

        btnDeadline = (Button) findViewById(R.id.btn_todo_date_picker);
        btnDeadline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        //get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        tvTodoDeadline = (TextView) findViewById(R.id.tv_todo_deadline_date);
        //display the current date
        updateDisplay();

        etNoteDescription = (EditText) findViewById(R.id.et_note_todo_description);
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
            todoDTO = (TodoDTO) getIntent().getSerializableExtra(KEY_UPDATE_TODO);
            etCreateNoteTitle.setText(todoDTO.getTitle());
            etNoteDescription.setText(todoDTO.getDescription());

            if(todoDTO.getMonth() + 1 < 10) {
                tvTodoDeadline.setText(
                        new StringBuilder()
                                //Month is 0 based so add 1
                                .append(todoDTO.getDay()).append("-0")
                                .append(todoDTO.getMonth() + 1).append("-")
                                .append(todoDTO.getYear()).append(" "));
            }else{
                tvTodoDeadline.setText(
                        new StringBuilder()
                                //Month is 0 based so add 1
                                .append(todoDTO.getDay()).append("-")
                                .append(todoDTO.getMonth() + 1).append("-")
                                .append(todoDTO.getYear()).append(" "));
            }
        }
        else{
            todoDTO = new TodoDTO(new String(""),0,getCurrentDay(),getCurrentMonth(),getCurrentYear(),new String(""));
        }

        btnCreateNoteSave = (Button) findViewById(R.id.note_btnCreateNoteTODOSave);
        btnCreateNoteSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCardForUpdate == true) {
                    deleteItemFromTable(todoDTO);
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
                isDone = 0;
                noteDay = mDay;
                noteMonth = mMonth;
                noteYear = mYear;
                noteDescription = etNoteDescription.getText().toString();

                note = new TodoDTO(noteTitle, isDone, noteDay, noteMonth, noteYear, noteDescription);


                contentValues.put(TABLE_NOTES_TODO_KEY_TITLE, noteTitle);
                contentValues.put(TABLE_NOTES_TODO_KEY_ISDONE, isDone);
                contentValues.put(TABLE_NOTES_TODO_KEY_DAY, noteDay);
                contentValues.put(TABLE_NOTES_TODO_KEY_MONTH, noteMonth);
                contentValues.put(TABLE_NOTES_TODO_KEY_YEAR, noteYear);
                contentValues.put(TABLE_NOTES_TODO_KEY_DESCRIPTION, noteDescription);
                //id заполнится автоматически.

                //вставляем подготовленные строки в таблицу.
                //второй аргумент используется для вставки пустой строки,
                //сейчас он нам не нужен, поэтому он = null.
                sqLiteDatabase.insert(DBHelper.TABLE_NOTES_TODO_NAME, null, contentValues);
                Log.d(LOG_TAG, "Date inserted");
                //закрываем соединение с БД.
                dbHelper.close();
////////////////////////
                isSavedFlagTODO = true;

            }
        });

        btnCreateNoteNotesMenu = (Button) findViewById(R.id.note_btnCreateNoteTODONotesMenu);
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


    private void deleteItemFromTable(TodoDTO todoDTO) {

//////////////////---------------------->
        //для работы с БД.
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_NOTES_TODO_NAME,
                TABLE_NOTES_TODO_KEY_TITLE + " = ? AND "
                        + TABLE_NOTES_TODO_KEY_DAY + " = ? AND "
                        + TABLE_NOTES_TODO_KEY_MONTH + " = ? AND "
                        + TABLE_NOTES_TODO_KEY_YEAR + " = ? AND "
                        + TABLE_NOTES_TODO_KEY_DESCRIPTION + " = ? ",
                new String[]{
                        todoDTO.getTitle(),
                        String.valueOf(todoDTO.getDay()),
                        String.valueOf(todoDTO.getMonth()),
                        String.valueOf(todoDTO.getYear()),
                        String.valueOf(todoDTO.getDescription())
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


    //For DatePicker
    private void updateDisplay() {

        if(mMonth + 1 < 10) {
            this.tvTodoDeadline.setText(
                    new StringBuilder()
                            // Month is 0 based so add 1
                            .append(mDay).append("-0")
                            .append(mMonth + 1).append("-")
                            .append(mYear).append(" "));
        }else{
            this.tvTodoDeadline.setText(
                    new StringBuilder()
                            // Month is 0 based so add 1
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

        if (isSavedFlagTODO) {
            TodoDTO newTodoDTO = new TodoDTO(
                    etCreateNoteTitle.getText().toString(),
                    0,
                    mDay,
                    mMonth,
                    mYear,
                    etNoteDescription.getText().toString()

            );

            Intent intent = new Intent(this, TodoActivity.class);
            intent.putExtra(KEY_TODO_DTO, newTodoDTO);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, TodoActivity.class);
            intent.putExtra(KEY_TODO_DTO, todoDTO);
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
