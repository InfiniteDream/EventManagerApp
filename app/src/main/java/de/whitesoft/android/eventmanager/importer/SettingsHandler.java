package de.whitesoft.android.eventmanager.importer;

import java.util.List;

import de.whitesoft.android.eventmanager.model.Event;
import de.whitesoft.android.eventmanager.model.Service;
import de.whitesoft.android.eventmanager.model.Settings;

/**
 * Created by akyolo on 02.02.2015.
 */
public interface SettingsHandler {

    public List<String> getSelectedServices();

    public void setSelectedService(Service service, boolean selected);

    public void setAppSettings(Settings settings);

    public Settings getAppSettings();

    public void setEventAlarmiert(Event event);

    public List<String> getAlarmierteEventIds();

}
