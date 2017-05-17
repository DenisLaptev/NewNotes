package ua.a5.newnotes.activities.notes_activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.a5.newnotes.R;
import ua.a5.newnotes.dto.notesDTO.IdeaDTO;

import static ua.a5.newnotes.fragments.notes_fragments.IdeasFragment.KEY_IDEA_DTO;

public class IdeaActivity extends AppCompatActivity {

    @BindView(R.id.tv_ideas_activity_title)
    TextView tvTitle;

    @BindView(R.id.tv_ideas_activity_date)
    TextView tvDate;

    @BindView(R.id.tv_ideas_activity_description)
    TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ideas);

        ButterKnife.bind(this);

        if (getIntent() != null) {
            IdeaDTO ideaDTO = (IdeaDTO) getIntent().getSerializableExtra(KEY_IDEA_DTO);
            tvTitle.setText(ideaDTO.getTitle());
            tvDate.setText(ideaDTO.getDate());
            tvDescription.setText(ideaDTO.getDescription());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
