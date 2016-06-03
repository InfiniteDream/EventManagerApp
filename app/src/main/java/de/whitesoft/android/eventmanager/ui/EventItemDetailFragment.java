package de.whitesoft.android.eventmanager.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.whitesoft.android.eventmanager.R;

import de.whitesoft.android.eventmanager.importer.InternalStorageHandler;
import de.whitesoft.android.eventmanager.model.Event;
import de.whitesoft.android.eventmanager.utils.ManagerUtils;

/**
 * A fragment representing a single EventItem detail screen.
 * This fragment is either contained in a {@link de.whitesoft.android.eventmanager.MainActivity}
 * in two-pane mode (on tablets) or a {@link EventItemDetailActivity}
 * on handsets.
 */
public class EventItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ITEM_EVENT_ID = "item_event_id";

    public static final String ITEM_MANAGER_ID = "item_manager_id";

    public static final String ITEM_EVENT_DATE = "item_event_date";

    /**
     * The dummy content this fragment is presenting.
     */
    private Event event;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ITEM_MANAGER_ID) && getArguments().containsKey(ITEM_EVENT_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String managerId = getArguments().getString(ITEM_MANAGER_ID);
            String eventId = getArguments().getString(ITEM_EVENT_ID);
            // TODO Event suchen
            event = InternalStorageHandler.getEventFromCache(getActivity(),managerId, eventId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_eventitem_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (event != null) {

            // TODO   Set Daten aus Model
            TextView title = (TextView) rootView.findViewById(R.id.eventTitle);
            title.setText(event.getTitle());
            TextView desc = (TextView) rootView.findViewById(R.id.eventDescription);
            desc.setText(" Date : " + ManagerUtils.convertDate(event.getDate()));

        }

        return rootView;
    }
}
