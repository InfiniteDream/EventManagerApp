package de.whitesoft.android.eventmanager.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.whitesoft.android.eventmanager.model.Settings;
import de.whitesoft.android.eventmanager.utils.ManagerUtils;
import de.whitesoft.android.eventmanager.utils.SettingUtils;

public class ServiceBootReceiver extends BroadcastReceiver {

    public ServiceBootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.v("-- ServiceBootReceiver --" , " gestartet... " );

        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){

            // EventAlarm Starten
            /*
            Intent alarmServiceIntent = new Intent(context, ServiceEventAlarm.class);
            PendingIntent piAlarmService = PendingIntent.getService(context,0,alarmServiceIntent,0);
            Settings settings = SettingUtils.getSettings(context);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.setInexactRepeating(AlarmManager.RTC, settings.getFirstStartTime(), settings.getAlarmInterval(), piAlarmService);
            */
            ManagerUtils.initBackgroundServices(context);

            Log.v("-- ServiceBootReceiver --" , " initBackgroundServices gestartet... " );
        }

    }
}
