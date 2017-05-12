package ua.a5.newnotes;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadLogoActivity extends AppCompatActivity {
    //таймер отсчитывает, сколько времени на заставке будет показываться логотип компании.
    LoadLogoTimer loadLogoTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_load_logo);

        loadLogoTimer = new LoadLogoTimer(2000, 1000);
        loadLogoTimer.start();
    }


    //вложенный класс для таймера. Таймер отсчитывает время, которое показывается заставка.
    public class LoadLogoTimer extends CountDownTimer {
        public LoadLogoTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
        }

        @Override
        public void onFinish() {
            //по окончании таймера заускаем активити LoadGameActivity.
            Intent intent = new Intent(LoadLogoActivity.this, StartMenuActivity.class);
            startActivity(intent);
            finish();
        }
    }
}