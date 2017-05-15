package ua.a5.newnotes.adapter.tabsFragmentAdapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import ua.a5.newnotes.fragments.AbstractTabFragment;
import ua.a5.newnotes.fragments.eventsFragments.ThisMonthFragment;
import ua.a5.newnotes.fragments.eventsFragments.ThisWeekFragment;
import ua.a5.newnotes.fragments.eventsFragments.TodayFragment;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class EventsTabsFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> eventsTabs;

    public EventsTabsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        initEventsTabsMap(context);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return eventsTabs.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return eventsTabs.get(position);
    }

    @Override
    public int getCount() {
        return eventsTabs.size();
    }

    private void initEventsTabsMap(Context context) {
        eventsTabs = new HashMap<>();
        eventsTabs.put(0, TodayFragment.getInstance(context));
        eventsTabs.put(1, ThisWeekFragment.getInstance(context));
        eventsTabs.put(2, ThisMonthFragment.getInstance(context));
    }
}
