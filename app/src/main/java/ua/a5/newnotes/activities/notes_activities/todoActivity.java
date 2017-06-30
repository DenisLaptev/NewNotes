package ua.a5.newnotes.activities.notes_activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.a5.newnotes.DAO.DBHelper;
import ua.a5.newnotes.R;
import ua.a5.newnotes.activities.CreateNoteTODOActivity;
import ua.a5.newnotes.dto.notesDTO.TodoDTO;

import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_DAY;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_DESCRIPTION;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_ISDONE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_MONTH;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_TITLE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_KEY_YEAR;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_TODO_NAME;
import static ua.a5.newnotes.R.id.delete_item;
import static ua.a5.newnotes.R.id.update_item;
import static ua.a5.newnotes.utils.Constants.KEY_TODO_DTO;
import static ua.a5.newnotes.utils.Constants.KEY_UPDATE_TODO;
import static ua.a5.newnotes.utils.Constants.isCardForUpdate;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.DATE_REGEXPS;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.DAYS_OF_THE_WEEK;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.TIME_WORDS;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsMethods.convertDateRegExps;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsMethods.convertString;

public class TodoActivity extends AppCompatActivity {

    public static SpannableString bufferSpannableString = null;

    String initialWord;
    String strRegExp;
    int isDone;

    @BindView(R.id.tv_todo_activity_title)
    TextView tvTitle;

    @BindView(R.id.tv_todo_activity_date)
    TextView tvDate;

    @BindView(R.id.chbx_todo_activity)
    CheckBox chbxIsDone;

    @BindView(R.id.tv_todo_activity_description)
    TextView tvDescription;

    @BindView(R.id.iv_todo_menu)
    ImageView ivTodoMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        ButterKnife.bind(this);

        if (getIntent() != null) {
            final TodoDTO todoDTO = (TodoDTO) getIntent().getSerializableExtra(KEY_TODO_DTO);
            tvTitle.setText(todoDTO.getTitle());
            if(todoDTO.getMonth()+1 < 10) {
                tvDate.setText(todoDTO.getDay() + "-0" + (todoDTO.getMonth() + 1) + "-" + todoDTO.getYear());
            }else{
                tvDate.setText(todoDTO.getDay() + "-" + (todoDTO.getMonth() + 1) + "-" + todoDTO.getYear());
            }
            isDone = todoDTO.getIsDone();
            if (isDone == 0) {
                chbxIsDone.setChecked(false);

            } else {
                chbxIsDone.setChecked(true);
            }
            chbxIsDone.setText("ВЫПОЛНЕНО");
            chbxIsDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        todoDTO.setIsDone(1);
                        updateTodoCheckboxUIandDB(todoDTO);
                    } else {
                        todoDTO.setIsDone(0);
                        updateTodoCheckboxUIandDB(todoDTO);
                    }
                }
            });


            try {
                SpannableString spannableString = new SpannableString(todoDTO.getDescription());
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
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
            tvDescription.setText(bufferSpannableString);
            tvDescription.setLinksClickable(true);
            tvDescription.setMovementMethod(LinkMovementMethod.getInstance());

            ivTodoMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu cardPopupMenu = new PopupMenu(TodoActivity.this, ivTodoMenu);
                    cardPopupMenu.getMenuInflater().inflate(R.menu.menu_card, cardPopupMenu.getMenu());

                    cardPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem it) {

                            switch (it.getItemId()) {
                                case delete_item:

                                    AlertDialog.Builder builder = new AlertDialog.Builder(TodoActivity.this, R.style.MyAlertDialogStyle);
                                    builder.setTitle("Delete?");
                                    builder.setMessage("Do You Really Want To Delete?");

                                    //positive button.
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(TodoActivity.this, "delete", Toast.LENGTH_SHORT).show();
                                            deleteItemFromTable(todoDTO);
                                            TodoActivity.this.finish();
                                        }

                                    });

                                    //negative button.
                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }

                                    });
                                    builder.show();
                                    break;

                                case update_item:
                                    isCardForUpdate = true;
                                    Intent intent = new Intent(TodoActivity.this, CreateNoteTODOActivity.class);
                                    intent.putExtra(KEY_UPDATE_TODO, todoDTO);
                                    startActivity(intent);
                                    finish();
                                    break;
                            }
                            return true;
                        }
                    });
                    cardPopupMenu.show();
                }
            });
        }
    }


    private void updateTodoCheckboxUIandDB(TodoDTO item) {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues newValues = new ContentValues();
        newValues.put(TABLE_NOTES_TODO_KEY_ISDONE, item.getIsDone());

        sqLiteDatabase.update(DBHelper.TABLE_NOTES_TODO_NAME, newValues, TABLE_NOTES_TODO_KEY_TITLE + " = ? ", new String[]{item.getTitle()});

        //закрываем соединение с БД.
        dbHelper.close();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
