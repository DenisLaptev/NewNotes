package ua.a5.newnotes.activities.notes_activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.a5.newnotes.R;
import ua.a5.newnotes.dto.notesDTO.DifferentDTO;

import static ua.a5.newnotes.fragments.notes_fragments.DifferentFragment.KEY_DIFFERENT_DTO;

public class DifferentActivity extends AppCompatActivity {

    @BindView(R.id.tv_different_activity_title)
    TextView tvTitle;

    @BindView(R.id.tv_different_activity_date)
    TextView tvDate;

    @BindView(R.id.tv_different_activity_description)
    TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_different);

        ButterKnife.bind(this);

        if (getIntent() != null) {
            DifferentDTO differentDTO = (DifferentDTO) getIntent().getSerializableExtra(KEY_DIFFERENT_DTO);
            tvTitle.setText(differentDTO.getTitle());
            tvDate.setText(differentDTO.getDate());
            tvDescription.setText(differentDTO.getDescription());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
