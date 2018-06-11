package com.example.home.myapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HomeFragment on 21/3/16.
 */
public class User {
    static String username, email;
    static boolean login=false;
    static int distance;
    static ArrayList<String>bookmark= new ArrayList<>() ;
    static String userSetting="";
    static boolean initiateSetting =false;

    public User (String username, String email){
        this.username = username;
        this.email = email;
        this.login = true;
        ;
    }

    public static String getUsername(){
        return username;
    }

    public static String getEmail(){
        return  email;
    }

    public static Boolean getLogin(){
        return login;
    }

    public static void setLogin(boolean tf){
        login=tf;
    }

    public static void setDistance(int i){
        distance = i;
    }

    public static int getDistance() {
        return distance;
    }

    public static List<String> getBookmark() {
        return bookmark;
    }

    public void setBookmark(Bookmark bookmark){
    }

    public static  List<String> getBookmarkName(){
//
        for (int i =0; i<bookmark.size(); i++){
     //       bookmark
        }
        return null;
    }


    /**
     *
     * @return userSetting for food choice preference
     */
    public static String getUserSetting(){
        return userSetting;
    }

    /**
     *
     * @param us the String of user food choice preference
     */
    public static void setUserSetting(String us){
        userSetting = us;
    }

    /**
     * use to stop data to read back initial setting
     * @param tf boolean true of false
     * @return
     */
    public static void setInitialSetting(boolean tf){
        initiateSetting = tf;
    }

    /**
     * check if its 1st time running(false), else true
     * @return boolean true or false
     */
    public static boolean getInitialSetting(){
        return initiateSetting;
    }

}