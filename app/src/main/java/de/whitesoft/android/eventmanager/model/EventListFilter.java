package de.whitesoft.android.eventmanager.model;

import java.util.List;

/**
 * Created by akyolo on 02.01.2015.
 */
public class EventListFilter {

    private List<String> managerIds;

    private String eventId;

    private String eventType;

    private String eventLocation;

    private long startDate = -1;

    private int dayOffset = 1;

    public EventListFilter(){
        startDate = System.currentTimeMillis();
    }

    public List<String> getManagerIds() {
        return managerIds;
    }

    public void setManagerIds(List<String> Ids) {
        this.managerIds = Ids;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public int getDayOffset() {
        return dayOffset;
    }

    public void setDayOffset(int dayOffset) {
        this.dayOffset = dayOffset;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "EventListFilter{" +
                "managerIds=" + managerIds +
                ", eventId=" + eventId +
                ", eventType='" + eventType + '\'' +
                ", eventLocation='" + eventLocation + '\'' +
                ", startDate=" + startDate +
                ", dayOffset=" + dayOffset +
                '}';
    }
}
