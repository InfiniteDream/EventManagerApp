package de.whitesoft.android.eventmanager.service;

import android.app.Activity;
import android.os.Bundle;

import de.whitesoft.android.eventmanager.R;

public class NotificationReceiverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_receiver);
    }
}
