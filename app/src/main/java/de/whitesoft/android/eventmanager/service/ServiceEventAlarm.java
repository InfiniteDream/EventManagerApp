package de.whitesoft.android.eventmanager.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.util.List;

import de.whitesoft.android.eventmanager.importer.InternalStorageHandler;
import de.whitesoft.android.eventmanager.model.Event;
import de.whitesoft.android.eventmanager.model.EventListFilter;
import de.whitesoft.android.eventmanager.utils.SettingUtils;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServiceEventAlarm extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "de.whitesoft.android.eventmanager.service.action.FOO";
    private static final String ACTION_BAZ = "de.whitesoft.android.eventmanager.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "de.whitesoft.android.eventmanager.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "de.whitesoft.android.eventmanager.service.extra.PARAM2";



    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ServiceEventAlarm.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public ServiceEventAlarm() {
        super("ServiceEventAlarm");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.v("ServiceEventAlarm","-- onHandleIntent startet...");

            //final String action = intent.getAction();
            //final String param1 = intent.getStringExtra(EXTRA_PARAM1);
            // Event Alarm
            // TODO: Filter definieren???
            EventListFilter filter = SettingUtils.getEventFilterFromSettings(getApplicationContext());
            List<Event> events = InternalStorageHandler.filterEvents(getApplicationContext(), filter);
            EventServiceNotification.createEventAlarm(getBaseContext(), events);

        }
    }

}
