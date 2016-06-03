package de.whitesoft.android.eventmanager.ui;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;

import de.whitesoft.android.eventmanager.R;
import de.whitesoft.android.eventmanager.model.Settings;
import de.whitesoft.android.eventmanager.utils.ManagerUtils;
import de.whitesoft.android.eventmanager.utils.SettingUtils;

/**
 * Created by akyolo on 09.01.2015.
 */
public class SettingsChangeListener implements Preference.OnPreferenceChangeListener{

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

        Log.d("SettingsChangeListener", " onPreferenceChange - preference:" +preference.getTitle()+ ", value:"+  value.toString());
        preference.setSummary("Bitte wählen...");

        SettingUtils.setDefaultSummaryToPreference(preference,value);

        // Setttinfs persistieren

        // Hier werden nur Einstellungen geändert, die sich ändern
        if(preference.getKey().equals("settings_eventShowDays")){
            Settings settings = SettingUtils.getSettings(preference.getContext());
            int eventShowDays = Integer.parseInt(value.toString());
            settings.setEventShowDays(eventShowDays);
            SettingUtils.saveSettings(preference.getContext(), settings);
        }else if(preference.getKey().equals("settings_alarm")){
            Settings settings = SettingUtils.getSettings(preference.getContext());
            settings.setAlarm((Boolean) value);
            SettingUtils.saveSettings(preference.getContext(), settings);
        }else if(preference.getKey().equals("settings_alarmShowDays")){
            Settings settings = SettingUtils.getSettings(preference.getContext());
            int alarmShowDays = Integer.parseInt(value.toString());
            settings.setAlarmShowDays(alarmShowDays);
            SettingUtils.saveSettings(preference.getContext(), settings);
        }else if(preference.getKey().equals("settings_updateInterval")){
            Settings settings = SettingUtils.getSettings(preference.getContext());
            long updateInterval = Long.parseLong(value.toString());
            settings.setUpdateInterval(updateInterval);
            SettingUtils.saveSettings(preference.getContext(), settings);

            // Wenn UpdateInerval sich ändern, kann man AlarmServices neue initialisieren
            ManagerUtils.initBackgroundServices(preference.getContext());

        }else if(preference.getKey().equals("settings_ServiceList")){
            // Nix tun.. es wird in eigener Aktivität persistiert
        }else if(preference.getKey().equals("settings_EventServiceSuchen")){
            // Nix tun.. es wird in eigener Aktivität persistiert
        }else if(preference.getKey().equals("3")){
            // Nix tun.. es wird in eigener Aktivität persistiert
        }



        return true;
    }

}
