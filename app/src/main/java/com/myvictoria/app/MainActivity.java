package com.myvictoria.app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;


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
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        mTitle = getResources().getStringArray(R.array.section_titles)[position];
        String website = getResources().getStringArray(R.array.websites)[position];
        WifiManager wifiManager = (WifiManager) getSystemService (Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if(!hasInternet() && info.getSSID().contains("victoria")){
            ft.replace(R.id.container, InternetFragment.newInstance("https://wireless.victoria.ac.nz/fs/customwebauth/login.html"), "MAIN_FRAGMENT");
        } else if(!website.isEmpty()) {
            ft.replace(R.id.container, InternetFragment.newInstance(website), "MAIN_FRAGMENT");
        } else {
            switch (position) {
                case 1:
                    ft.replace(R.id.container, new MapFragment());
                    break;
                case 7:
                    ft.replace(R.id.container, new LectureFragment());
                    break;
                case 9:
                    ft.replace(R.id.container, new SettingsFragment());
                    break;
            }
        }
        ft.commit();
    }

    private boolean hasInternet() {
        boolean haveConnectedWifi = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        cm.setNetworkPreference(ConnectivityManager.TYPE_WIFI);
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                haveConnectedWifi = false;
                NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
                if (ni.isConnected() && activeNetworkInfo != null) {
                    haveConnectedWifi = true;
                }
            }
            if(ni.getTypeName().equalsIgnoreCase("MOBILE")){
                haveConnectedWifi = true;
            }
        }
        return haveConnectedWifi;
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
