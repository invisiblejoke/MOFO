package com.example.home.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Home on 4/4/16.
 */
public class bookmarkAsyncTask extends AsyncTask<String, Void, String> {

    public static String name;
    public static String code;
    public static String message;

    String setBookmark = "http://mofo.16mb.com/setBookmark.php";

    @Override
    protected String doInBackground(String... params) {

        String method = params[0];

        Log.i("infohelp: ", "00");

        if (method.equals("bookmark")) {
            try {
                Log.i("infohelp: ", "0");

                URL url = new URL(setBookmark);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                Log.i("infohelp: ", "1");

                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                Log.i("infohelp: ", "2");

                OutputStream outputStream = httpURLConnection.getOutputStream();

                Log.i("infohelp: ", "3");

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                Log.i("infohelp: ", "4");


                String email = User.email;
                String id = params[1];
                String name = params[2];

                Log.i("infohelp: ", "5");

                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")+"&"+
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");

                Log.i("infohelp: ", "6");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                Log.i("infohelp: ", "7");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                Log.i("infohelp: ", "8");


                while ((line = bufferedReader.readLine()) != null) {

                    stringBuilder.append(line + "\n");
                    Log.i("infohelp: ", "9");
                }

                httpURLConnection.disconnect();


                return stringBuilder.toString().trim();


            } catch (MalformedURLException e) {
                Log.i("Error dib", "malformed");

                e.printStackTrace();
                Log.i("Error dib", "malformed end");
            } catch (IOException e) {
                Log.i("Error dib", "IOException");

                e.printStackTrace();
                Log.i("Error dib", "IOException end");
            }

        }

        return null;
    }


    @Override
    protected void onPostExecute(String json) {
        Log.i("infohelp: ", "11");
        try {

            String s = "";
            s += json;
            Log.i("infohelp: ", "12");
            Log.i("infohelp: ", s);
            JSONObject jsonObject = new JSONObject(s);
            Log.i("Json", jsonObject.toString());

            JSONArray srArray = jsonObject.getJSONArray("server_response");
            JSONObject srObject = srArray.getJSONObject(0);
            Log.i("infohelp: ", "13");
            Log.i("infohelp: ",srObject.toString());
            code = srObject.getString("code");
            message = srObject.getString("message");

            //check if login is true
            if (code.equals("true")) {
                Log.i("infohelp: ", "16");

            } else {
                Log.i("infohelp: ", "17");
            }

        } catch (JSONException e1) {
            Log.i("Error ope", "json");
            e1.printStackTrace();
            Log.i("Error ope", "json end");
        }


        Log.i("infohelp: ", "18");
        if (code.equals("true")) {
            Log.i("true",message);


        } else if (code.equals("false")) {
            Log.i("false",message);




        } else {


        }

    }
}


