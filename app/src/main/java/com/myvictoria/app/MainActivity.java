package com.myvictoria.app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    FragmentManager fragmentManager;
    SharedPreferences getData;
    static String name;
    static String pass;
    ShowcaseView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData = PreferenceManager.getDefaultSharedPreferences(this);
        name = getData.getString("username", "No Username Set");
        pass = getData.getString("password", "No Password Set");
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        boolean isFirstRun = getData.getBoolean("FIRSTRUN", true);
        if (isFirstRun) {
            ActionViewTarget av = new ActionViewTarget(this, ActionViewTarget.Type.TITLE);
            sv = new ShowcaseView.Builder(this)
                    .setContentText("Tap this to get to the menu of modules")
                    .setContentTitle("MyVictoria Home")
                    .hideOnTouchOutside()
                    .setTarget(av)
                    .setStyle(R.style.CustomShowcaseTheme)
                    .build();
            sv.setButtonText("OK Got it!");
            getData.edit()
                    .putBoolean("FIRSTRUN", false)
                    .apply();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment webview = getFragmentManager().findFragmentById(R.id.container);
        if (webview instanceof InternetFragment) {
            if (((InternetFragment) webview).close()) {
                finish();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        fragmentManager = getFragmentManager();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        name = prefs.getString("username", "Username here");
        pass = prefs.getString("password", "Password here");
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        switch (position) {
            case 0:
                mTitle = getString(R.string.title_section1);
                ft.replace(R.id.container, InternetFragment.newInstanceLogin("https://my.vuw.ac.nz/cp/home/displaylogin", name, pass));
                ft.commit();
                break;
            case 1:
                mTitle = getString(R.string.title_section5);
                ft.replace(R.id.container, new MapFragment());
                ft.commit();
                break;
            case 2:
                mTitle = getString(R.string.title_section6);
                ft.replace(R.id.container, InternetFragment.newInstanceLogin("https://blackboard.vuw.ac.nz/webapps/portal/execute/tabs/tabAction?tab_tab_group_id=_1_1", name, pass));
                ft.commit();
                break;
            case 3:
                mTitle = getString(R.string.title_section8);
                ft.replace(R.id.container, InternetFragment.newInstance("https://www.facebook.com/groups/overheardvic/"));
                ft.commit();
                break;
            case 4:
                mTitle = getString(R.string.title_section9);
                ft.replace(R.id.container, InternetFragment.newInstanceLogin("https://signups.victoria.ac.nz/login.aspx?ReturnUrl=%2findex.aspx", name, pass));
                ft.commit();
                break;
            case 5:
                mTitle = getString(R.string.title_section7);
                ft.replace(R.id.container, new LectureFragment());
                ft.commit();
                break;
            case 6:
                mTitle = getString(R.string.title_section3);
                ft.replace(R.id.container, InternetFragment.newInstanceLogin("https://library.victoria.ac.nz/roombooking/edit_entry.php", name, pass));
                ft.commit();
                break;
            case 7:
                mTitle = getString(R.string.title_section2);
                ft.replace(R.id.container, new SettingsFragment());
                ft.commit();
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

}
