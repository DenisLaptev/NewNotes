package ua.a5.newnotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.GregorianCalendar;

import ua.a5.newnotes.R;
import ua.a5.newnotes.adapter.tabsFragmentAdapters.EventsTabsFragmentAdapter;

import static ua.a5.newnotes.utils.Constants.MAP_INDEX_ALL_EVENTS;
import static ua.a5.newnotes.utils.Constants.MAP_INDEX_THIS_MONTH;
import static ua.a5.newnotes.utils.Constants.MAP_INDEX_TODAY;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentDay;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentMonth;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentYear;

public class EventsActivity extends AppCompatActivity {
    private static final int LAYOUT = R.layout.activity_events;

    private Toolbar toolbarEvents;
    private DrawerLayout drawerLayoutEvents;
    private ViewPager viewPagerEvents;
    private TabLayout tabLayoutEvents;

    EventsTabsFragmentAdapter adapterEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initToolbar();
        initNavigationView();
        initTabs();
    }

    private void initToolbar() {
        toolbarEvents = (Toolbar) findViewById(R.id.toolbar_events);
        toolbarEvents.setTitle(R.string.toolbar_title);
        toolbarEvents.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.btn_calendar_events:

                        //The intent to create a calendar event.
                        Intent calIntent = new Intent(Intent.ACTION_INSERT);
                        calIntent.setType("vnd.android.cursor.item/event");

                        try {
                            GregorianCalendar calDateBegin = new GregorianCalendar(getCurrentYear(), getCurrentMonth(), getCurrentDay());
                            calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                    calDateBegin.getTimeInMillis());

                            startActivity(calIntent);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return false;
            }
        });

        toolbarEvents.inflateMenu(R.menu.menu_events);
    }


    private void initNavigationView() {
        drawerLayoutEvents = (DrawerLayout) findViewById(R.id.drawer_layout_events);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this,
                        drawerLayoutEvents,
                        toolbarEvents,
                        R.string.view_navigation_open,
                        R.string.view_navigation_close
                );
        drawerLayoutEvents.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationViewEvents = (NavigationView) findViewById(R.id.navigation_view_events);
        navigationViewEvents.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayoutEvents.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.todayItem:
                        viewPagerEvents.setCurrentItem(MAP_INDEX_TODAY);
                        break;

                    case R.id.thisMonthItem:
                        viewPagerEvents.setCurrentItem(MAP_INDEX_THIS_MONTH);
                        break;

                    case R.id.allEventsItem:
                        viewPagerEvents.setCurrentItem(MAP_INDEX_ALL_EVENTS);
                        break;

                    case R.id.mainmenuItem:
                        onBackPressed();
                        finish();
                        break;
                }
                return true;
            }
        });
    }


    private void initTabs() {
        viewPagerEvents = (ViewPager) findViewById(R.id.viewpager_events);
        adapterEvents = new EventsTabsFragmentAdapter(this, getSupportFragmentManager());
        viewPagerEvents.setAdapter(adapterEvents);

        tabLayoutEvents = (TabLayout) findViewById(R.id.tablayout_events);
        tabLayoutEvents.setupWithViewPager(viewPagerEvents);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
