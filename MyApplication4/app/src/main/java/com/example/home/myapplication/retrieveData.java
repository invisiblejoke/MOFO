package com.example.home.myapplication;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class retrieveData extends AsyncTask<String, Void, String> {
    public static List<String> currentPost = new ArrayList<String>();
    public static String title, open_now, place_id, address, phoneNo, category, user_ratings_total, website, permanently_closed, photo_reference;
    public static String callfrom="";
    static Bitmap resImage=null;

    @Override
    protected String doInBackground(String... urls) {
        Log.i("logg", "3 ");
        String result = "";
        URL url;
        HttpURLConnection urlConnection;
        Log.i("logg", "4 ");
        try {
            url = new URL(urls[0]);
            Log.i("logg", "5 ");
            urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

/**
 if(response == null) {
 response = "THERE WAS AN ERROR";
 }
 System.out.println(response);
 **/
        try {

            JSONObject jObj = new JSONObject(result);
            JSONObject response = jObj.getJSONObject("result");
            Log.i("logg", "10 ");
            place_id = response.getString("place_id");
            Log.i("logg:place id",place_id);

            title = response.getString("name");
            Log.i("logg:title", title);

            if (response.has("open_now")){
                 open_now = response.getString("open_now");
                Log.i("logg:open_now",open_now);
            }else{
                open_now ="not available";
            }

            if (response.has("formatted_address")){
            address = response.getString("formatted_address");
            }else{
                address ="not available";
            }

            if (response.has("formatted_phone_number")){
                phoneNo = response.getString("formatted_phone_number");
            }else{
                phoneNo ="not available";
            }

            if (response.has("types") ){
                category = response.getString("types");
                Log.i("logg:category",category);
            }else{
                category ="not available";
            }

            if (response.has("user_ratings_total") ){
                user_ratings_total = response.getString("user_ratings_total");
                Log.i("logg:user_ratings_total",user_ratings_total);
            }else{
                user_ratings_total ="not available";
            }
            if (response.has("website") ){
                website = response.getString("website");
                Log.i("logg:website",website);
            }else{
                website ="not available";
            }


            if (response.has("permanent_closed")){
                permanently_closed = response.getString("permanently_closed");
                Log.i("logg:permamently_closed",permanently_closed);
                if (permanently_closed.equals(true))
                    permanently_closed ="not available";
            }else{
               permanently_closed ="not available";
            }


            if (response.has("photos")){
                String photo_reference_json = response.getString("photos");

                //take out the [],leave {content}
                JSONArray photoJSON = new JSONArray(photo_reference_json);

                //loop and find position of the photo_reference
                for (int i = 0; i <photoJSON.length(); i++) {
                    photo_reference = photoJSON.getJSONObject(i).getString("photo_reference");
                    Log.i("logg:photo_reference", photo_reference);
                }
            }else{
                photo_reference="";
                resImage=null;
            }
/**
            Log.i("logg:", "smtg1");
            HomeFragment.changeTextLikeCount(user_ratings_total);
            HomeFragment.changeTextName(title);
            Log.i("logg:", "smtg2");
            if(open!="") {
                HomeFragment.changeTextShShortInfo(open);
            }else {
                HomeFragment.changeTextShortInfo(phoneNo);
            }
            Log.i("logg:", "smtg3");

            **/
        if(callfrom.equals("HomeFragment")) {

            retrievePostLike taskPostLike = new retrievePostLike();
            taskPostLike.execute("getPostLike",place_id);

            HomeFragment.tvName.setText(title);
            Log.i("logg:", "smtg1");
            HomeFragment.tvLikeCount.setText(user_ratings_total);
            Log.i("logg:", "smtg1");
            if (open_now != "not available") {
                HomeFragment.tvShortInfo.setText("Status:"+open_now);
            } else {
                HomeFragment.tvShortInfo.setText("Phone No: "+phoneNo);
            }

            //if photo exist
            if (photo_reference != "") {
                retrieveImage task = new retrieveImage();

                try {
                    resImage = task.execute("https://maps.googleapis.com/maps/api/place/photo?maxheight=600&photoreference=" + photo_reference + "&key=AIzaSyDQG21ZWu1Sojx6PtZjy_boIJQobaERVHo").get();
                    HomeFragment.ivPic.setImageBitmap(resImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                 HomeFragment.ivPic.setImageResource(R.drawable.no_image_thumb);
            }

            //check and see if its bookmarked
            if(Bookmark.findID(place_id)!=null){
                HomeFragment.bBookmark.setText("Unbookmark");
            }else{
                HomeFragment.bBookmark.setText("Bookmark");
            }

        }else if(callfrom.equals("BookmarkFragment")){

            InformationActivity.tvInfoName.setText(title);
            InformationActivity.tvInfoLike.setText(retrievePostLike.total_like);
            InformationActivity.tvInfoAdd.setText(address);
            InformationActivity.tvInfoPhone.setText(phoneNo);
            InformationActivity.tvInfoWebsite.setText(website);

            if (photo_reference != "") {
                retrieveImage task = new retrieveImage();
                try {
                    resImage = task.execute("https://maps.googleapis.com/maps/api/place/photo?maxheight=500&photoreference=" + photo_reference + "&key=AIzaSyDQG21ZWu1Sojx6PtZjy_boIJQobaERVHo").get();
                    InformationActivity.ivInfoPic.setImageBitmap(resImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                InformationActivity.ivInfoPic.setImageResource(R.drawable.no_image_thumb);
                }

            retrievePostLike taskPostLike = new retrievePostLike();
            taskPostLike.execute("getPostLike", place_id);

            callfrom="InformationActivity";
        }

            //how to retrive photo link
            //https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference= PHOTOREF &key=YOUR_API_KEY


            //JSONArray arr = response.getJSONArray("result");
            Log.i("logg", "11 ");
            Log.i("Logg",place_id);


        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("logg print",e.toString());
            Log.i("logg", "17 ");
        }
    }

}