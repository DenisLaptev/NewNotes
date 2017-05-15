package ua.a5.newnotes.adapter.tabsFragmentAdapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import ua.a5.newnotes.fragments.AbstractTabFragment;
import ua.a5.newnotes.fragments.notesFragments.BirthdaysFragment;
import ua.a5.newnotes.fragments.notesFragments.DifferentFragment;
import ua.a5.newnotes.fragments.notesFragments.IdeasFragment;
import ua.a5.newnotes.fragments.notesFragments.TodoFragment;

/**
 * Created by A5 Android Intern 2 on 12.05.2017.
 */

public class NotesTabsFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> notesTabs;

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
        notesTabs = new HashMap<>();
        notesTabs.put(0, TodoFragment.getInstance(context));
        notesTabs.put(1, IdeasFragment.getInstance(context));
        notesTabs.put(2, BirthdaysFragment.getInstance(context));
        notesTabs.put(3, DifferentFragment.getInstance(context));
    }
}