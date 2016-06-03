package de.whitesoft.android.eventmanager.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import de.whitesoft.android.eventmanager.MainActivity;
import de.whitesoft.android.eventmanager.importer.InternalStorageHandler;
import de.whitesoft.android.eventmanager.model.Event;
import de.whitesoft.android.eventmanager.model.EventListFilter;
import de.whitesoft.android.eventmanager.utils.SettingUtils;

/**
 * A list fragment representing a list of EventItems. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link EventItemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link de.whitesoft.android.eventmanager.ui.EventShowCallback}
 * interface.
 */
public class EventItemListFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private EventShowCallback mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private EventListViewAdapter mAdapter;

    /**
     * A dummy implementation of the {@link de.whitesoft.android.eventmanager.ui.EventShowCallback} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static EventShowCallback sDummyCallbacks = new EventShowCallback() {
        @Override
        public void onItemSelected(Event event) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ermittle Eventdaten
        // TODO Filter aus Prefercen nehmen...
        EventListFilter filter = SettingUtils.getEventFilterFromSettings(getActivity());
        List<Event> events = InternalStorageHandler.filterEvents(getActivity(), filter);
        // Aktualisiere Ansicht
        updateEvents(events);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(0);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof EventShowCallback)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (EventShowCallback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        // TODO
        Log.v(" onItemClick ", "-- position:" + position + ", id:" + id + ", viewId:" + view.getId());
        Event event = mAdapter.getItem(position);

        Log.v(" onItemClick ", " EVENT: " + event.toString());
        mCallbacks.onItemSelected(event);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    public void updateEvents(List<Event> events) {
        mAdapter = new EventListViewAdapter(getActivity(), events);
        setListAdapter(mAdapter);
        mAdapter.updateView(events);
    }
}
