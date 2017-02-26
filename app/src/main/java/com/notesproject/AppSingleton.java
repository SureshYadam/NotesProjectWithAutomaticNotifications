package com.notesproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.notesproject.fragments.LoginFragment;

/**
 * Created by suresh on 25/2/17.
 */

public class AppSingleton {

    private MainActivity mainActivity;
    private static AppSingleton appSingleton;

    public void initMainActivity(MainActivity mainActivity) {

        this.mainActivity = mainActivity;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public static AppSingleton getSingleton() {
        if (appSingleton == null) {
            appSingleton = new AppSingleton();
        }
        return appSingleton;
    }


    public void addFragment(Fragment fragment, String tag, boolean allowback) {

        mainActivity.hideVirtualKeyboard();
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.container, fragment);

        if (allowback) {
            fragmentTransaction.addToBackStack(tag);
        }

        fragmentTransaction.commit();
    }

    public void replaceFragment(Fragment fragment, String tag, boolean allowback) {

        mainActivity.hideVirtualKeyboard();
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.container, fragment);

        if (allowback) {
            fragmentTransaction.addToBackStack(tag);
        }

        fragmentTransaction.commit();
    }
}
