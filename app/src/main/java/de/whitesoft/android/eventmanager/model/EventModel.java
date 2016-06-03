package de.whitesoft.android.eventmanager.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by akyolo on 30.12.2014.
 */
public class EventModel {

    private String updateDate = "01.01.2000";


    private Map<String, EventManager> eventManagerMap;

    public EventModel(){
        eventManagerMap = new HashMap<String, EventManager>();

    }

    public EventModel(EventManager manager){
        addEventManager(manager);
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }


    public EventManager getEventManager(String id) {
        EventManager manager = null;
        String key = String.valueOf(id);
        if(eventManagerMap != null && eventManagerMap.containsKey(key)){
            manager = eventManagerMap.get(key);
        }
        return manager;
    }

    public void addEventManager(EventManager eventManager) {
        if(this.eventManagerMap == null){
            this.eventManagerMap = new HashMap<String, EventManager>();
        }
        this.eventManagerMap.put(String.valueOf(eventManager.getId()), eventManager);
    }

    @Override
    public String toString() {
        return "EventModel{" +
                "updateDate='" + updateDate + '\'' +
                ", eventManagerMap=" + eventManagerMap +
                '}';
    }

    public List<Event> getEvents(){
        List<Event> eventListe = new ArrayList<Event>();
        Map managerMap =  this.eventManagerMap;
        if(managerMap != null && managerMap.size() > 0) {
            Iterator it = managerMap.keySet().iterator();
            while (it.hasNext()) {
                String managerId = (String) it.next();
                EventManager manager = (EventManager) managerMap.get(managerId);
                eventListe.addAll(manager.getEvents());
            }
        }
        return eventListe;
    }

    public List<EventManager> getEventManager(){
        List<EventManager> eventListe = new ArrayList<EventManager>();
        Map managerMap =  this.eventManagerMap;
        if(managerMap != null && managerMap.size() > 0) {
            Iterator it = managerMap.keySet().iterator();
            while (it.hasNext()) {
                String managerId = (String) it.next();
                EventManager manager = (EventManager) managerMap.get(managerId);
                eventListe.add(manager);
            }
        }
        return eventListe;
    }
}
