package de.whitesoft.android.eventmanager.model;

import android.text.format.DateUtils;

/**
 * Created by akyolo on 17.01.2015.
 */
public class Settings {

    private String appKey;
    private String appVersion;
    private String googleMapsApiKey;
    private boolean alarm;
    private long alarmInterval;
    private long updateInterval;
    private long firstStartTime;
    private int eventShowDays;
    private int alarmShowDays;

    public Settings(){
        this.appKey = "demo";
        this.appVersion = "1.0";
        this.googleMapsApiKey = "demo";
        this.alarm = false;
        this.firstStartTime = DateUtils.MINUTE_IN_MILLIS;
        this.alarmInterval = DateUtils.DAY_IN_MILLIS;
        this.updateInterval  = DateUtils.HOUR_IN_MILLIS;
        this.eventShowDays = 1;
        this.alarmShowDays = 1;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    public long getAlarmInterval() {
        return alarmInterval;
    }

    public void setAlarmInterval(long alarmInterval) {
        this.alarmInterval = alarmInterval;
    }

    public long getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(long updateInterval) {
        this.updateInterval = updateInterval;
    }

    public long getFirstStartTime() {
        return firstStartTime;
    }

    public void setFirstStartTime(long firstStartTime) {
        this.firstStartTime = firstStartTime;
    }

    public int getEventShowDays() {
        return eventShowDays;
    }

    public void setEventShowDays(int eventShowDays) {
        this.eventShowDays = eventShowDays;
    }

    public int getAlarmShowDays() {
        return alarmShowDays;
    }

    public void setAlarmShowDays(int alarmShowDays) {
        this.alarmShowDays = alarmShowDays;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getGoogleMapsApiKey() {
        return googleMapsApiKey;
    }

    public void setGoogleMapsApiKey(String googleMapsApiKey) {
        this.googleMapsApiKey = googleMapsApiKey;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "appKey='" + appKey + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", googleMapsApiKey='" + googleMapsApiKey + '\'' +
                ", alarm=" + alarm +
                ", alarmInterval=" + alarmInterval +
                ", updateInterval=" + updateInterval +
                ", firstStartTime=" + firstStartTime +
                ", eventShowDays=" + eventShowDays +
                ", alarmShowDays=" + alarmShowDays +
                '}';
    }
}
