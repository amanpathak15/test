package com.mindwarriorhack.app.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.model.SocialFeeds.Comment.CommentsItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class PreferenceManager {

    private static SharedPreferences sharedPrefs;
    private static SharedPreferences.Editor preferenceEditor;
    private static Context context;

    public static void createSharedPreferences(final Context context) {

        PreferenceManager.context = context;
        sharedPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(PreferenceManager.context);
        preferenceEditor = sharedPrefs.edit();
    }

    public static void clearPreference() {
        preferenceEditor = sharedPrefs.edit();
        preferenceEditor.clear();
        preferenceEditor.commit();

    }

    public static String getDeviceToken() {
        return sharedPrefs.getString(Constant.DEVICE_TOKEN, "");
    }

    public static void setDeviceToken(String token) {
        preferenceEditor.putString(Constant.DEVICE_TOKEN, token);
        preferenceEditor.commit();
    }

    public static void storePojo(CommentsItem response, String key){
        Gson gson = new Gson();
        String json = gson.toJson(response); // myObject - instance of MyObject
        preferenceEditor.putString(key, json);
        preferenceEditor.commit();
    }

    public static CommentsItem getPojo(String key){
        Gson gson = new Gson();
        String json = sharedPrefs.getString(key, "");
        return gson.fromJson(json, CommentsItem.class);
    }




    public static String getDeviceId() {
        return sharedPrefs.getString(Constant.DEVICE_ID,"1");
    }

    public static void setDeviceId(String vlu) {
        preferenceEditor.putString(Constant.DEVICE_ID, vlu);
        preferenceEditor.apply();
    }


    public static void setIsUserLogin(boolean status) {
        preferenceEditor.putBoolean(Constant.LOGIN_STATUS, status);
        preferenceEditor.apply();
    }

    public static boolean isUserLogin() {
        return sharedPrefs.getBoolean(Constant.LOGIN_STATUS, false);
    }

    public static void setIsNotificationsEnabled(boolean status) {
        preferenceEditor.putBoolean(Constant.NOTIFICATION_STATUS, status);
        preferenceEditor.apply();
    }

    public static boolean IsNotificationsEnabled() {
        return sharedPrefs.getBoolean(Constant.NOTIFICATION_STATUS, false);
    }


    public static void setString(String key, String str) {
        preferenceEditor.putString(key, TextUtils.isEmpty(str) ? "" : str);
        preferenceEditor.commit();
    }




    public static String getString(String key) {
        return sharedPrefs.getString(key, "");
    }

}
