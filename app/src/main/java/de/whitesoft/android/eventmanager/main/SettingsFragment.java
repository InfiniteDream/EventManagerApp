package de.whitesoft.android.eventmanager.main;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import de.whitesoft.android.eventmanager.R;
import de.whitesoft.android.eventmanager.ui.SettingsServicePreferenceFragment;

import static de.whitesoft.android.eventmanager.R.id.fragment_settings_service_preference;

/**
 * Created by akyolo on 24.01.2015.
 */
public class SettingsFragment extends Fragment{

    private static volatile SettingsFragment instance;

    public static SettingsFragment newInstance(){
        if (instance == null){
            instance = new SettingsFragment();
        }
        return instance;
    }

    private static View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        if(view == null) {
            view = inflater.inflate(R.layout.layout_settings, container, false);
        }
        return view;
    }
}
