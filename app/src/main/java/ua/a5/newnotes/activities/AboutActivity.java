package ua.a5.newnotes.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import ua.a5.newnotes.R;

public class AboutActivity extends AppCompatActivity {

    Button btnAboutBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        btnAboutBack = (Button) findViewById(R.id.btnAboutBack);
        btnAboutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    //set white background to the Activity.
    @Override
    public void onResume() {
        super.onResume();
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.about_relative_layout);
        rl.setBackgroundColor(getResources().getColor(R.color.colorBackgroundWhite));
    }
}
