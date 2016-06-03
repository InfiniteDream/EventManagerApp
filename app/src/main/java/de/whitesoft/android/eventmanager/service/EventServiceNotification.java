package de.whitesoft.android.eventmanager.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.whitesoft.android.eventmanager.R;
import de.whitesoft.android.eventmanager.model.Event;
import de.whitesoft.android.eventmanager.ui.EventItemDetailActivity;
import de.whitesoft.android.eventmanager.ui.EventItemDetailFragment;
import de.whitesoft.android.eventmanager.utils.SettingUtils;


public class EventServiceNotification {

    private static final String NOTIFICATION_TAG = "EventService";

    private static void createNotification(final Context context, final Event event, int index) {

        try {

            final Resources res = context.getResources();
            final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.example_picture);

            //TODO : Datenbefüllen
            final String eventTitle = event.getTitle();
            final String eventTicker = event.getId();
            //final int eventNummer = event.getId();
            final String contentTitle = res.getString(
                    R.string.event_service_notification_title_template, eventTitle);
            final String contentText = res.getString(
                    R.string.event_service_notification_placeholder_text_template, eventTitle);

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setDefaults(Notification.DEFAULT_ALL);
            builder.setSmallIcon(R.drawable.ic_stat_event_service);
            builder.setContentTitle(contentTitle);
            builder.setContentText(contentText);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.setLargeIcon(picture);
            builder.setTicker(eventTicker);
            builder.setNumber(index);

            // TODO : Eventdaten übermitteln...
            Intent detailIntent = new Intent(context, EventItemDetailActivity.class);
            detailIntent.putExtra(EventItemDetailFragment.ITEM_EVENT_ID, event.getId());
            detailIntent.putExtra(EventItemDetailFragment.ITEM_MANAGER_ID, event.getManagerId());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationCompat.Style style = new NotificationCompat.BigTextStyle()
                    .bigText(eventTitle)
                    .setBigContentTitle(contentTitle)
                    .setSummaryText("Event...");
            builder.setStyle(style);
            builder.addAction(
                    R.drawable.ic_action_stat_share,
                    res.getString(R.string.action_share),
                    PendingIntent.getActivity(
                            context,
                            0,
                            Intent.createChooser(new Intent(Intent.ACTION_SEND)
                                    .setType("text/plain")
                                    .putExtra(Intent.EXTRA_TEXT, "Dummy text"), "Dummy title"),
                            PendingIntent.FLAG_UPDATE_CURRENT));
            builder.addAction(
                    R.drawable.ic_action_stat_reply,
                    res.getString(R.string.action_reply),
                    null);
            builder.setAutoCancel(true);

            Log.v("EventServiceNotification", "Event Alarm wird ausgelöst... Event:" + event.toString());
            notify(context, builder.build(), index);

            // Markeire Event als Alarmiert
            SettingUtils.setEventAlarmiert(context, event);

        }catch (Exception e){
            Log.v("EventServiceNotification", "Alarm konnte nicht durchgeführt werden!", e);
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification, int id) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG+id, id, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context, int id) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG+id, id);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }


    public static void createEventAlarm(Context context, List<Event> eventListe){
        int endIndex = 1;
        if(eventListe != null && eventListe.size() > 0) {
            List<String> alarmierteEvents = SettingUtils.getAlarmierteEventIds(context);
            for (Event event:eventListe){
                if(!alarmierteEvents.contains(event.getId())) {
                    createNotification(context, event, endIndex);
                    // TODO: es ist maximal 5 Alarm anzuzeigen... Recihts es aus???
                    if (endIndex > 4) {
                        break;
                    }
                    endIndex++;
                }
            }
            Log.d("EventServiceNotification"," -- alarmierteEvents:" + alarmierteEvents.toString());
        }
        Log.d("EventServiceNotification"," -- createEventAlarm Anzahl:"+endIndex);
    }

}