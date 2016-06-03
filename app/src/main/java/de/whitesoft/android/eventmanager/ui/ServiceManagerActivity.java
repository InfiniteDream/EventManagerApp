package de.whitesoft.android.eventmanager.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import de.whitesoft.android.eventmanager.R;

/**
 * Created by akyolo on 23.01.2015.
 */
public class ServiceManagerActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings_service);


        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
