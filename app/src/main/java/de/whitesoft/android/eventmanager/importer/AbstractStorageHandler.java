package de.whitesoft.android.eventmanager.importer;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by akyolo on 30.01.2015.
 */
public class AbstractStorageHandler {

    private final String FILE_PREFIX = "event_manager_file_";

    private final String FILE_NAME_SERVICE_LIST = "service_list";

    private final String FILE_NAME_EVENT_LIST = "event_list";
    private Context context;

    public AbstractStorageHandler(Context con){
        this.context = con;
    }

    protected String readJsonData(String fileName) {
        String data = null;
        try {
            FileInputStream stream = this.context.openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer str = new StringBuffer();
            String tmp = "";
            while ((tmp = reader.readLine()) != null) {
                str.append(tmp);
            }
            data = str.toString();
            Log.v("InternalStorageHandler", "queryEventList from File:" + str.toString());
        }catch (FileNotFoundException e){
            Log.i("InternalStorageHandler", "QueryEventList File '"+fileName+"' not found!");
        } catch (IOException e) {
            Log.e("InternalStorageHandler", "QueryEventList Error:" , e);
        }


        return data;
    }

    protected void saveJsonDataLocal(String jsonData, String fileName) {

        try {
            if(jsonData != null && jsonData.length() > 0) {
                FileOutputStream stream = this.context.openFileOutput(fileName, Context.MODE_PRIVATE);
                stream.write(jsonData.getBytes());
                stream.close();

                Log.v("InternalStorageHandler", "EventService als Datei gespeichert '" + fileName + "'.");
            }

        } catch (FileNotFoundException e) {
            Log.e("InternalStorageHandler", "FileNotFoundException:" , e);
        } catch (IOException e) {
            Log.e("InternalStorageHandler", "IOException:" , e);
        }
    }

    protected String getServiceListFileName(){
        return FILE_PREFIX + "_" + FILE_NAME_SERVICE_LIST;
    }

    protected String getEventManagerFileName(){
        return FILE_PREFIX + "_" + FILE_NAME_EVENT_LIST;
    }

    protected Context getContext() {
        return context;
    }

    protected void setContext(Context context) {
        this.context = context;
    }
}
