package de.whitesoft.android.eventmanager.importer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.whitesoft.android.eventmanager.model.EventListFilter;
import de.whitesoft.android.eventmanager.utils.ManagerUtils;
import de.whitesoft.android.eventmanager.utils.SettingUtils;

/**
 * Created by akyolo on 30.12.2014.
 */
public class AsyncTaskUpdateEventList extends AbstractAsyncTask {




    public AsyncTaskUpdateEventList(Context con) {
        super(con);
    }


    @Override
    protected String doInBackground(String... params) {
        String response = execute(params[0]);
        Log.v("UpdateDatabaseAsyncTask" , " response -- > '" + response + "'");
        return response;
    }

    @Override
    protected void onPostExecute(String response) {

        // TODO Filter??
        EventListFilter filter = SettingUtils.getEventFilterFromSettings(getContext());

        InternalStorageHandler handler = new InternalStorageHandler(getContext());
        handler.saveJsonDataLocal(filter, response);

        // Event Update Callback
        ManagerUtils.sendCallbackForUpdateEventList(getContext());

        // Alarm Service prüfen
        ManagerUtils.startAlarmCheck(getContext());
    }

}
