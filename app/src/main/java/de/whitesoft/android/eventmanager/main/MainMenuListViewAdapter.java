package de.whitesoft.android.eventmanager.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.whitesoft.android.eventmanager.R;

/**
 * Created by akyolo on 24.01.2015.
 */
public class MainMenuListViewAdapter extends ArrayAdapter<MainMenu> {

    private List<MainMenu> menuList;

    public MainMenuListViewAdapter(Context context, List<MainMenu> objects) {
        super(context, R.layout.layout_main_menu_list_item, objects);
        this.menuList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MenuViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.layout_main_menu_list_item, parent, false);

            viewHolder = new MenuViewHolder();
            ImageView image = (ImageView) convertView.findViewById(R.id.mainMenuImage);
            TextView title = (TextView) convertView.findViewById(R.id.mainMenuTitle);
            viewHolder.image = image;
            viewHolder.title = title;
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (MenuViewHolder) convertView.getTag();
        }


        // Set Data
        MainMenu menu = getItem(position);
        //viewHolder.image.setImageResource(R.drawable.example_picture);
        viewHolder.title.setText(menu.getTitle());
        return convertView;
    }

    @Override
    public MainMenu getItem(int position) {
       MainMenu menu = menuList.get(position);
        return  menu;
    }
}
