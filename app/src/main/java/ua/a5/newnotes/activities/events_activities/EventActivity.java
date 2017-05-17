package ua.a5.newnotes.activities.events_activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.a5.newnotes.R;
import ua.a5.newnotes.dto.eventsDTO.EventDTO;

import static ua.a5.newnotes.fragments.events_fragments.TodayFragment.KEY_EVENT_DTO;

public class EventActivity extends AppCompatActivity {

    @BindView(R.id.tv_event_activity_title)
    TextView tvTitle;

    @BindView(R.id.tv_event_activity_date)
    TextView tvDate;

    @BindView(R.id.tv_event_activity_description)
    TextView tvDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ButterKnife.bind(this);

        if (getIntent() != null) {
            EventDTO eventDTO = (EventDTO) getIntent().getSerializableExtra(KEY_EVENT_DTO);
            tvTitle.setText(eventDTO.getTitle());
            tvDate.setText(eventDTO.getDate());
            tvDescription.setText(eventDTO.getDescription());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
