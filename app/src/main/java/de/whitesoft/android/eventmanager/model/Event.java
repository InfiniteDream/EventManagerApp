package de.whitesoft.android.eventmanager.model;

import java.io.Serializable;

/**
 * Created by akyolo on 30.12.2014.
 */
public class Event implements Serializable, Comparable<Event>{


    private String id;
    private long date;
    private String managerId;
    private String title;
    private String description;
    private boolean alarmiert;

    public Event(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAlarmiert() {
        return alarmiert;
    }

    public void setAlarmiert(boolean alarmiert) {
        this.alarmiert = alarmiert;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public int compareTo(Event another) {
        int compare = 0;
        if(this.getDate() > another.getDate()){
            compare = 1;
        }else if(this.getDate() < another.getDate()){
            compare = -1;
        }
        return compare;
    }
}
