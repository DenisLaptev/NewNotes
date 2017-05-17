package ua.a5.newnotes.activities.notes_activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.a5.newnotes.R;
import ua.a5.newnotes.dto.notesDTO.TodoDTO;

import static ua.a5.newnotes.fragments.notes_fragments.TodoFragment.KEY_TODO_DTO;

public class TodoActivity extends AppCompatActivity {

    @BindView(R.id.tv_todo_activity_title)
    TextView tvTitle;

    @BindView(R.id.tv_todo_activity_date)
    TextView tvDate;

    @BindView(R.id.tv_todo_activity_description)
    TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        ButterKnife.bind(this);

        if (getIntent() != null) {
            TodoDTO todoDTO = (TodoDTO) getIntent().getSerializableExtra(KEY_TODO_DTO);
            tvTitle.setText(todoDTO.getTitle());
            tvDate.setText(todoDTO.getDate());
            tvDescription.setText(todoDTO.getDescription());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
