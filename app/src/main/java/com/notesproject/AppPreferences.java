package com.notesproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

/**
 * Created by suresh on 25/2/17.
 */

public class AppPreferences {

    private String KEY_PERSONS = "KEY_PERSONS";
    private String KEY_ID = "KEY_ID";
    private String KEY_NOTES = "KEY_NOTES";
    private String KEY_OLD_PHONE = "KEY_OLD_PHONE";
    private String KEY_FIRST_TIME = "KEY_FIRST_TIME";
    private String KEY_NOTIFICATION_COUNT = "KEY_NOTIFICATION_COUNT";

    private static AppPreferences appPreferences;
    private SharedPreferences sharedPreferences;

    public AppPreferences(Context context) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static AppPreferences getAppPreferences(Context context) {


        if (appPreferences == null) {
            appPreferences = new AppPreferences(context);
        }
        return appPreferences;
    }

    public String getPersons() {

        String persons = null;
        if (sharedPreferences != null) {

            persons = sharedPreferences.getString(KEY_PERSONS, null);
        }
        return persons;
    }

    public void insertPersons(String persons) {

        if (sharedPreferences != null) {

            sharedPreferences.edit().putString(KEY_PERSONS, persons).commit();

        }
    }

    public int getCurrentId() {
        int id = 0;

        if (sharedPreferences != null) {
            id = sharedPreferences.getInt(KEY_ID, 0);
        }
        return id;
    }

    public void setCurrentId(int id) {
        if (sharedPreferences != null) {
            sharedPreferences.edit().putInt(KEY_ID, id).commit();
        }
    }

    public void insertNotes(String notes) {

        if (sharedPreferences != null) {

            sharedPreferences.edit().putString(KEY_NOTES, notes).commit();

        }
    }

    public String getNotes() {

        String notes = null;
        if (sharedPreferences != null) {
            notes = sharedPreferences.getString(KEY_NOTES, null);
        }
        return notes;
    }

    public String getOldLoginPhone() {
        String phone = null;
        if (sharedPreferences != null) {
            phone = sharedPreferences.getString(KEY_OLD_PHONE, null);
        }
        return phone;
    }

    public void saveOldLoginPhone(String phone) {
        if (sharedPreferences != null) {
            sharedPreferences.edit().putString(KEY_OLD_PHONE, phone).commit();
        }
    }

    public boolean isFirstTime() {
        boolean firstTime = false;
        if (sharedPreferences != null) {
            firstTime = sharedPreferences.getBoolean(KEY_FIRST_TIME, true);
        }
        return firstTime;
    }

    public void setFirstTime(boolean value) {
        if (sharedPreferences != null) {
            sharedPreferences.edit().putBoolean(KEY_FIRST_TIME, value).commit();
        }
    }


    public int getNotificationCount() {
        int notifiction = 0;

        if (sharedPreferences != null) {
            notifiction = sharedPreferences.getInt(KEY_NOTIFICATION_COUNT, 0);
        }
        return notifiction;
    }

    public void setNotificationCount(int notificationCount) {
        if (sharedPreferences != null) {
            sharedPreferences.edit().putInt(KEY_NOTIFICATION_COUNT, notificationCount).commit();
        }
    }

}
