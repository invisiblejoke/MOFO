package com.example.home.myapplication;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 29/3/16.
 */
public class Bookmark {

    String email;
    String id;
    String name;
    static ArrayList<Bookmark> bookmarkArrayList = new ArrayList<Bookmark>();


    public Bookmark(String id,String name){
        this.email = User.getEmail();
        this.id = id;
        this.name =name;

    }

    public static ArrayList<Bookmark> getBookmarkArrayList() {
        return bookmarkArrayList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public static Bookmark findID (String id){
        for(int i=0; i<bookmarkArrayList.size(); i++){
            if(bookmarkArrayList.get(i).getId().equals(id))
                return bookmarkArrayList.get(i);
        }return null;
    }

    public static void deleteID (String id){
        for(int i=0; i<bookmarkArrayList.size(); i++) {
            if (bookmarkArrayList.get(i).getId().equals(id)) {

                bookmarkAsyncTask task = new bookmarkAsyncTask();
                task.execute("bookmark",bookmarkArrayList.get(i).getId(),bookmarkArrayList.get(i).getName() );

                bookmarkArrayList.remove(i);
            }
        }
    }

    public static String findPosition(int position){
        return bookmarkArrayList.get(position).getId();
    }


}

