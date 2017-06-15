package ua.a5.newnotes.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;

import ua.a5.newnotes.R;

public class StartMenuActivity extends AppCompatActivity {

    Button btnStartMenuNotes;
    Button btnStartMenuEvents;
    Button btnStartMenuQuit;

    //Переменная для управления выходом кнопкой Back.
    private int quitIntFlag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        btnStartMenuNotes = (Button) findViewById(R.id.btnStartMenuNotes);
        btnStartMenuNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartMenuActivity.this, NotesActivity.class);
                startActivity(intent);
            }
        });

        btnStartMenuEvents = (Button) findViewById(R.id.btnStartMenuEvents);
        btnStartMenuEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartMenuActivity.this, EventsActivity.class);
                startActivity(intent);
            }
        });

        btnStartMenuQuit = (Button) findViewById(R.id.btnStartMenuQuit);
        btnStartMenuQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StartMenuActivity.this, R.style.MyAlertDialogStyle);
                builder.setTitle("Quit?");
                builder.setMessage("Do You Really Want To Quit?");

                //positive button.
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                });

                //negative button.
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                });
                builder.show();
            }
        });


//Date and Time
        Calendar cal = Calendar.getInstance();
        int millisecond = cal.get(Calendar.MILLISECOND);
        int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        //12 hour format
        int hour = cal.get(Calendar.HOUR);
        //24 hour format
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);
        System.out.println("Time: " + hourofday + ":" + minute + ":" + second);


        cal = Calendar.getInstance();
        int dayofyear = cal.get(Calendar.DAY_OF_YEAR);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);
        System.out.println("Date: (" + dayofweek + ") " + dayofmonth + "-" + (++month) + "-" + year);
//Date and Time
    }

    @Override
    public void onResume() {
        super.onResume();
        quitIntFlag = 0;
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.start_menu_relative_layout);
        rl.setBackgroundColor(getResources().getColor(R.color.colorBackgroundWhite));
    }

    @Override
    public void onBackPressed() {
        if (quitIntFlag == 0) {
            Toast.makeText(getApplicationContext(), "Press again for Quit",
                    Toast.LENGTH_LONG).show();
            quitIntFlag++;
        } else {
            quitIntFlag = 0;
            super.onBackPressed();
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        quitIntFlag = 0;
    }
}
