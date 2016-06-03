package de.whitesoft.android.eventmanager.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

import de.whitesoft.android.eventmanager.R;
import de.whitesoft.android.eventmanager.model.Settings;
import de.whitesoft.android.eventmanager.utils.SettingUtils;


/**
 * This fragment shows general preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 */

public class SettingsServicePreferenceFragment extends PreferenceFragment {

    private static final SettingsChangeListener listener = new SettingsChangeListener();

    public SettingsServicePreferenceFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_service);

        // Default Setting aus Datenbank
        Settings settings = SettingUtils.getSettings(getActivity());
        setDefaultValueUpdateInterval(settings);
        setDefaultValueEventShowDays(settings);
        setDefaultValueAlarm(settings);
        setDefaultValueAlarmShowDays(settings);

        //
        // setDefaultValueServiceList(settings);
    }

    private void setDefaultValueServiceList(Settings defaultSettings) {
        Preference preference = findPreference("settings_ServiceList");
        if(preference != null) {
            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(listener);
            // Set Default Value
            Object value = null;
            // listener.onPreferenceChange(preference, value);
            SettingUtils.setDefaultSummaryToPreference(preference,value);
        }else{
            Log.e("SettingsServicePreferenceFragment","bindPreferenceSummaryToValue: NULL" );
        }
    }

    private void setDefaultValueUpdateInterval(Settings defaultSettings) {
        Preference preference = findPreference("settings_updateInterval");
        if(preference != null) {
            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(listener);
            // Set Default Value
            Object value = defaultSettings.getUpdateInterval();
            if(value == null) {
                value = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getLong(preference.getKey(), 3600000);
            }
            // listener.onPreferenceChange(preference, value);
            SettingUtils.setDefaultSummaryToPreference(preference,value);
        }else{
            Log.e("SettingsServicePreferenceFragment","bindPreferenceSummaryToValue: NULL" );
        }
    }

    private void setDefaultValueAlarm(Settings defaultSettings) {
        Preference preference = findPreference("settings_alarm");
        if(preference != null) {
            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(listener);
            // Set Default Value
            Object value = defaultSettings.isAlarm();
            if(value == null) {
                value = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getBoolean(preference.getKey(), true);
            }
            //listener.onPreferenceChange(preference, value);
            SettingUtils.setDefaultSummaryToPreference(preference,value);
        }else{
            Log.e("SettingsServicePreferenceFragment","bindPreferenceSummaryToValue: NULL" );
        }
    }

    private void setDefaultValueAlarmShowDays(Settings defaultSettings) {
        Preference preference = findPreference("settings_alarmShowDays");
        if(preference != null) {
            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(listener);
            // Set Default Value
            Object value = defaultSettings.getAlarmShowDays();
            if(value == null) {
                value = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getInt(preference.getKey(), 24);
            }
            //listener.onPreferenceChange(preference, value);
            SettingUtils.setDefaultSummaryToPreference(preference,value);
        }else{
            Log.e("SettingsServicePreferenceFragment","bindPreferenceSummaryToValue: NULL" );
        }
    }


    private void setDefaultValueEventShowDays(Settings defaultSettings) {
        Preference preference = findPreference("settings_eventShowDays");
        if(preference != null) {
            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(listener);
            // Set Default Value
            Object value = defaultSettings.getEventShowDays();
            if(value == null) {
                value = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getInt(preference.getKey(), 1);
            }
            //listener.onPreferenceChange(preference, value);
            SettingUtils.setDefaultSummaryToPreference(preference,value);
        }else{
            Log.e("SettingsServicePreferenceFragment","bindPreferenceSummaryToValue: NULL" );
        }
    }


}
