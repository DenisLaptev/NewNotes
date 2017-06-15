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

import java.util.GregorianCalendar;

import ua.a5.newnotes.R;
import ua.a5.newnotes.adapter.tabsFragmentAdapters.NotesTabsFragmentAdapter;

import static ua.a5.newnotes.R.id.toolbar_notes;
import static ua.a5.newnotes.utils.Constants.MAP_INDEX_BIRTHDAYS;
import static ua.a5.newnotes.utils.Constants.MAP_INDEX_DIFFERENT;
import static ua.a5.newnotes.utils.Constants.MAP_INDEX_IDEAS;
import static ua.a5.newnotes.utils.Constants.MAP_INDEX_TODO;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentDay;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentMonth;
import static ua.a5.newnotes.utils.utils_spannable_string.UtilsDates.getCurrentYear;

public class NotesActivity extends AppCompatActivity {
    private static final int LAYOUT = R.layout.activity_notes;

    private Toolbar toolbarNotes;
    private DrawerLayout drawerLayoutNotes;
    private ViewPager viewPagerNotes;
    private TabLayout tabLayoutNotes;

    NotesTabsFragmentAdapter adapterNotes;

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
        toolbarNotes = (Toolbar) findViewById(toolbar_notes);
        toolbarNotes.setTitle(R.string.toolbar_title);
        toolbarNotes.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.btn_calendar_notes:

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

        toolbarNotes.inflateMenu(R.menu.menu_notes);
    }


    private void initNavigationView() {
        drawerLayoutNotes = (DrawerLayout) findViewById(R.id.drawer_layout_notes);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this,
                        drawerLayoutNotes,
                        toolbarNotes,
                        R.string.view_navigation_open,
                        R.string.view_navigation_close
                );
        drawerLayoutNotes.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationViewNotes = (NavigationView) findViewById(R.id.navigation_view_notes);
        navigationViewNotes.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayoutNotes.closeDrawers();
                switch (menuItem.getItemId()) {

                    case R.id.todoItem:
                        viewPagerNotes.setCurrentItem(MAP_INDEX_TODO);
                        break;

                    case R.id.ideasItem:
                        viewPagerNotes.setCurrentItem(MAP_INDEX_IDEAS);
                        break;

                    case R.id.birthdaysItem:
                        viewPagerNotes.setCurrentItem(MAP_INDEX_BIRTHDAYS);
                        break;

                    case R.id.differentItem:
                        viewPagerNotes.setCurrentItem(MAP_INDEX_DIFFERENT);
                        break;

                    case R.id.mainmenuItem:
                        onBackPressed();
                        break;
                }
                return true;
            }
        });
    }


    private void initTabs() {
        viewPagerNotes = (ViewPager) findViewById(R.id.viewpager_notes);
        adapterNotes = new NotesTabsFragmentAdapter(this, getSupportFragmentManager());
        viewPagerNotes.setAdapter(adapterNotes);

        tabLayoutNotes = (TabLayout) findViewById(R.id.tablayout_notes);
        tabLayoutNotes.setupWithViewPager(viewPagerNotes);
    }
   


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
