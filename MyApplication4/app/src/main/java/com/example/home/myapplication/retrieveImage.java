package com.example.home.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Home on 2/4/16.
 */
public class retrieveImage extends AsyncTask<String, Void, Bitmap> {


    @Override
    protected Bitmap doInBackground(String... urls) {

        try {

            URL url = new URL(urls[0]);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.connect();

            InputStream inputStream = connection.getInputStream();

            Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

            return myBitmap;


        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return null;
    }
}




