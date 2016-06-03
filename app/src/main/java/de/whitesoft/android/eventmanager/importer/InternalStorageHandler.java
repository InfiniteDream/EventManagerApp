package de.whitesoft.android.eventmanager.importer;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.whitesoft.android.eventmanager.model.Event;
import de.whitesoft.android.eventmanager.model.EventListFilter;
import de.whitesoft.android.eventmanager.model.EventManager;
import de.whitesoft.android.eventmanager.model.EventModel;
import de.whitesoft.android.eventmanager.utils.SettingUtils;

/**
 * Created by akyolo on 08.01.2015.
 */
public class InternalStorageHandler extends AbstractStorageHandler{

    private static Map<String, EventManager> eventCache = new HashMap<>();

    public InternalStorageHandler(Context con) {
        super(con);
    }


    public EventModel queryEventList(EventListFilter filter) {

        EventModel model = null;
        String fileName = getEventManagerFileName();
        String data = readJsonData(fileName);
        model = parseJsonDataFromStorage(filter, data);

        return model;
    }



    public void saveJsonDataLocal(EventListFilter filter, String jsonData) {
        String fileName = getEventManagerFileName();
        saveJsonDataLocal(jsonData,fileName);
        saveJsonDataToCache(filter, jsonData);
     }



    public void saveJsonDataToCache(EventListFilter filter, String jsonData){
       EventModel model = parseJsonDataFromStorage(filter, jsonData);
       updateCache(model);
    }

    private EventModel parseJsonDataFromStorage(EventListFilter filter, String jsonData) {
        // super.onPostExecute(s);
        EventModel model = null;
        if(jsonData != null && jsonData.length() > 0) {
            try {
                // model
                model = new EventModel();

                JSONArray serviceListe = new JSONArray(jsonData);

                for (int s = 0; s < serviceListe.length(); s++) {

                    JSONObject json = (JSONObject) serviceListe.get(s);
                    String serviceId = json.getString("serviceId");
                    String type = json.getString("serviceName");
                    String location = json.getString("serviceDesc");
                    Log.v("UpdateDatabaseAsyncTask", "serviceId: " + serviceId + ", serviceName: " + type + ", serviceDesc: " + location);

                    if(filter.getManagerIds().contains(serviceId)) {
                        EventManager eventManager = new EventManager();
                        eventManager.setId(serviceId);
                        eventManager.setType(type);
                        eventManager.setLocation(location);

                        JSONArray mapListe = json.getJSONArray("eventList");
                        int counter = 0;
                        if (mapListe != null) {
                            for (int i = 0; i < mapListe.length(); i++) {
                                JSONObject eventObj = (JSONObject) mapListe.get(i);
                                if (eventObj != null) {
                                    String eventId = eventObj.getString("eventId");
                                    long eventDate = eventObj.getLong("eventDate");
                                    String eventText = eventObj.getString("eventText");
                                    boolean alarmiert = false;
                                    if (!eventObj.isNull("alarmiert")) {
                                        alarmiert = eventObj.getBoolean("alarmiert");
                                    }

                                    // new
                                    Event event = new Event();
                                    event.setId(eventId);
                                    event.setDate(eventDate);
                                    event.setManagerId(serviceId);
                                    event.setAlarmiert(alarmiert);
                                    event.setTitle(eventText);
                                    event.setDescription(eventText);

                                    // add
                                    eventManager.addEvent(event);
                                    counter++;
                                }
                            }

                        }
                        Log.v("UpdateDatabaseAsyncTask", " From Server Events Found:" + counter);
                        model.addEventManager(eventManager);
                    }
                }
            } catch (JSONException e) {
                Log.e("UpdateDatabaseAsyncTask", "Fehler beim Parsen der JSON Daten!", e);
            }
        }

        return model;

    }


    public static List<Event> filterEvents(Context context, EventListFilter filter){

        List<Event> events = new ArrayList<Event>();
        try {
            if(filter != null && filter.getManagerIds() != null) {
                for(String serviceId: filter.getManagerIds()) {
                    if (serviceId != null) {
                        EventManager manager = eventCache.get(serviceId);
                        if (manager == null) {
                            loadFileFromStorage(context);
                            manager = eventCache.get(serviceId);
                        }
                        if (manager != null) {

                            List<Event> rawEvents = manager.getEvents();
                            if (rawEvents != null) {
                                long startDate = filter.getStartDate();
                                long endDate = startDate + (filter.getDayOffset() * DateUtils.DAY_IN_MILLIS);
                                for (Event event : rawEvents) {
                                    if (event.getDate() >= startDate && event.getDate() < endDate) {
                                        events.add(event);
                                    }
                                }
                            }
                        }

                        // TODO wenn manage rnull ist???
                    }
                }
            }
        }catch (Exception e){
            Log.e("","",e);
        }
        return events;
    }


    private static void updateCache(EventModel model){
        // TODO was passier wenn null ist ??? Wann ist zu CLEAR ??
        if(model != null && model.getEventManager() != null){
            for(EventManager manager : model.getEventManager()){
                if(manager != null){
                    eventCache.put(manager.getId(), manager);
                }
            }
        }
    }

    public static void setEventInCache(Context context, Event event){
        if(event.getManagerId() != null && !event.getManagerId().isEmpty()) {
            EventManager manager = eventCache.get(event.getManagerId());
            if (manager != null) {
                manager.setEvent(event);
            }
            eventCache.put(event.getManagerId(), manager);
        }
    }

    public static boolean eventAlarmiert(Event event){
        boolean alarmiert = false;
        EventManager manager = eventCache.get(event.getManagerId());
        if (manager != null) {
            Event eventim = manager.getEvent(event.getId());
            if (eventim != null && eventim.isAlarmiert()) {
                alarmiert = true;
            }
        }
        return alarmiert;
    }

    public static Event getEventFromCache(Context context, String managerId, String eventId){
        Event event = null;
        if(managerId != null) {
            EventManager manager = eventCache.get(managerId);
            if (manager != null) {
                event = manager.getEvent(eventId);
            }
            if (event == null) {
                // TODO was passier wenn Event nicht im Cach ist ???
                try {
                    loadFileFromStorage(context);
                     manager = eventCache.get(managerId);
                    if (manager != null) {
                        event = manager.getEvent(eventId);
                    }
                    // TODO was ist wenn event immer noch null ist??
                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }
        return event;
    }

    private static synchronized void loadFileFromStorage(Context context){
        InternalStorageHandler storage = new InternalStorageHandler(context);
        // TODO: Filter befüllen anhand von Settings...
        EventListFilter filter = SettingUtils.getEventFilterFromSettings(context);
        // Daten query...
        EventModel model = storage.queryEventList(filter);
        updateCache(model);
    }


}
