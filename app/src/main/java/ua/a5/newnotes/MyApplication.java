package ua.a5.newnotes;

import android.app.Application;
import android.content.Context;

/**
 * Created by A5 Android Intern 2 on 30.06.2017.
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}