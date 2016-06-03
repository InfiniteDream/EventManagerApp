package de.whitesoft.android.eventmanager.importer;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.whitesoft.android.eventmanager.model.Event;
import de.whitesoft.android.eventmanager.model.EventListFilter;
import de.whitesoft.android.eventmanager.model.EventManager;
import de.whitesoft.android.eventmanager.model.EventModel;
import de.whitesoft.android.eventmanager.model.Service;

/**
 * Created by akyolo on 08.01.2015.
 */
public class StorageHandlerServiceList extends AbstractStorageHandler{



    public StorageHandlerServiceList(Context con) {
        super(con);
    }

    public void saveJsonDataLocal(String jsonData) {
        String fileName = getServiceListFileName();
        saveJsonDataLocal(jsonData,fileName);
    }

    public List<Service> getServiceListFromFile(){
        // Caching
        String jsonData = readJsonData(getServiceListFileName());
        List<Service> services = parseJsonDataFromStorage(jsonData);
        return  services;
    }

    private List<Service> parseJsonDataFromStorage(String jsonData) {
        List<Service> liste = new ArrayList<>();
        if(jsonData != null && jsonData.length() > 0) {
            try {
                JSONArray serviceListe = new JSONArray(jsonData);
                for (int s = 0; s < serviceListe.length(); s++) {
                    JSONObject json = (JSONObject) serviceListe.get(s);
                    String serviceId = json.getString("serviceId");
                    String serviceName = json.getString("serviceName");
                    String serviceDesc = json.getString("serviceDesc");

                    Service service = new Service();
                    service.setId(serviceId);
                    service.setName(serviceName);
                    service.setDescription(serviceDesc);
                    Log.v("StorageHandlerServiceList", "serviceId: " + serviceId + ", serviceName: " + serviceName + ", serviceDesc: " + serviceDesc);

                    liste.add(service);
                }
                Log.v("StorageHandlerServiceList", " From Server Manager Found:" + liste.size());
            } catch (JSONException e) {
                Log.e("StorageHandlerServiceList", "Fehler beim Parsen der JSON Daten!", e);
            }
        }
        return liste;
    }




}
