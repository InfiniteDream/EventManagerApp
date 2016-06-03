package de.whitesoft.android.eventmanager.utils;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.whitesoft.android.eventmanager.R;
import de.whitesoft.android.eventmanager.importer.InternalSettingsHandler;
import de.whitesoft.android.eventmanager.importer.SettingsContentProvider;
import de.whitesoft.android.eventmanager.importer.SettingsHandler;
import de.whitesoft.android.eventmanager.model.Event;
import de.whitesoft.android.eventmanager.model.EventListFilter;
import de.whitesoft.android.eventmanager.model.Service;
import de.whitesoft.android.eventmanager.model.Settings;

/**
 * Created by akyolo on 09.01.2015.
 */
public class SettingUtils {

    private static  final String CLOUD_SERVICE_URL = "http://tomcat-eventmanager.rhcloud.com/eventme/";

    private static final String CLOUD_METHOD_QUERY = "list";

    private static final String CLOUD_METHOD_SERVICE_LIST = "servicelist";

    public static final int SERVICE_CONN_TIMEOUT = 5000;

    private SettingUtils(){

    }

    public static String getCloudMethodServiceList(){
        return CLOUD_SERVICE_URL + CLOUD_METHOD_QUERY;
    }

    public static String getCloudMethodQuery(){
        return CLOUD_SERVICE_URL + CLOUD_METHOD_QUERY;
    }



    public static synchronized EventListFilter getEventFilterFromSettings(Context context){
        EventListFilter filter = new EventListFilter();
        filter.setStartDate(System.currentTimeMillis());
        // Settings
        Settings settings = getSettings(context);
        filter.setDayOffset(settings.getEventShowDays());
        List<String> services = getSelectedServices(context);
        filter.setManagerIds(services);

        Log.v("SettingUtils", "-- getEventFilterFromSettings --> "+ settings.toString() + ", -- "+ filter.toString());

        return filter;
    }

    public static List<Service> mergeServiceList(Context context, List<Service> managers) {
        if(managers != null) {
            List<String> selectedServices = getSelectedServices(context);
            for (Service service : managers) {
                if (selectedServices != null && selectedServices.contains(service.getId())) {
                    service.setSelected(true);
                }else{
                    service.setSelected(false);
                }
            }
        }
        return managers;
    }

    public static synchronized void setDefaultSummaryToPreference(Preference preference, Object value){
        if (preference instanceof ListPreference) {
            String stringValue = value.toString();
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);

        } else if (preference instanceof RingtonePreference) {
            String stringValue = value.toString();
            // For ringtone preferences, look up the correct display value
            // using RingtoneManager.
            if (TextUtils.isEmpty(stringValue)) {
                // Empty values correspond to 'silent' (no ringtone).
                preference.setSummary(R.string.pref_alarm_ringtone_silent);

            } else {
                Ringtone ringtone = RingtoneManager.getRingtone(
                        preference.getContext(), Uri.parse(stringValue));

                if (ringtone == null) {
                    // Clear the summary if there was a lookup error.
                    preference.setSummary(null);
                } else {
                    // Set the summary to reflect the new ringtone display
                    // name.
                    String name = ringtone.getTitle(preference.getContext());
                    preference.setSummary(name);
                }
            }

        }else if(preference instanceof CheckBoxPreference) {
            boolean selected = (boolean) value;
            CheckBoxPreference pref = (CheckBoxPreference)preference;
            pref.setChecked(selected);
            if(selected){
                preference.setSummary("Aktiviert");
            }else{
                preference.setSummary("Deaktiviert");
            }
        }
    }

    // GET Contents...

    public static synchronized Settings getSettings(Context context){
        Uri uri = ContentUris.withAppendedId(SettingsContentProvider.CONTENT_URI, SettingsContentProvider.URI_QUERY_SETTINGS);
        Cursor cursor = context.getContentResolver().query(uri, InternalSettingsHandler.PROJECTION_APP_SETTINGS, null,null,null);
        Settings settings = InternalSettingsHandler.getAppSettings(cursor);
        return settings;
    }

    public static synchronized List<String> getAlarmierteEventIds(Context context){
        Uri uri = ContentUris.withAppendedId(SettingsContentProvider.CONTENT_URI, SettingsContentProvider.URI_QUERY_EVENT_ALARM_LIST);
        Cursor cursor = context.getContentResolver().query(uri, InternalSettingsHandler.PROJECTION_EVENT_ALARMIERT, null,null,null);
        List<String> liste = InternalSettingsHandler.getAlarmierteEventIds(cursor);
        return liste;
    }

    public static synchronized List<String> getSelectedServices(Context context){
        Uri uri = ContentUris.withAppendedId(SettingsContentProvider.CONTENT_URI, SettingsContentProvider.URI_QUERY_SERVICE_LIST);
        Cursor cursor = context.getContentResolver().query(uri, InternalSettingsHandler.PROJECTION_SELECTED_SERVICES,null,null,null);
        List<String> services = InternalSettingsHandler.getSelectedServices(cursor);
        return services;
    }

    // SET Contents...

    public static synchronized void setEventAlarmiert(Context context, Event event){
        Uri uri = ContentUris.withAppendedId(SettingsContentProvider.CONTENT_URI, SettingsContentProvider.URI_UPDATE_EVENT_ALARM_LIST);
        ContentValues values = InternalSettingsHandler.getValuesForEventAlarmiert(event);
        int result = context.getContentResolver().update(uri, values, null,null);
        Log.v("SettingUtils", "-- Event saved, result:"+ result);
    }

    public static synchronized void setServiceState(Context context, Service service, boolean activ){
        Uri uri = ContentUris.withAppendedId(SettingsContentProvider.CONTENT_URI, SettingsContentProvider.URI_UPDATE_SERVICE_LIST);
        ContentValues values = InternalSettingsHandler.getValuesForSelectedService(service, activ);
        int result = context.getContentResolver().update(uri, values, null, null);
        Log.v("SettingUtils", "-- Service saved, result:"+ result);
    }


    public static synchronized void saveSettings(Context context, Settings settings){
        Uri uri = ContentUris.withAppendedId(SettingsContentProvider.CONTENT_URI, SettingsContentProvider.URI_UPDATE_SETTINGS);
        ContentValues values = InternalSettingsHandler.getValuesForAppSettings(settings);
        int result = context.getContentResolver().update(uri, values, null, null);

        Log.v("SettingUtils", "-- settings saved, result:"+ result);
    }
}
