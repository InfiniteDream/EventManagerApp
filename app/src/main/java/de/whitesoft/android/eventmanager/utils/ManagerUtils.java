package de.whitesoft.android.eventmanager.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.whitesoft.android.eventmanager.model.Settings;
import de.whitesoft.android.eventmanager.service.ServiceDatenbaseUpdate;
import de.whitesoft.android.eventmanager.service.ServiceEventAlarm;

/**
 * Created by akyolo on 05.01.2015.
 */
public class ManagerUtils {

    // 17.01.2015
    private static final String DATE_FORMAT = "dd.MM.yyyy";


    private ManagerUtils(){

    }

    public static synchronized void initBackgroundServices(Context context){
        checkAndCreateUpdateService(context);
        checkAndCreateAlarmService(context);
    }

    public static synchronized void startAlarmCheck(Context context){
        // Alarm Service Starten...
        Log.v("-- startAlarmCheck -- ", "Alarm-Service startet...");
        Settings settings = SettingUtils.getSettings(context);
        // Alarm Service Prüfen
        if(settings.isAlarm()) {
            // EventAlarm Starten
            Intent alarmServiceIntent = new Intent(context, ServiceEventAlarm.class);
            context.startService(alarmServiceIntent);
        }
    }

    public static synchronized void startUpdateCheck(Context context){
        // Alarm Service Starten...
        Log.v("-- startUpdateCheck -- ", "Database-Service startet...");

        // EventAlarm Starten
        Intent updateServiceIntent = new Intent(context, ServiceDatenbaseUpdate.class);
        context.startService(updateServiceIntent);
    }

    private static long calcFirstStartTime(Settings settings){
        long start = DateUtils.SECOND_IN_MILLIS * 15;
        if(settings.getFirstStartTime() > start){
            start = settings.getFirstStartTime();
        }
        return System.currentTimeMillis() + start;
    }

    private static long calcAlarmInterval(Settings settings){
        long start = DateUtils.MINUTE_IN_MILLIS;
        if(settings.getAlarmInterval() > start){
            start = settings.getAlarmInterval();
        }
        return start;
    }

    private static long calcUpdateInterval(Settings settings){
        long start = DateUtils.MINUTE_IN_MILLIS;
        if(settings.getUpdateInterval() > start){
            start = settings.getUpdateInterval();
        }
        return start;
    }

    private static PendingIntent piAlarmService;
    private static PendingIntent piUpdateService;

    public static synchronized void checkAndCreateAlarmService(Context context){
        Settings settings = SettingUtils.getSettings(context);
        // Alarm Service Prüfen
        if(settings.isAlarm()) {
            // Alarm Service Starten...
            Log.v("-- checkAndCreateAlarmService -- ", "Alarm-Service startet...");
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if(piAlarmService != null){
                am.cancel(piAlarmService);
            }

            // EventAlarm Starten
            Intent alarmServiceIntent = new Intent(context, ServiceEventAlarm.class);
            piAlarmService = PendingIntent.getService(context, 0, alarmServiceIntent, 0);
            am.setInexactRepeating(AlarmManager.RTC, calcFirstStartTime(settings), calcAlarmInterval(settings), piAlarmService);

            Log.v("-- checkAndCreateAlarmService --", " Alarm-Service gestartet.");
        }else{
            if(piAlarmService != null){
                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                am.cancel(piAlarmService);
            }
        }
    }

    public static synchronized void checkAndCreateUpdateService(Context context) {

        // Alarm Service Starten...
        Settings settings = SettingUtils.getSettings(context);

        Log.v("-- checkAndCreateUpdateService -- ", "Database-Service startet...");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(piUpdateService != null){
            am.cancel(piUpdateService);
        }
        // EventAlarm Starten
        Intent updateServiceIntent = new Intent(context, ServiceDatenbaseUpdate.class);
        piUpdateService = PendingIntent.getService(context,0,updateServiceIntent,0);
        am.setInexactRepeating(AlarmManager.RTC, calcFirstStartTime(settings), calcUpdateInterval(settings), piUpdateService);

        Log.v("-- checkAndCreateUpdateService --" , "Database-Service gestartet." );
    }




    private static boolean isConnected(Context context){
        boolean connected = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if(info != null && info.isConnected()){
            connected = true;
        }
        return connected;
    }



    public static String convertDate(long dateLong){
        Date date = new Date(dateLong);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        String returnDate = format.format(date);
        return  returnDate;
    }

    public static long convertDate(String dateString){

        long returnDate = -1;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date date = format.parse(dateString);
            returnDate = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    public static final String ACTION_NAME_UPDATE_EVENT_LIST = "CallbackForUpdateEventList";

    public static void sendCallbackForUpdateEventList(Context context){
        // TODO: Impl fehlt..
        // Event Update Callback
        Intent intent = new Intent(ACTION_NAME_UPDATE_EVENT_LIST);
        intent.putExtra("callback", true);
        context.sendBroadcast(intent);
    }
}
