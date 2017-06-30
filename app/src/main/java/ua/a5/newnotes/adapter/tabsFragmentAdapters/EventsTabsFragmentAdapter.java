package ua.a5.newnotes.adapter.tabsFragmentAdapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import ua.a5.newnotes.fragments.AbstractTabFragment;
import ua.a5.newnotes.fragments.events_fragments.AllEventsFragment;
import ua.a5.newnotes.fragments.events_fragments.ThisMonthFragment;
import ua.a5.newnotes.fragments.events_fragments.TodayFragment;

import static ua.a5.newnotes.utils.Constants.MAP_INDEX_ALL_EVENTS;
import static ua.a5.newnotes.utils.Constants.MAP_INDEX_THIS_MONTH;
import static ua.a5.newnotes.utils.Constants.MAP_INDEX_TODAY;

/**
 * Created by A5 Android Intern 2 on 15.05.2017.
 */

public class EventsTabsFragmentAdapter extends FragmentPagerAdapter {

    public static Map<Integer, AbstractTabFragment> eventsTabs = new HashMap<>();

    public EventsTabsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        initEventsTabsMap(context);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return eventsTabs.get(position).getTitle();
    }

    @Override
    public AbstractTabFragment getItem(int position) {
        return eventsTabs.get(position);
    }

    @Override
    public int getCount() {
        return eventsTabs.size();
    }

    private void initEventsTabsMap(Context context) {
        notifyDataSetChanged();
        //eventsTabs = new HashMap<>();
        eventsTabs.put(MAP_INDEX_TODAY, TodayFragment.getInstance(context));
        eventsTabs.put(MAP_INDEX_THIS_MONTH, ThisMonthFragment.getInstance(context));
        eventsTabs.put(MAP_INDEX_ALL_EVENTS, AllEventsFragment.getInstance(context));
    }
}
