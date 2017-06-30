package ua.a5.newnotes.activities.notes_activities;

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
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.a5.newnotes.DAO.DBHelper;
import ua.a5.newnotes.R;
import ua.a5.newnotes.activities.CreateNoteIdeasActivity;
import ua.a5.newnotes.dto.notesDTO.IdeaDTO;

import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_IDEAS_KEY_DATE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_IDEAS_KEY_DESCRIPTION;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_IDEAS_KEY_TITLE;
import static ua.a5.newnotes.DAO.DBHelper.TABLE_NOTES_IDEAS_NAME;
import static ua.a5.newnotes.R.id.delete_item;
import static ua.a5.newnotes.R.id.update_item;
import static ua.a5.newnotes.utils.Constants.KEY_IDEA_DTO;
import static ua.a5.newnotes.utils.Constants.KEY_UPDATE_IDEAS;
import static ua.a5.newnotes.utils.Constants.isCardForUpdate;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.DATE_REGEXPS;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.DAYS_OF_THE_WEEK;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.TIME_WORDS;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsMethods.convertDateRegExps;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsMethods.convertString;

public class IdeaActivity extends AppCompatActivity {

    public static SpannableString bufferSpannableString = null;

    String initialWord;
    String strRegExp;

    @BindView(R.id.tv_ideas_activity_title)
    TextView tvTitle;

    @BindView(R.id.tv_ideas_activity_date)
    TextView tvDate;

    @BindView(R.id.tv_ideas_activity_description)
    TextView tvDescription;

    @BindView(R.id.iv_idea_menu)
    ImageView ivIdeaMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ideas);

        ButterKnife.bind(this);

        if (getIntent() != null) {
            final IdeaDTO ideaDTO = (IdeaDTO) getIntent().getSerializableExtra(KEY_IDEA_DTO);
            tvTitle.setText(ideaDTO.getTitle());
            tvDate.setText(ideaDTO.getDate());

            try {
                SpannableString spannableString = new SpannableString(ideaDTO.getDescription());
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


            ivIdeaMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu cardPopupMenu = new PopupMenu(IdeaActivity.this, ivIdeaMenu);
                    cardPopupMenu.getMenuInflater().inflate(R.menu.menu_card, cardPopupMenu.getMenu());

                    cardPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem it) {

                            switch (it.getItemId()) {
                                case delete_item:

                                    AlertDialog.Builder builder = new AlertDialog.Builder(IdeaActivity.this, R.style.MyAlertDialogStyle);
                                    builder.setTitle("Delete?");
                                    builder.setMessage("Do You Really Want To Delete?");

                                    //positive button.
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            deleteItemFromTable(ideaDTO);
                                            IdeaActivity.this.finish();
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
                                    Intent intent = new Intent(IdeaActivity.this, CreateNoteIdeasActivity.class);
                                    intent.putExtra(KEY_UPDATE_IDEAS, ideaDTO);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
