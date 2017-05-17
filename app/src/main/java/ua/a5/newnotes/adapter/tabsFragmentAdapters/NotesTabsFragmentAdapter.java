package ua.a5.newnotes.adapter.tabsFragmentAdapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import ua.a5.newnotes.fragments.AbstractTabFragment;
import ua.a5.newnotes.fragments.notes_fragments.BirthdaysFragment;
import ua.a5.newnotes.fragments.notes_fragments.DifferentFragment;
import ua.a5.newnotes.fragments.notes_fragments.IdeasFragment;
import ua.a5.newnotes.fragments.notes_fragments.TodoFragment;

import static ua.a5.newnotes.utils.Constants.MAP_INDEX_BIRTHDAYS;
import static ua.a5.newnotes.utils.Constants.MAP_INDEX_DIFFERENT;
import static ua.a5.newnotes.utils.Constants.MAP_INDEX_IDEAS;
import static ua.a5.newnotes.utils.Constants.MAP_INDEX_TODO;

/**
 * Created by A5 Android Intern 2 on 12.05.2017.
 */

public class NotesTabsFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> notesTabs = new HashMap<>();

    public NotesTabsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        initNotesTabsMap(context);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return notesTabs.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return notesTabs.get(position);
    }

    @Override
    public int getCount() {
        return notesTabs.size();
    }

    private void initNotesTabsMap(Context context) {
        //notesTabs = new HashMap<>();
        notesTabs.put(MAP_INDEX_TODO, TodoFragment.getInstance(context));
        notesTabs.put(MAP_INDEX_IDEAS, IdeasFragment.getInstance(context));
        notesTabs.put(MAP_INDEX_BIRTHDAYS, BirthdaysFragment.getInstance(context));
        notesTabs.put(MAP_INDEX_DIFFERENT, DifferentFragment.getInstance(context));
    }
}