package ua.a5.newnotes.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by A5 Android Intern 2 on 03.05.2017.
 */

public abstract class AbstractTabFragment extends Fragment {

    protected Context context;
    protected View view;
    private String title;

    public void setContext(Context context) {
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
