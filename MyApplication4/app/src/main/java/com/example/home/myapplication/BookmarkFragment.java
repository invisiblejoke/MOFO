package com.example.home.myapplication;

import java.util.ArrayList;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.ListView;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.content.Intent;

public class BookmarkFragment extends Fragment implements AdapterView.OnItemClickListener {
    ListView listview;
    static int bookmarkPosition;

    public BookmarkFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //update userSetting
        settingAsyncTask task1 = new settingAsyncTask();
        task1.execute();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //*****give null exception when wrong fragment
        View rootView = inflater.inflate(R.layout.fragment_bookmark, container, false);
        ArrayList<Bookmark> arrayOfBookmark = Bookmark.getBookmarkArrayList();
        BookmarkAdapter adapter = new BookmarkAdapter(getContext(), arrayOfBookmark);

       // View rootView2 = inflater.inflate(R.layout.fragment_setting, container, false);
        listview = (ListView) rootView.findViewById(R.id.listView);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(this);

        // Inflate the layout for this fragment
       return rootView;
    //return inflater.inflate(R.layout.fragment_blank, container, false);


    }

    @Override
    public void onResume(){
        super.onResume();
        listview.invalidateViews();

    }



    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
        // Then you start a new Activity via Intent
        bookmarkPosition = position;

        retrieveData.callfrom="BookmarkFragment";
        Intent intent = new Intent();
        intent.setClass(getActivity(), InformationActivity.class);
        intent.putExtra("unique_page", "BookmarkFragment");


        //show where its coming from
        intent.putExtra("position", position);
        intent.putExtra("id", id);


        BookmarkFragment.this.startActivity(intent);
        retrieveData task = new retrieveData();
        task.execute("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + Bookmark.findPosition(bookmarkPosition) + "&key=AIzaSyDQG21ZWu1Sojx6PtZjy_boIJQobaERVHo");



    }


    public static int getPosition(){
        return bookmarkPosition;
    }



}