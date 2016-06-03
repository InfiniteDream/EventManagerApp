package de.whitesoft.android.eventmanager;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import java.util.List;

import de.whitesoft.android.eventmanager.importer.InternalStorageHandler;
import de.whitesoft.android.eventmanager.main.AccountFragment;
import de.whitesoft.android.eventmanager.main.SettingsFragment;
import de.whitesoft.android.eventmanager.model.Event;
import de.whitesoft.android.eventmanager.model.EventListFilter;
import de.whitesoft.android.eventmanager.ui.EventItemDetailActivity;
import de.whitesoft.android.eventmanager.ui.EventItemDetailFragment;
import de.whitesoft.android.eventmanager.ui.EventItemListFragment;
import de.whitesoft.android.eventmanager.ui.EventShowCallback;
import de.whitesoft.android.eventmanager.utils.ManagerUtils;
import de.whitesoft.android.eventmanager.utils.SettingUtils;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, EventShowCallback {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Prüfe ob hier nützlich ist...
        /*if (findViewById(R.id.eventitem_detail_container) != null) {
            mTwoPane = true;
            ((EventItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.eventitem_list))
                    .setActivateOnItemClick(true);
        }*/

        // TODO: If exposing deep links into your app, handle intents here.
        ManagerUtils.initBackgroundServices(getApplicationContext());

        //
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

       Fragment fragment;
        if(position == 0){
            fragment = new EventItemListFragment();
        }else if (position == 1) {
            fragment = AccountFragment.newInstance();
        } else if (position == 2) {
            fragment = SettingsFragment.newInstance();
        }else{
            fragment = new EventItemListFragment();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.main_menu_title_event_list);
                break;
            case 1:
                mTitle = getString(R.string.main_menu_title_account);
                break;
            case 2:
                mTitle = getString(R.string.main_menu_title_settings);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
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
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if(id == R.id.action_sync){
            // TODO anpassen???
            // UpDate Service und Refresh Screens
            ManagerUtils.startUpdateCheck(getApplicationContext());

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Event event) {
        Log.v("EventItemListActivity", " Selected Event-Id:");


            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, EventItemDetailActivity.class);
            detailIntent.putExtra(EventItemDetailFragment.ITEM_EVENT_ID, event.getId());
            detailIntent.putExtra(EventItemDetailFragment.ITEM_MANAGER_ID, event.getManagerId());
            startActivity(detailIntent);

        // TODO: Prüfen ob hier nützlioch ist
        /*if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(EventItemDetailFragment.ITEM_EVENT_ID, event.getId());
            arguments.putInt(EventItemDetailFragment.ITEM_MANAGER_ID, event.getManagerId());
            arguments.putLong(EventItemDetailFragment.ITEM_EVENT_DATE, event.getDate());
            EventItemDetailFragment fragment = new EventItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.eventitem_detail_container, fragment)
                    .commit();

        }*/
    }

    private void onEventListUpdated() {
        Log.i("BroadcastReceiver"," - onEventListUpdated -- ");
        EventListFilter filter = SettingUtils.getEventFilterFromSettings(getApplicationContext());
        List<Event> events = InternalStorageHandler.filterEvents(getApplicationContext(), filter);
        if(events != null && events.size() > 0) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
            if(fragment instanceof EventItemListFragment && fragment.isVisible()) {
                ((EventItemListFragment) fragment).updateEvents(events);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ManagerUtils.ACTION_NAME_UPDATE_EVENT_LIST));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("BroadcastReceiver"," - receiver -- ");
            if(intent != null) {
                boolean callback  = intent.getBooleanExtra("callback", false);
                if(callback) {
                    onEventListUpdated();
                }else{
                    // TODO Message wenn keine Daten updated ???
                }
            }
        }
    };

/*    *//**
     * A placeholder fragment containing a simple view.
     *//*
    public static class PlaceholderFragment extends Fragment {
        *//**
         * The fragment argument representing the section number for this
         * fragment.
         *//*
        private static final String ARG_SECTION_NUMBER = "section_number";

        *//**
         * Returns a new instance of this fragment for the given section
         * number.
         *//*
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }*/

}
