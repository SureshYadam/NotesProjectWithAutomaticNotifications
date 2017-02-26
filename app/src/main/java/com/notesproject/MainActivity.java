package com.notesproject;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.notesproject.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {

    private View snackbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        snackbarView = findViewById(R.id.container);

        //initializing mainactivity
        AppSingleton.getSingleton().initMainActivity(this);

        AppSingleton.getSingleton().replaceFragment(new LoginFragment(), "login", false);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void showToast(String message) {

        hideVirtualKeyboard();

        Snackbar snack = Snackbar.make(snackbarView, message, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        view.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red));
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();

    }

    public void hideVirtualKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            View focused = getCurrentFocus();
            if (focused != null && focused.getWindowToken() != null) {
                inputManager.hideSoftInputFromWindow(focused.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
        }
    }
}
