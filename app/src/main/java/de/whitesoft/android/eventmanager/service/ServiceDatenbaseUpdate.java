package de.whitesoft.android.eventmanager.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import de.whitesoft.android.eventmanager.importer.AsyncTaskUpdateEventList;
import de.whitesoft.android.eventmanager.importer.AsyncTaskUpdateServiceList;
import de.whitesoft.android.eventmanager.utils.SettingUtils;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServiceDatenbaseUpdate extends IntentService {


    public ServiceDatenbaseUpdate() {
        super("ServiceDatenbaseUpdate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.v("ServiceDatenbaseUpdate", " onHandleIntent startet...");
            // Data
            if(isConnected()){
                //  http://localhost:8080/event/list?id=1&event=garbagecalendar
                // für Emulator : http://10.0.2.2 wird intern auf localhost gemappt (weil localhost ist von android belegt)
                // TODO: Qeury pro EventManager == serviceId ???
                String url = SettingUtils.getCloudMethodQuery();
                new AsyncTaskUpdateEventList(getApplicationContext()).execute(url);

                // TODO: Stimme ab ob Zeitpunkt wenn die Service Liste aktualisiert wird
                String urlServiceList = SettingUtils.getCloudMethodServiceList();
                new AsyncTaskUpdateServiceList(getApplicationContext()).execute(urlServiceList);
            }else{
                // TODO ???
            }
        }
    }

    private boolean isConnected(){
        boolean connected = false;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if(info != null && info.isConnected()){
            connected = true;
        }
        return connected;
    }


}
