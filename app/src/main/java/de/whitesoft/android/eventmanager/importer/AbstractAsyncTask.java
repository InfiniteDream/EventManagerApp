package de.whitesoft.android.eventmanager.importer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.whitesoft.android.eventmanager.utils.SettingUtils;

/**
 * Created by akyolo on 30.01.2015.
 */
public abstract class AbstractAsyncTask extends AsyncTask<String, Void, String> {

    private Context context;

    public AbstractAsyncTask(Context con) {
        super();
        this.context = con;
    }

    protected static String execute(String url){

        InputStream stream = null;
        String message = "";
        try {

            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, SettingUtils.SERVICE_CONN_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, SettingUtils.SERVICE_CONN_TIMEOUT);

            HttpClient client = new DefaultHttpClient(params);
            HttpGet request = new HttpGet(url);
            request.setParams(params);
            HttpResponse response = client.execute(request);
            stream = response.getEntity().getContent();
            if(stream != null){
                message = convertStreamToMessage(stream);
            }
        } catch (IOException e) {
            if(e instanceof ConnectTimeoutException){
                Log.w("AsyncTaskUpdateServiceList", "ConnectTimeoutException for Server URL '" + url + "'");
            }else{
                Log.e("AsyncTaskUpdateServiceList", "Exception for Server URL '"+url +"'", e);
            }
        }

        return message;
    }

    private static String convertStreamToMessage(InputStream stream){

        String line = "";
        String message = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            while((line = reader.readLine()) != null){
                message += line;
            }

            stream.close();
        } catch (IOException e) {
            Log.e("AsyncTaskUpdateServiceList", "Exception convertStreamToMessage", e);
        }
        return message;
    }

    protected Context getContext() {
        return context;
    }

    protected void setContext(Context context) {
        this.context = context;
    }



}
