package com.example.home.myapplication;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Home on 31/3/16.
 */
public class BookmarkAdapter extends ArrayAdapter<Bookmark>  {

    public BookmarkAdapter(Context context, ArrayList<Bookmark> bookmarkArrayList) {
        super(context, 0, bookmarkArrayList);
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Bookmark bookmark = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bookmarkadapter, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvBookmarkName);

        // Populate the data into the template view using the data object
        tvName.setText(bookmark.name);

        // Return the completed view to render on screen
        return convertView;
    }



}
