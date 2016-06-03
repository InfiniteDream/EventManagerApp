package de.whitesoft.android.eventmanager.importer;

import android.content.Context;
import android.util.Log;

import de.whitesoft.android.eventmanager.model.EventListFilter;
import de.whitesoft.android.eventmanager.utils.SettingUtils;

/**
 * Created by akyolo on 30.12.2014.
 */
public class AsyncTaskUpdateServiceList  extends AbstractAsyncTask{

    public static final String ACTION_NAME = "AsyncTaskUpdateServiceList";

    public AsyncTaskUpdateServiceList(Context con) {
        super(con);
    }


    @Override
    protected String doInBackground(String... params) {
        String response = execute(params[0]);
        Log.v("AsyncTaskUpdateServiceList" , " response -- > '" + response + "'");
        return response;
    }

    @Override
    protected void onPostExecute(String response) {

        // TODO Filter??
        EventListFilter filter = SettingUtils.getEventFilterFromSettings(getContext());

        StorageHandlerServiceList handler = new StorageHandlerServiceList(getContext());
        handler.saveJsonDataLocal(response);

        // TODO: Impl fehlt..
        // Event Update Callback
        //Intent intent = new Intent(ACTION_NAME);
        //intent.putExtra("callback", true);
        //getContext().sendBroadcast(intent);

    }




}
