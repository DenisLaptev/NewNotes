package ua.a5.newnotes.adapter.tabsFragmentAdapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ua.a5.newnotes.fragments.events_fragments.AllEventsFragment;
import ua.a5.newnotes.fragments.events_fragments.ThisMonthFragment;
import ua.a5.newnotes.fragments.events_fragments.TodayFragment;

/**
 * Created by Lenovo on 21.06.2017.
 */

public class MyEventsFragmentPagerAdapter extends FragmentPagerAdapter {

    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;
    private Context mContext;


    public MyEventsFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mFragmentManager = fm;
        mFragmentTags = new HashMap<Integer, String>();
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new TodayFragment();

            case 1:
                return new ThisMonthFragment();

            case 2:
                return new AllEventsFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if(obj instanceof Fragment){
            //record fragment tag here.
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position,tag);
        }
        return obj;
    }


    public Fragment getFragment(int position){
        String tag = mFragmentTags.get(position);
        if(tag == null){
            return null;
        }
        return mFragmentManager.findFragmentByTag(tag);
    }
}
