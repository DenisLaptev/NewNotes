package ua.a5.newnotes;

import android.content.Intent;
import android.os.Bundle;
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

public class NotesActivity extends AppCompatActivity {
    private static final int LAYOUT = R.layout.activity_notes;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    //TabsFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initToolbar();
        initNavigationView();
        //initTabs();
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_title);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.search:
                        //showNotificationTab();
                        Toast.makeText(getApplicationContext(), "search", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(NotesActivity.this, CreateNoteActivity.class);
                        startActivity(intent);
                        break;
                }

                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu_notes);
    }


    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this,
                        drawerLayout,
                        toolbar,
                        R.string.view_navigation_open,
                        R.string.view_navigation_close
                );
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.actionNotificationItem:
                        //showNotificationTab();
                        Toast.makeText(getApplicationContext(), "Notification", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.todoItem:
                        //showNotificationTab();
                        Toast.makeText(getApplicationContext(), "TODO", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.ideasItem:
                        //showNotificationTab();
                        Toast.makeText(getApplicationContext(), "Ideas", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.birthdaysItem:
                        //showNotificationTab();
                        Toast.makeText(getApplicationContext(), "Birthdays", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.differentItem:
                        //showNotificationTab();
                        Toast.makeText(getApplicationContext(), "Different", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.mainmenuItem:
                        //showNotificationTab();
                        Toast.makeText(getApplicationContext(), "Main menu_notes", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(NotesActivity.this, StartMenuActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });

    }

/*
    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new TabsFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        new RemindMeTask().execute();

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }
   */

/*

    private void showNotificationTab() {
        viewPager.setCurrentItem(Constants.TAB_TWO);
    }

*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
