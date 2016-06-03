package de.whitesoft.android.eventmanager.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.whitesoft.android.eventmanager.R;
import de.whitesoft.android.eventmanager.model.Event;
import de.whitesoft.android.eventmanager.model.EventManager;
import de.whitesoft.android.eventmanager.model.Service;
import de.whitesoft.android.eventmanager.utils.ManagerUtils;
import de.whitesoft.android.eventmanager.utils.SettingUtils;

/**
 * Created by akyolo on 01.01.2015.
 */
public class ServiceListViewAdapter extends ArrayAdapter<Service>{

    private List<Service> managers;

    public ServiceListViewAdapter(Context context, List<Service> objects) {
        super(context, R.layout.layout_service_list_item, objects);
        this.managers = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ServiceViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.layout_service_list_item, parent, false);
            viewHolder = new ServiceViewHolder();
            ImageView image = (ImageView) convertView.findViewById(R.id.serviceImage);
            TextView text1 = (TextView) convertView.findViewById(R.id.serviceTitle);
            TextView text2 = (TextView) convertView.findViewById(R.id.serviceDescription);
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxService);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ServiceViewHolder view = (ServiceViewHolder) buttonView.getTag();
                    Log.v(" onItemClick ", " isChecked: " + isChecked + ", serviceId:"+view.service.getId());
                    if(isChecked != view.service.isSelected()) {
                        SettingUtils.setServiceState(getContext(), view.service, isChecked);
                    }
                }
            });
            checkBox.setTag(viewHolder);
            viewHolder.serviceImage = image;
            viewHolder.serviceTitle = text1;
            viewHolder.serviceDescription = text2;
            viewHolder.serviceCheckBox = checkBox;
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ServiceViewHolder) convertView.getTag();
        }


        // Set Data
        Service manager = getItem(position);
        viewHolder.serviceImage.setImageResource(R.drawable.example_picture);
        viewHolder.serviceTitle.setText(manager.getName());
        viewHolder.serviceDescription.setText(manager.getDescription());
        viewHolder.service = manager;
        viewHolder.serviceCheckBox.setChecked(manager.isSelected());
        return convertView;
    }

    @Override
    public Service getItem(int position) {
        Service event = super.getItem(position);
        event = managers.get(position);
        return event;
    }

    public void updateView(List<Service> managers){
        this.managers = managers;
        this.notifyDataSetChanged();
        if(managers != null) {
            Log.v("ServiceListViewAdapter", " updateView EventManager:" + managers.size());
        }
    }
}
