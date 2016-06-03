package de.whitesoft.android.eventmanager.importer;

import android.content.Context;

import de.whitesoft.android.eventmanager.model.Event;
import de.whitesoft.android.eventmanager.model.EventListFilter;
import de.whitesoft.android.eventmanager.model.EventModel;

/**
 * Created by akyolo on 08.01.2015.
 */
public interface StorageHandler {

    public EventModel queryEventList(EventListFilter filter);

    public void updateEventModel(EventListFilter filter, String jsonData);

    //public Event getEventFromCache(Context context, int managerId, int eventId, long eventDate);
}
