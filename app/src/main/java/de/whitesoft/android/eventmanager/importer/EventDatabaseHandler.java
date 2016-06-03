package de.whitesoft.android.eventmanager.importer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateUtils;
import android.util.Log;

import java.util.List;

import de.whitesoft.android.eventmanager.model.Event;
import de.whitesoft.android.eventmanager.model.EventManager;
import de.whitesoft.android.eventmanager.model.EventModel;
import de.whitesoft.android.eventmanager.model.EventListFilter;

/**
 * Created by akyolo on 31.12.2014.
 */
public class EventDatabaseHandler extends SQLiteOpenHelper{

    // Datenbank Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "EventManager";

    // Contacts table name
    private static final String TABLE_EVENTS = "events";

    // eventManagerId, eventType, eventId, eventDate, eventLocation
    // ACHTUNG: Tabellen Reihenfolge wie folgt
    private static final String EVENTS_COLUMN_KEY_ID = "eventKeyId";
    private static final int EVENTS_COLUMN_KEY_ID_INDEX = 0;
    private static final String EVENTS_COLUMN_TYPE = "eventType";
    private static final int EVENTS_COLUMN_TYPE_INDEX = 1;
    private static final String EVENTS_COLUMN_ID = "eventId";
    private static final int EVENTS_COLUMN_ID_INDEX = 2;
    private static final String EVENTS_COLUMN_DATE = "eventDate";
    private static final int EVENTS_COLUMN_DATE_INDEX = 3;
    private static final String EVENTS_COLUMN_LOCATION = "eventLocation";
    private static final int EVENTS_COLUMN_LOCATION_INDEX = 4;
    private static final String EVENTS_COLUMN_MANAGER_ID = "eventManagerId";
    private static final int EVENTS_COLUMN_MANAGER_ID_INDEX = 5;

    // TODO:
    //  A SQLiteConnection object for database '/data/data/de.whitesoft.android.eventmanager/databases/EventManager' was leaked!  Please fix your application to end transactions in progress properly and to close the database when it is no longer needed.
    public EventDatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_EVENTS + " ("
                + " _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EVENTS_COLUMN_KEY_ID + " TEXT NOT NULL UNIQUE, "
                + EVENTS_COLUMN_TYPE + " TEXT, "
                + EVENTS_COLUMN_ID + " INTEGER NOT NULL, "
                + EVENTS_COLUMN_DATE + " INTEGER NOT NULL, "
                + EVENTS_COLUMN_LOCATION+ " TEXT, "
                + EVENTS_COLUMN_MANAGER_ID + " INTEGER "
                + ")";
        Log.v("EventDatabaseHandler"," -- onCreate -- "+sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_EVENTS;
        db.execSQL(sql);
        onCreate(db);
    }

    public EventModel queryEventList(EventListFilter filter){

        EventModel model = new EventModel();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_EVENTS + " WHERE ";
        if(filter.getStartDate() > -1){
            // TODO DayOffset
            long startDate  = filter.getStartDate();
            long endDate = startDate + (filter.getDayOffset() * DateUtils.DAY_IN_MILLIS);
            sql += EVENTS_COLUMN_DATE + " BETWEEN " + startDate + " AND " + endDate;
        }
        sql += " ORDER BY " + EVENTS_COLUMN_DATE + " ASC";
        // TODO ist das wirklich nötig ???
        //if(filter.getManagerId() > -1){
        //    sql += EVENTS_COLUMN_MANAGER_ID + " = " + filter.getManagerId();
        //}
        //if(filter.getEventId() > -1){
        //    sql += " AND "+ EVENTS_COLUMN_ID + " = " + filter.getEventId();
        //}

        Log.v("queryEventList", " SQL: " + sql);

        Cursor cursor = db.rawQuery(sql, null);

        EventManager manager = new EventManager();
        // TODO:  Datenbefüllen von Filter zu EventManger
        //manager.setId(filter.getManagerId());
        int index = 0;
        if(cursor.moveToFirst()){
            do{
                Event event = new Event();
                event.setId(cursor.getString(EVENTS_COLUMN_ID_INDEX));
                event.setDate(cursor.getLong(EVENTS_COLUMN_DATE_INDEX));
                //event.setManagerId(filter.getManagerId());
                manager.addEvent(event);
                index++;
            }while (cursor.moveToNext());

        }

        model.addEventManager(manager);
        db.close();
        Log.v("EventDatabaseHandler", " Events Found :" + index);

        return model;
    }


    public synchronized void updateEventModel(EventModel model){

        SQLiteDatabase db = this.getWritableDatabase();
        Log.v("EventDatabaseHandler", " EVENT UPDATE vorher found: " + countEventsInDb(null, -1));
        try {
            List<Event> events = model.getEvents();
            if (events != null && events.size() > 0) {
                for (Event event : events) {

                    Log.v("EventDatabaseHandler", " EVENT UPDATE: " + event.toString());

                    EventManager manager = model.getEventManager(event.getManagerId());
                    ContentValues values = new ContentValues();
                    values.put(EVENTS_COLUMN_MANAGER_ID, manager.getId());
                    values.put(EVENTS_COLUMN_TYPE, manager.getType());
                    values.put(EVENTS_COLUMN_LOCATION, manager.getLocation());
                    values.put(EVENTS_COLUMN_ID, event.getId());
                    values.put(EVENTS_COLUMN_DATE, event.getDate());
                    values.put(EVENTS_COLUMN_KEY_ID, event.getId());
                    // TODO Was sit eindeutig für ein Event ???

                    // INSERT or Replace: EventId und EventDate eindeutig
                    //String sql = "INSERT OR IGNORE INTO " + TABLE_EVENTS + "(" + EVENTS_COLUMN_ID + "," + EVENTS_COLUMN_DATE + ") VALUES (" + event.getId() + "," + event.getDate() + ")";
                    // Update Data
                    // db.execSQL(sql);

                        int count = countEventsInDb(event.getId(), event.getDate());
                        db.beginTransaction();
                        //if (count > 0) {
                            long result = db.delete(TABLE_EVENTS, " eventId = " + event.getId() + " AND eventDate=" + event.getDate(), null);
                            Log.v("EventDatabaseHandler", " SQL DELETE for Rows-Count=" + result);
                        //}
                        result = db.insert(TABLE_EVENTS, null, values);
                        Log.v("EventDatabaseHandler", " SQL INSERT for RowId=" + result);
                        db.setTransactionSuccessful();
                        db.endTransaction();
                        // long result = db.insertWithOnConflict(TABLE_EVENTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        // eventId, eventDate Eindeutig ....

                }
                Log.v("EventDatabaseHandler"," Found Events to Insert :"+events.size());
                Log.v("EventDatabaseHandler", " Nach Update found: " + countEventsInDb(null,-1));
            }
        }catch (Exception e){
            throw e;
        }finally {
            db.close();
        }

    }


    public int countEventsInDb(String eventId, long eventDate){
        int counter = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_EVENTS;

        if(eventId != null){
            sql += " WHERE ";
            sql += EVENTS_COLUMN_ID + " = " + eventId;
        }
        if(eventDate > -1){
            sql += " AND "+ EVENTS_COLUMN_DATE + " = " + eventDate;
        }
        //Log.v("queryEventList", " SQL: " + sql);
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                counter++;
                //Log.v("EventDatabaseHandler"," EVENT: " + event.toString());
            }while (cursor.moveToNext());

        }
       return counter;
    }
}
