package de.whitesoft.android.eventmanager.importer;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class SettingsContentProvider extends ContentProvider {

    // SettingsDatabase
    private static InternalSettingsHandler database;

    private static final String AUTHORITY = "de.whitesoft.android.eventmanager";
    private static final String BASE_PATH = "settings";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    //public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/settings";
    //public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/setting";

    //private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final int URI_QUERY_SETTINGS = 100;
    public static final int URI_QUERY_SERVICE_LIST = 200;
    public static final int URI_QUERY_EVENT_ALARM_LIST = 300;

    public static final int URI_UPDATE_SETTINGS = 101;
    public static final int URI_UPDATE_SERVICE_LIST = 201;
    public static final int URI_UPDATE_EVENT_ALARM_LIST = 301;

    static {
        //uriMatcher.addURI(AUTHORITY, BASE_PATH, URI_QUERY_SETTINGS);
    }

    public SettingsContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        database = new InternalSettingsHandler(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();


        SQLiteDatabase db = database.getReadableDatabase();

        String sql = "";
        String methode = uri.getLastPathSegment();
        int param = Integer.parseInt(methode);
        switch (param){
            case URI_QUERY_SETTINGS:
                // query für TABLE_APP_SETTINGS
                builder.setTables(InternalSettingsHandler.TABLE_APP_SETTINGS);
            break;
            case URI_QUERY_SERVICE_LIST:
                //  query für TABLE_SERVICE_LIST
                builder.setTables(InternalSettingsHandler.TABLE_SERVICE_LIST);
                break;
            case URI_QUERY_EVENT_ALARM_LIST:
                //  query für TABLE_EVENT_ALARM_LIST
                builder.setTables(InternalSettingsHandler.TABLE_EVENT_ALARM_LIST);
                break;
            default:
                throw new IllegalArgumentException("Ungültiger URL:" + uri);
        }
        //

        cursor = builder.query(db, projection, selection, selectionArgs,null,null,sortOrder);
        // db.beginTransaction();
        // cursor = db.rawQuery(sql,null);
        // db.endTransaction();
        // Callbacks
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db = database.getWritableDatabase();
        int result = -1;
        String methode = uri.getLastPathSegment();
        int param = Integer.parseInt(methode);
        switch (param){
            case URI_UPDATE_SETTINGS:
                // update für TABLE_APP_SETTINGS
                db.beginTransaction();
                long transaktion = db.insertWithOnConflict(InternalSettingsHandler.TABLE_APP_SETTINGS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                db.setTransactionSuccessful();
                db.endTransaction();
                Log.v("InternalSettingsHandler", " settings gespeichert result=" + transaktion);
                result = 1;
                break;
            case URI_UPDATE_SERVICE_LIST:
                // update für TABLE_SERVICE_LIST
                db.beginTransaction();
                transaktion = db.insertWithOnConflict(InternalSettingsHandler.TABLE_SERVICE_LIST, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                db.setTransactionSuccessful();
                db.endTransaction();
                Log.v("InternalSettingsHandler", " ServiceList gespeichert result=" + transaktion);
                result = 1;
                break;
            case URI_UPDATE_EVENT_ALARM_LIST:
                // update für TABLE_EVENT_ALARM_LIST
                db.beginTransaction();
                transaktion = db.insertWithOnConflict(InternalSettingsHandler.TABLE_EVENT_ALARM_LIST, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                db.setTransactionSuccessful();
                db.endTransaction();
                Log.v("InternalSettingsHandler", " EventAlarm gespeichert result=" + transaktion);
                result = 1;
                break;
            default:
                throw new IllegalArgumentException("Ungültiger URL:" + uri);
        }
        return result;
    }
}
