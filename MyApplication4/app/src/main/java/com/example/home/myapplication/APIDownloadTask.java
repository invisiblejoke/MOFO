package com.example.home.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class APIDownloadTask extends AsyncTask<String, Void, String> {
    public static List<String> place_idArray = new ArrayList<String>();


    @Override
    protected String doInBackground(String... urls) {
        Log.i("api", "3 ");
        String result = "";
        URL url;
        HttpURLConnection urlConnection;
        Log.i("api", "4 ");
        try {
            url = new URL(urls[0]);
            Log.i("api", "5 ");
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

        try {
            Log.i("api", "10 ");
            JSONObject jsonObject = new JSONObject(result);
            Log.i("api", "11 ");


            String results = jsonObject.getString("results");

            Log.i("api", "12 ");
            System.out.println("result" + results);
            Log.i("api", "13 ");
            JSONArray arr = new JSONArray(results);
            Log.i("api", "14 ");

            //check aar length
            Log.i("api arr length", String.valueOf(arr.length()));

            //check result length
            Log.i("api result length", String.valueOf(result.length()));



            for (int i = 0; i < arr.length(); i++) {

                String place_id = arr.getJSONObject(i).getString("place_id");
                place_idArray.add(place_id);
                System.out.println( place_idArray);

            }

            //if there is data returned
            if (arr.length() != 0)
            {
                retrieveData task2 = new retrieveData();
                int x = 0;

                String place_id = place_idArray.get(x);
                x++;
                String link = ("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&key=AIzaSyDQG21ZWu1Sojx6PtZjy_boIJQobaERVHo");
                Log.i("api", link);
                task2.execute(link);

                //allow button to be press
                HomeFragment.enableButton();

            }else{

                //disable all button if there is no data
                HomeFragment.disableButton();



            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("api print",e.toString());
            Log.i("api", "17 ");
        }

    }


    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

}
