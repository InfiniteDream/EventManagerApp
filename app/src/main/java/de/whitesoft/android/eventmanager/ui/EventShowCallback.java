package de.whitesoft.android.eventmanager.ui;

import de.whitesoft.android.eventmanager.model.Event;

/**
 * Created by akyolo on 06.01.2015.
 */
public interface EventShowCallback {

    /**
     * Callback for when an item has been selected.
     */
    public void onItemSelected(Event model);

}
