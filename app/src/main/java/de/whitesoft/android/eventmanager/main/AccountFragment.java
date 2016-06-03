package de.whitesoft.android.eventmanager.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.whitesoft.android.eventmanager.R;

/**
 * Created by akyolo on 24.01.2015.
 */
public class AccountFragment extends Fragment{

    private static volatile AccountFragment instance;

    public static AccountFragment newInstance(){
        if (instance == null){
            instance = new AccountFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_login, container, false);
    }
}
