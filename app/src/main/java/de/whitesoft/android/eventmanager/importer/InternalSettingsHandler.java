package de.whitesoft.android.eventmanager.importer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.whitesoft.android.eventmanager.model.Event;
import de.whitesoft.android.eventmanager.model.Service;
import de.whitesoft.android.eventmanager.model.Settings;

/**
 * Created by akyolo on 02.02.2015.
 */
public class InternalSettingsHandler extends SQLiteOpenHelper{

    // Datenbank Version
    private static final int DATABASE_VERSION = 7;

    // Database Name
    private static final String DATABASE_NAME = "InternalSettings";

    // Table "ServiceList"
    public static final String TABLE_SERVICE_LIST = "ServiceList";
    private static final String COLUMN_SERVICE_ID = "ServiceId";
    private static final int INDEX_SERVICE_ID = 0;
    private static final String COLUMN_SERVICE_SELECTED = "ServiceSelected";
    private static final int INDEX_SERVICE_SELECTED = 1;

    // Table "AppSettings"
    public static final String TABLE_APP_SETTINGS = "AppSettings";
    private static final String COLUMN_APP_KEY = "appKey";
    private static final int INDEX_APP_KEY = 0;
    private static final String COLUMN_APP_VERSION = "appVersion";
    private static final int INDEX_APP_VERSION = 1;
    private static final String COLUMN_GOOGLE_APP_KEY = "googleMapsApiKey";
    private static final int INDEX_GOOGLE_APP_KEY = 2;
    private static final String COLUMN_UPDATE_INTERVAL = "updateInterval";
    private static final int INDEX_UPDATE_INTERVAL = 3;
    private static final String COLUMN_ALARM = "alarm";
    private static final int INDEX_ALARM = 4;
    private static final String COLUMN_ALARM_SHOW_DAYS = "alarmShowDays";
    private static final int INDEX_ALARM_SHOW_DAYS = 5;
    private static final String COLUMN_EVENT_SHOW_DAYS = "eventShowDays";
    private static final int INDEX_EVENT_SHOW_DAYS = 6;
    private static final String COLUMN_ALARM_INTERVAL = "alarmInterval";
    private static final int INDEX_ALARM_INTERVAL = 7;
    private static final String COLUMN_FIRST_STARTTIME = "firstStartTime";
    private static final int INDEX_FIST_STARTTIME = 8;

    // Table "ServiceList"
    public static final String TABLE_EVENT_ALARM_LIST = "EventAlarmList";
    private static final String COLUMN_EVENT_ID = "eventId";
    private static final int INDEX_EVENT_ID = 0;
    private static final String COLUMN_EVENT_ALARMIERT = "alarmiert";
    private static final int INDEX_EVENT_ALARMIERT = 1;

    // DEFUAL KEYS
    private static final String DEFAULT_APP_KEY = "demo";
    private static final String DEFAULT_APP_VERSION = "1.0";

    public InternalSettingsHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createServiceList(db);
        createAppSettings(db);
        createEventAlarmList(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteServiceList(db);
        deleteAppSettings(db);
        deleteEventAlarmList(db);
        onCreate(db);
    }

    private void createServiceList(SQLiteDatabase db){
        String sql = "CREATE TABLE " + TABLE_SERVICE_LIST + " ("
                + COLUMN_SERVICE_ID + " TEXT NOT NULL UNIQUE, "
                + COLUMN_SERVICE_SELECTED + " SMALLINT NOT NULL "
                + ")";
        db.execSQL(sql);
        Log.v("InternalSettingsHandler", " -- createServiceList -- " + sql);
    }

    private void deleteServiceList(SQLiteDatabase db){
        String sql = "DROP TABLE IF EXISTS " + TABLE_SERVICE_LIST;
        db.execSQL(sql);
        Log.v("InternalSettingsHandler", " -- deleteServiceList -- " + sql);
    }

    private void createAppSettings(SQLiteDatabase db){
        String sql = "CREATE TABLE " + TABLE_APP_SETTINGS + " ("
                + COLUMN_APP_KEY + " TEXT NOT NULL UNIQUE, "
                + COLUMN_APP_VERSION+ " TEXT NOT NULL, "
                + COLUMN_GOOGLE_APP_KEY + " TEXT NOT NULL, "
                + COLUMN_UPDATE_INTERVAL + " BIGINT NOT NULL, "
                + COLUMN_ALARM + " SMALLINT NOT NULL, "
                + COLUMN_ALARM_SHOW_DAYS + " SMALLINT NOT NULL, "
                + COLUMN_EVENT_SHOW_DAYS + " SMALLINT NOT NULL, "
                + COLUMN_ALARM_INTERVAL + " BIGINT NOT NULL, "
                + COLUMN_FIRST_STARTTIME + " BIGINT NOT NULL "
                + ")";
        db.execSQL(sql);
        Log.v("InternalSettingsHandler", " -- createServiceList -- " + sql);
    }

    private void deleteAppSettings(SQLiteDatabase db){
        String sql = "DROP TABLE IF EXISTS " + TABLE_APP_SETTINGS;
        db.execSQL(sql);
        Log.v("InternalSettingsHandler", " -- deleteServiceList -- " + sql);
    }

    private void createEventAlarmList(SQLiteDatabase db){
        String sql = "CREATE TABLE " + TABLE_EVENT_ALARM_LIST + " ("
                + COLUMN_EVENT_ID + " TEXT NOT NULL UNIQUE, "
                + COLUMN_EVENT_ALARMIERT + " SMALLINT NOT NULL "
                + ")";
        db.execSQL(sql);
        Log.v("InternalSettingsHandler", " -- createEventAlarmList -- " + sql);
    }

