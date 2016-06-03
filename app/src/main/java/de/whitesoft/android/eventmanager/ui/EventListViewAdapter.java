package de.whitesoft.android.eventmanager.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import de.whitesoft.android.eventmanager.R;
import de.whitesoft.android.eventmanager.model.Event;
import de.whitesoft.android.eventmanager.utils.ManagerUtils;

/**
 * Created by akyolo on 01.01.2015.
 */
public class EventListViewAdapter extends ArrayAdapter<Event>{

    private List<Event> events;

    public EventListViewAdapter(Context context, List<Event> objects) {
        super(context, R.layout.layout_event_list_item, objects);
        this.events = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EventViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.layout_event_list_item, parent, false);

            viewHolder = new EventViewHolder();
            ImageView image = (ImageView) convertView.findViewById(R.id.eventImage);
            TextView text1 = (TextView) convertView.findViewById(R.id.eventTitle);
            TextView text2 = (TextView) convertView.findViewById(R.id.eventDescription);
            viewHolder.eventImage = image;
            viewHolder.eventTitle = text1;
            viewHolder.eventDescription = text2;
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (EventViewHolder) convertView.getTag();
        }


        // Set Data
        Event event = getItem(position);
        viewHolder.eventImage.setImageResource(R.drawable.example_picture);
        viewHolder.eventTitle.setText(event.getTitle());
        viewHolder.eventDescription.setText(ManagerUtils.convertDate(event.getDate()));

        return convertView;
    }

    @Override
    public Event getItem(int position) {
        Event event = events.get(position);
        return event;
    }

    public void updateView(List<Event> events){
        this.events = events;
        this.notifyDataSetChanged();
        if(events != null) {
            Log.v("EventListViewAdapter", " updateEvents:" + events.size());
        }
    }
}
