package com.example.home.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    String radius, keyword;
    static TextView tvName, tvLikeCount, tvShortInfo,tvLike;
    static ImageView ivPic,ivLikePic;
    static Button bBookmark, bNextPost, bMoreInfo;
    int currentPlaceId = 1;


    //initialize for the location
    LocationManager locationManager;
    LocationListener locationListener;
    Location locations;
    String provider,coordinate;
    double lat;
    double lng;

    AlertDialog.Builder builder;

    boolean alreadyExceuted = false;
    boolean GPSalreadyExceuted = false;
    public static Context staticContext;
    static boolean dataRetrieved = false;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //to maek the context callable in static method
        staticContext = getContext();

        radius = SettingFragment.getRadius();
        keyword = SettingFragment.getKeyword();

        //let retrieve data know method is calling from home fragment
        retrieveData.callfrom = "HomeFragment";

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //best provider,either gps or internet
        provider = locationManager.getBestProvider(new Criteria(), false);

        //get the last known location
        locations = locationManager.getLastKnownLocation(provider);

        //check location
        if (locations != null) {
            Log.i("location info", "achieve");
        } else {
            Log.i("location info", "no location");
        }

        //listen to location change
        locationListener = new LocationListener() {

            //on changes, display location
            @Override
            public void onLocationChanged(Location location) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locations = locationManager.getLastKnownLocation(provider);
                displayLocation();

                //if findFood method not ran before
                if(!alreadyExceuted){
                    findFood();
                }

                //rerun findFood upon location change if no data returned previously
                if(!dataRetrieved){
                    findFood();
                }

                Log.i("info ", "onlocationchange");
            }

            //from no location to have or vise versa, display location
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

                //check permission, required by system
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                //get last known location by the system
                locations = locationManager.getLastKnownLocation(provider);

                //log it
                displayLocation();

                Log.i("info ", "onstatuschange");

                //check if its run.
                if(!alreadyExceuted){
                    findFood();
                }

            }

            //when GPS is enabled
            @Override
            public void onProviderEnabled(String provider) {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                //check if its enable
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    //check permission, required by system
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    //update the location base on GPS
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Log.i("info", "onProvideGPS");

                    //display msg if no location available,else check if findFood method is ran
                    if (!checkGPS()){
                        Toast.makeText(getContext(), "GPS location not available, please try again later.", Toast.LENGTH_LONG).show();
                    }else{
                        if(!alreadyExceuted)
                            findFood();
                    }

                } else {

                    //update the location base on network
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    Log.i("info", "onProvideNETWORK");

                    //check if its run
                    if(!alreadyExceuted){
                        findFood();
                    }

                }
            }

            // GPS disable
            @Override
            public void onProviderDisabled(String provider) {
                //bring user to GPS setting page
                if(!GPSalreadyExceuted) {
                    toGPS();
                }
            }

        };

        //check permission, required by system
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET}, 10);
            }
            return;
        }
        //if GPS is enable before app start
        else
        {
            //update location
            locationManager.requestLocationUpdates("gps", 400, 1, locationListener);

            //if location dont exist
            if(!checkGPS()){
                Toast.makeText(getContext(), "GPS location not available, please try again later.", Toast.LENGTH_LONG).show();
            }
            Log.i("info ", "smtg");
        }

        //initialize the find food process
        findFood();
    }

    //find the food
    private void findFood(){

        //if location exist
        if (checkGPS()) {

            //state the method ran.
            alreadyExceuted = true;

            //find the coordinate
            displayLocation();
            coordinate = Double.toString(lat)+","+Double.toString(lng);


            //find the foodplace based on userSetting
            APIDownloadTask task = new APIDownloadTask();
            //String register_url = "https://maps.googleapis.com/maps/api/place/radarsearch/json?location=3.152209,101.671317&radius=" + radius + "&keyword=" + keyword + "&type=food&key=AIzaSyDQG21ZWu1Sojx6PtZjy_boIJQobaERVHo";
            String register_url = "https://maps.googleapis.com/maps/api/place/radarsearch/json?location="+coordinate +"&radius=" + radius + "&keyword=" + keyword + "&type=food&key=AIzaSyDQG21ZWu1Sojx6PtZjy_boIJQobaERVHo";
            Log.i("map", register_url);
            task.execute(register_url);


            //update userSetting
            settingAsyncTask task1 = new settingAsyncTask();
            task1.execute();

            //show text to ask user wait
            Toast.makeText(getContext(), "Please wait while we find you some food place =)", Toast.LENGTH_LONG).show();
        }
    }


    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        tvLike = (TextView) rootView.findViewById(R.id.tvLike);
        tvLikeCount = (TextView) rootView.findViewById(R.id.tvLikeCount);
        tvShortInfo = (TextView) rootView.findViewById(R.id.tvShortInfo);
        ivPic = (ImageView) rootView.findViewById(R.id.ivPic);
        ivLikePic = (ImageView)rootView.findViewById(R.id.ivLikePic);
        bBookmark = (Button) rootView.findViewById(R.id.bBookmark);
        bNextPost = (Button) rootView.findViewById(R.id.bNextPost);
        bMoreInfo = (Button) rootView.findViewById(R.id.bMoreInfo);

        ivLikePic.setImageResource(R.drawable.unlike);

        bBookmark.setEnabled(false);
        bMoreInfo.setEnabled(false);
        bNextPost.setEnabled(false);
        tvLike.setEnabled(false);
        tvLikeCount.setEnabled(false);
        ivLikePic.setEnabled(false);


       // tvLikeCount.setEnabled(true);
        tvLikeCount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateLike();
            }
        });

        tvLike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateLike();
            }
        });

        ivLikePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLike();
            }
        });


        //bookmark button
        bBookmark.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Bookmark.findID(retrieveData.place_id) == null) {
                    Bookmark.bookmarkArrayList.add(new Bookmark(retrieveData.place_id, retrieveData.title));
                    bookmarkAsyncTask task = new bookmarkAsyncTask();
                    task.execute("bookmark", retrieveData.place_id, retrieveData.title);
                    Toast.makeText(getContext(), "Bookmarked!", Toast.LENGTH_SHORT).show();
                    HomeFragment.bBookmark.setText("Unbookmark");

                } else {
                    Bookmark.deleteID(retrieveData.place_id);
                    Toast.makeText(getContext(), "Bookmark deleted", Toast.LENGTH_SHORT).show();
                    HomeFragment.bBookmark.setText("Bookmark");
                }
            }
        });





        //next post button
        bNextPost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //intialize task
                retrieveData task2 = new retrieveData();

                //compare current id index to the id size
                //if same, go back to start
                if (currentPlaceId == APIDownloadTask.place_idArray.size()) {
                    currentPlaceId = 0;
                    Toast.makeText(getContext(), "mo more new data. going back to the start", Toast.LENGTH_LONG).show();
                }
                String place_id = APIDownloadTask.place_idArray.get(currentPlaceId);
                currentPlaceId++;
                String link = ("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&key=AIzaSyDQG21ZWu1Sojx6PtZjy_boIJQobaERVHo");
                Log.i("map", link);
                task2.execute(link);
            }
        });

        //more info button
        bMoreInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), InformationActivity.class);

                //set the class intent to HomeFragment
                intent.putExtra("unique_page", "HomeFragment");
                HomeFragment.this.startActivity(intent);

                //set the method calling to InformationActivity
                retrieveData.callfrom = "InformationActivity";


            }
        });

        return rootView;
    }


    public static void changeTextName(String s){
        tvName.setText(s);
    }

    public static void changeTextLikeCount(String s){
        tvLikeCount.setText(s);
    }

    public static void changeTextShortInfo(String s){
        tvShortInfo.setText(s);
    }

    //method to enable all button
    public static void enableButton(){
        bBookmark.setEnabled(true);
        bMoreInfo.setEnabled(true);
        bNextPost.setEnabled(true);
        tvLike.setEnabled(true);
        tvLikeCount.setEnabled(true);
        ivLikePic.setEnabled(true);
        dataRetrieved = true;
    }

    //method to disable all button
    public static void disableButton(){
        bBookmark.setEnabled(false);
        bMoreInfo.setEnabled(false);
        bNextPost.setEnabled(false);
        ivLikePic.setEnabled(false);
        tvLikeCount.setEnabled(false);
        tvLike.setEnabled(false);

        Toast.makeText(staticContext, "No food place found. Please reset your setting.", Toast.LENGTH_LONG).show();
        dataRetrieved = false;

    }

    //update like for the current post
    public void updateLike(){
        retrievePostLike taskPostLike = new retrievePostLike();
        taskPostLike.execute("setPostLike", retrieveData.place_id);

    }

    //check if GPS coordinate is available
    public Boolean checkGPS() {
        if (locations != null) {
            return true;
        }
        return false;
    }

    //log the coordination
    public void displayLocation() {

        if (locations != null) {

            lat = locations.getLatitude();
            lng = locations.getLongitude();

            Log.i("info lng", Double.toString(lng));
            Log.i("info lat", Double.toString(lat));
        }
        else{
            Log.i("info lng", "not available");
        }
    }





    //display alert box and bring user tyo turn on GPS
    public void toGPS(){
        GPSalreadyExceuted=true;
        //display a alert box that bring user to turn on GPS
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("GPS Disabled");
        builder.setMessage("Please turn on the GPS to allow us to locate you");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            //click ok bring user to turn GPS on
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



}