    private void deleteEventAlarmList(SQLiteDatabase db){
        String sql = "DROP TABLE IF EXISTS " + TABLE_EVENT_ALARM_LIST;
        db.execSQL(sql);
        Log.v("InternalSettingsHandler", " -- deleteEventAlarmList -- " + sql);
    }

    public static final String[] PROJECTION_SELECTED_SERVICES = {COLUMN_SERVICE_ID, COLUMN_SERVICE_SELECTED};

    public static synchronized List<String> getSelectedServices(Cursor cursor) {
        List<String> liste = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                String serviceId = cursor.getString(INDEX_SERVICE_ID);
                int selected = cursor.getInt(INDEX_SERVICE_SELECTED);
                if(selected == 1) {
                    liste.add(serviceId);
                }
                Log.v("InternalSettingsHandler", " serviceId: " + serviceId + ", selected:" + selected);
            }while (cursor.moveToNext());

        }
        cursor.close();
        return liste;
    }

    public static synchronized ContentValues getValuesForSelectedService(Service service, boolean selected){
        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICE_ID, service.getId());
        if (selected) {
            values.put(COLUMN_SERVICE_SELECTED, 1);
        } else {
            values.put(COLUMN_SERVICE_SELECTED, 0);
        }
        Log.v("InternalSettingsHandler", " values: " +values.toString());
        return values;
    }

    public static synchronized ContentValues getValuesForAppSettings(Settings settings) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_APP_KEY, DEFAULT_APP_KEY);
        values.put(COLUMN_APP_VERSION, DEFAULT_APP_VERSION);
        values.put(COLUMN_GOOGLE_APP_KEY, DEFAULT_APP_KEY);
        values.put(COLUMN_UPDATE_INTERVAL, settings.getUpdateInterval());
        if (settings.isAlarm()) {
            values.put(COLUMN_ALARM, 1);
        } else {
            values.put(COLUMN_ALARM, 0);
        }
        values.put(COLUMN_ALARM_SHOW_DAYS, settings.getAlarmShowDays());
        values.put(COLUMN_EVENT_SHOW_DAYS,settings.getEventShowDays());
        values.put(COLUMN_ALARM_INTERVAL,settings.getAlarmInterval());
        values.put(COLUMN_FIRST_STARTTIME,settings.getFirstStartTime());

        Log.v("InternalSettingsHandler", " settings konvetriert: " + settings.toString());
        return values;
    }

    public static final String[] PROJECTION_APP_SETTINGS = {COLUMN_APP_KEY, COLUMN_APP_VERSION , COLUMN_GOOGLE_APP_KEY,COLUMN_UPDATE_INTERVAL,COLUMN_ALARM,COLUMN_ALARM_SHOW_DAYS, COLUMN_EVENT_SHOW_DAYS, COLUMN_ALARM_INTERVAL, COLUMN_FIRST_STARTTIME};

    public static synchronized Settings getAppSettings(Cursor cursor) {
       Settings settings = new Settings();
        if(cursor.moveToFirst()){
            do{
                String appKey = cursor.getString(INDEX_APP_KEY);
                String appVersion = cursor.getString(INDEX_APP_VERSION);
                String googleApiKey = cursor.getString(INDEX_GOOGLE_APP_KEY);
                long updateInterval = cursor.getLong(INDEX_UPDATE_INTERVAL);
                int alarm = cursor.getInt(INDEX_ALARM);
                int alarmDayOffset = cursor.getInt(INDEX_ALARM_SHOW_DAYS);
                int eventsDayOffset = cursor.getInt(INDEX_EVENT_SHOW_DAYS);
                long alarmInterval = cursor.getLong(INDEX_ALARM_INTERVAL);
                long firstStarttime = cursor.getLong(INDEX_FIST_STARTTIME);

                // Setting Values
                settings.setAppKey(appKey);
                settings.setAppVersion(appVersion);
                settings.setGoogleMapsApiKey(googleApiKey);
                settings.setFirstStartTime(firstStarttime);
                settings.setUpdateInterval(updateInterval);
                settings.setAlarmInterval(alarmInterval);
                settings.setAlarmShowDays(alarmDayOffset);
                settings.setEventShowDays(eventsDayOffset);
                if(alarm == 1) {
                    settings.setAlarm(true);
                }else{
                    settings.setAlarm(false);
                }

                Log.v("InternalSettingsHandler", " settings gelesen: " + settings.toString());
            }while (cursor.moveToNext());

        }
        cursor.close();
        return settings;
    }

    public static final String[] PROJECTION_EVENT_ALARMIERT = {COLUMN_EVENT_ID, COLUMN_EVENT_ALARMIERT};

    public static synchronized ContentValues getValuesForEventAlarmiert(Event event) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_ID, event.getId());
        values.put(COLUMN_EVENT_ALARMIERT, 1);
        Log.v("InternalSettingsHandler", " values: " +values.toString());
        return values;
    }

    public static synchronized List<String> getAlarmierteEventIds(Cursor cursor){
        List<String> liste = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                String eventId = cursor.getString(INDEX_EVENT_ID);
                int selected = cursor.getInt(INDEX_EVENT_ALARMIERT);
                if(selected == 1) {
                    liste.add(eventId);
                }
                Log.v("InternalSettingsHandler", " eventId: " + eventId + ", alarmiert:" + eventId);
            }while (cursor.moveToNext());

        }
        cursor.close();
        return liste;
    }

}
