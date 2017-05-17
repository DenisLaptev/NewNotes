package ua.a5.newnotes;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.regex.Pattern;

import ua.a5.newnotes.DAO.DBHelper;
import ua.a5.newnotes.model.Note;
import ua.a5.newnotes.utils.MyMovementMethod;

import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_KEY_CATEGORY;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_KEY_IMPORTANCE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_KEY_NOTETEXT;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_KEY_TITLE;

public class CreateNoteActivity extends AppCompatActivity implements TextWatcher {
    //Интерфейс TextWatcher нужен для работы с Т9 (AutoCompleteTextView actvNote)

    public static final String LOG_TAG = "log";

    public static final String[] WORDS = {"today", "tomorrow", "day before"};

    TextView tvCreateNoteDate;
    TextView tvCreateNoteTime;

    Spinner spinnerCreateNoteCategory;

    EditText etCreateNoteTitle;
    //CheckBox chbCreateNoteImportant;

    AutoCompleteTextView actvCreateNoteNote;
    TextView tvCreateNoteNote;

    Button btnCreateNoteSave;
    Button btnCreateNoteNotesMenu;
    //Button btnCreateNoteCalendar;

    //для работы с БД.
    DBHelper dbHelper;

    Note note;
    String noteCategory;
    String noteTitle;
    int noteImportance;
    String noteFullText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        //для работы с БД.
        dbHelper = new DBHelper(this);
        setDateAndTime();


        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,list);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.xml.spinner_item,list);


        spinnerCreateNoteCategory = (Spinner) findViewById(R.id.note_spinnerCreateNoteCategory);
        //ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.Categories, android.R.layout.simple_spinner_item);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.Categories, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCreateNoteCategory.setAdapter(adapter);
        spinnerCreateNoteCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] array = getResources().getStringArray(R.array.Categories);

                noteCategory = array[position];
                //Toast.makeText(getApplicationContext(), "noteCategory=" + array[position], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        etCreateNoteTitle = (EditText) findViewById(R.id.note_etCreateNoteTitle);
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


        //chbCreateNoteImportant = (CheckBox) findViewById(R.id.note_chbCreateNoteImportant);


        actvCreateNoteNote = (AutoCompleteTextView) findViewById(R.id.note_actvCreateNoteNote);
        actvCreateNoteNote.addTextChangedListener(this);
        actvCreateNoteNote.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, WORDS));
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        actvCreateNoteNote.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        actvCreateNoteNote.setLinksClickable(true);
        actvCreateNoteNote.setAutoLinkMask(Linkify.WEB_URLS);
        actvCreateNoteNote.setMovementMethod(MyMovementMethod.getInstance());
        //If the edit text contains previous text with potential links
        Linkify.addLinks(actvCreateNoteNote, Linkify.WEB_URLS);


        tvCreateNoteNote = (TextView) findViewById(R.id.note_tvCreateNoteNote);
        Linkify.addLinks(tvCreateNoteNote, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);

        int flags = Pattern.CASE_INSENSITIVE;
        Pattern p = Pattern.compile("\\bquake[0-9]*\\b", flags);
        Linkify.addLinks(
                tvCreateNoteNote,
                p,
                "content://com.paad.earthquake/earthquakes/"
        );

        tvCreateNoteNote.setMovementMethod(LinkMovementMethod.getInstance());


        btnCreateNoteSave = (Button) findViewById(R.id.note_btnCreateNoteSave);
        btnCreateNoteSave.setOnClickListener(new View.OnClickListener() {
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


                noteTitle = etCreateNoteTitle.getText().toString();

/*

                if (!chbCreateNoteImportant.isChecked()) {
                    noteImportance = 0;
                } else {
                    noteImportance = 1;
                }
*/

                noteFullText = tvCreateNoteNote.getText().toString();

                note = new Note(noteCategory, noteTitle, noteImportance, noteFullText);
                System.out.println(note);


                contentValues.put(TABLE_NOTES_KEY_CATEGORY, noteCategory);
                contentValues.put(TABLE_NOTES_KEY_TITLE, noteTitle);
                contentValues.put(TABLE_NOTES_KEY_IMPORTANCE, noteImportance);
                contentValues.put(TABLE_NOTES_KEY_NOTETEXT, noteFullText);
                //id заполнится автоматически.

                //вставляем подготовленные строки в таблицу.
                //второй аргумент используется для вставки пустой строки,
                //сейчас он нам не нужен, поэтому он = null.
                sqLiteDatabase.insert(DBHelper.TABLE_NOTES_NAME, null, contentValues);
                Log.d(LOG_TAG, "Date inserted");
                //закрываем соединение с БД.
                dbHelper.close();
////////////////////////


            }
        });

        btnCreateNoteNotesMenu = (Button) findViewById(R.id.note_btnCreateNoteNotesMenu);
        btnCreateNoteNotesMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(CreateNoteActivity.this, NotesActivity.class);
                //startActivity(intent);
                onBackPressed();
                finish();
            }
        });


    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tvCreateNoteNote.setText(actvCreateNoteNote.getText());

    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = actvCreateNoteNote.getText().toString();
        if (str.equals("привет")) {
            tvCreateNoteNote.setText("Ура!");
            actvCreateNoteNote.setTextColor(Color.RED);
        }

        Linkify.addLinks(s, Linkify.WEB_URLS);
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


        tvCreateNoteDate = (TextView) findViewById(R.id.note_tvCreateNoteDate);
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

        tvCreateNoteTime = (TextView) findViewById(R.id.note_tvCreateNoteTime);
        tvCreateNoteTime.setText(strHourOfDay + " : " + strMinute + " : " + strSecond);
    }
}
