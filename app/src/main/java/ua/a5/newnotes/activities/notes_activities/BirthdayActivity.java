package ua.a5.newnotes.activities.notes_activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.a5.newnotes.R;
import ua.a5.newnotes.dto.notesDTO.BirthdayDTO;

import static ua.a5.newnotes.fragments.notes_fragments.BirthdaysFragment.KEY_BIRTHDAY_DTO;

public class BirthdayActivity extends AppCompatActivity {

    @BindView(R.id.tv_birthdays_activity_title)
    TextView tvTitle;

    @BindView(R.id.tv_birthdays_activity_date)
    TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthdays);

        ButterKnife.bind(this);

        if (getIntent() != null) {
            BirthdayDTO birthdayDTO = (BirthdayDTO) getIntent().getSerializableExtra(KEY_BIRTHDAY_DTO);
            tvTitle.setText(birthdayDTO.getName());
            tvDate.setText(birthdayDTO.getDate());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
