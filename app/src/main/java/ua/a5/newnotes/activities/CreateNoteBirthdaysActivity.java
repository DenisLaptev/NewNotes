package ua.a5.newnotes.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import ua.a5.newnotes.DAO.DBHelper;
import ua.a5.newnotes.R;
import ua.a5.newnotes.dto.notesDTO.BirthdayDTO;

import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_BIRTHDAYS_KEY_DAY;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_BIRTHDAYS_KEY_MONTH;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_BIRTHDAYS_KEY_NAME;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_BIRTHDAYS_KEY_YEAR;
import static ua.a5.newnotes.adapter.notesListAdapters.BirthdaysListAdapter.KEY_UPDATE_BIRTHDAYS;
import static ua.a5.newnotes.utils.Constants.isCardForUpdate;

public class CreateNoteBirthdaysActivity extends AppCompatActivity {

    private int mYear;
    private int mMonth;
    private int mDay;
    private TextView dateDisplay;
    private Button btnPickDate;
    static final int DATE_DIALOG_ID = 0;


    public static final String LOG_TAG = "log";

    EditText etName;

    Button btnSaveBirthdayNote;
    Button btnAllNotes;

    //для работы с БД.
    DBHelper dbHelper;

    BirthdayDTO note;
    String noteCategory;
    String noteName;
    int noteDay;
    int noteMonth;
    int noteYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note_birthdays);

        //для работы с БД.
        dbHelper = new DBHelper(this);


        etName = (EditText) findViewById(R.id.et_name);
        //этот слушатель позволяет убирать клавиатуру EditText
        //при нажатии на пустое пространство.
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        dateDisplay = (TextView) findViewById(R.id.tv_birthday_date);
        btnPickDate = (Button) findViewById(R.id.btn_date_picker);

        btnPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // display the current date
        updateDisplay();


        if (isCardForUpdate == true && getIntent() != null) {
            BirthdayDTO birthdayDTO = (BirthdayDTO) getIntent().getSerializableExtra(KEY_UPDATE_BIRTHDAYS);
            etName.setText(birthdayDTO.getName());
            dateDisplay.setText(
                    new StringBuilder()
                            // Month is 0 based so add 1
                            .append(birthdayDTO.getDay()).append("-")
                            .append(birthdayDTO.getMonth() + 1).append("-")
                            .append(birthdayDTO.getYear()).append(" "));
        }

        btnSaveBirthdayNote = (Button) findViewById(R.id.btn_save);
        btnSaveBirthdayNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                noteCategory = "birthdays";
                noteName = etName.getText().toString();
                noteDay = mDay;
                noteMonth = mMonth;
                noteYear = mYear;

                note = new BirthdayDTO(noteName, noteDay, noteMonth, noteYear);

                contentValues.put(TABLE_NOTES_BIRTHDAYS_KEY_NAME, noteName);
                contentValues.put(TABLE_NOTES_BIRTHDAYS_KEY_DAY, noteDay);
                contentValues.put(TABLE_NOTES_BIRTHDAYS_KEY_MONTH, noteMonth);
                contentValues.put(TABLE_NOTES_BIRTHDAYS_KEY_YEAR, noteYear);
                //id заполнится автоматически.

                //вставляем подготовленные строки в таблицу.
                //второй аргумент используется для вставки пустой строки,
                //сейчас он нам не нужен, поэтому он = null.
                sqLiteDatabase.insert(DBHelper.TABLE_NOTES_BIRTHDAYS_NAME, null, contentValues);
                Log.d(LOG_TAG, "Date inserted");
                //закрываем соединение с БД.
                dbHelper.close();
////////////////////////

            }
        });

        btnAllNotes = (Button) findViewById(R.id.btn_allnotes);
        btnAllNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCardForUpdate = false;
                onBackPressed();
            }
        });
    }


    //метод, для убирания клавиатуры EditText при нажатии на пустое пространство.
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    //For DatePicker
    private void updateDisplay() {
        this.dateDisplay.setText(
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
    }
}
