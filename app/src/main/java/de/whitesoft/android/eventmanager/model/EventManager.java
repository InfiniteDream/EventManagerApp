package de.whitesoft.android.eventmanager.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by akyolo on 31.12.2014.
 */
public class EventManager implements Serializable{
    // eventManagerId, eventType, eventId, eventDate, eventLocation
    private String id;
    private String type;
    private String location;

    private boolean selected;

    private Map<String, Event> eventsMap;

    public EventManager(){
        eventsMap = new HashMap<String, Event>() ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Event> getEventsMap() {
        return eventsMap;
    }

    @Override
    public String toString() {
        return "EventManager{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", eventsMap=" + eventsMap +
         '}';
    }

    public void addEvent(Event event) {
        if(eventsMap == null){
            eventsMap = new HashMap<String, Event>();
        }
        eventsMap.put(event.getId(), event);
    }

    public Event getEvent(String eventId){
        Event event = null;
        if(this.eventsMap != null) {
            event = this.eventsMap.get(eventId);
        }
        return event;
    }

    @Deprecated
    private Event getEvent(int eventId, long eventDate){
        Event event = null;
        if(this.eventsMap != null) {
            event = this.eventsMap.get(eventId + "_" + eventDate);
        }
        return event;
     }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setEvent(Event event){
        this.eventsMap.put(event.getId(), event);
    }

    public List<Event> getEvents(){
        List<Event> returnListe = new ArrayList<Event>();
        Map events =  this.getEventsMap();
        if(events != null && events.size() > 0) {
            Iterator it = events.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                Event event = (Event) events.get(key);
                returnListe.add(event);
            }
        }
        Collections.sort(returnListe);
       return returnListe;
    }
}
