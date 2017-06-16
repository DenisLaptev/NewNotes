package ua.a5.newnotes.activities.notes_activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.a5.newnotes.DAO.DBHelper;
import ua.a5.newnotes.R;
import ua.a5.newnotes.activities.CreateNoteBirthdaysActivity;
import ua.a5.newnotes.dto.notesDTO.BirthdayDTO;

import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_BIRTHDAYS_KEY_DAY;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_BIRTHDAYS_KEY_MONTH;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_BIRTHDAYS_KEY_NAME;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_BIRTHDAYS_KEY_YEAR;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_BIRTHDAYS_NAME;
import static ua.a5.newnotes.R.id.delete_item;
import static ua.a5.newnotes.R.id.update_item;
import static ua.a5.newnotes.utils.Constants.KEY_BIRTHDAY_DTO;
import static ua.a5.newnotes.utils.Constants.KEY_UPDATE_BIRTHDAYS;
import static ua.a5.newnotes.utils.Constants.isCardForUpdate;

public class BirthdayActivity extends AppCompatActivity {

    @BindView(R.id.tv_birthdays_activity_title)
    TextView tvTitle;

    @BindView(R.id.tv_birthdays_activity_date)
    TextView tvDate;

    @BindView(R.id.iv_birthday_menu)
    ImageView ivBirthdayMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthdays);

        ButterKnife.bind(this);

        if (getIntent() != null) {
            final BirthdayDTO birthdayDTO = (BirthdayDTO) getIntent().getSerializableExtra(KEY_BIRTHDAY_DTO);
            tvTitle.setText(birthdayDTO.getName());
            tvDate.setText(birthdayDTO.getDay() + " " + birthdayDTO.getStringMonth());


            ivBirthdayMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //deleteItem(position, todoDTOList);
                    PopupMenu cardPopupMenu = new PopupMenu(BirthdayActivity.this, ivBirthdayMenu);
                    cardPopupMenu.getMenuInflater().inflate(R.menu.menu_card, cardPopupMenu.getMenu());

                    cardPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem it) {

                            switch (it.getItemId()) {
                                case delete_item:
                                    Toast.makeText(BirthdayActivity.this, "delete", Toast.LENGTH_SHORT).show();
                                    deleteItemFromTable(birthdayDTO);
                                    BirthdayActivity.this.finish();
                                    break;

                                case update_item:
                                    Toast.makeText(BirthdayActivity.this, "update", Toast.LENGTH_SHORT).show();


                                    isCardForUpdate = true;
                                    Intent intent = new Intent(BirthdayActivity.this, CreateNoteBirthdaysActivity.class);
                                    intent.putExtra(KEY_UPDATE_BIRTHDAYS, birthdayDTO);
                                    startActivity(intent);
                                    Toast.makeText(BirthdayActivity.this, it.getTitle(), Toast.LENGTH_SHORT).show();
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


    private void deleteItemFromTable(BirthdayDTO birthdayDTO) {

        //////////////////---------------------->
        //для работы с БД.
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_NOTES_BIRTHDAYS_NAME,
                TABLE_NOTES_BIRTHDAYS_KEY_NAME + " = ? AND "
                        + TABLE_NOTES_BIRTHDAYS_KEY_DAY + " = ? AND "
                        + TABLE_NOTES_BIRTHDAYS_KEY_MONTH + " = ? AND "
                        + TABLE_NOTES_BIRTHDAYS_KEY_YEAR + " = ? ",
                new String[]{
                        birthdayDTO.getName(),
                        String.valueOf(birthdayDTO.getDay()),
                        String.valueOf(birthdayDTO.getMonth()),
                        String.valueOf(birthdayDTO.getYear())
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
