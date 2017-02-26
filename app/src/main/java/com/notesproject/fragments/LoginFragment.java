package com.notesproject.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.notesproject.AppPreferences;
import com.notesproject.AppSingleton;
import com.notesproject.MainActivity;
import com.notesproject.R;
import com.notesproject.dataitems.Person;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suresh on 25/2/17.
 */

public class LoginFragment extends Fragment {

    private EditText phoneNumber, password;
    private Button signup, login;
    private Person person;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_layout, container, false);

        initViews(rootView);

        return rootView;
    }

    private void initViews(View view) {

        phoneNumber = (EditText) view.findViewById(R.id.phoneNumberLogin);

        password = (EditText) view.findViewById(R.id.passwordLogin);

        signup = (Button) view.findViewById(R.id.signUpButton);

        login = (Button) view.findViewById(R.id.login);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //replace registartion fragment
                phoneNumber.setText("");
                password.setText("");
                AppSingleton.getSingleton().replaceFragment(new RegistrationFragment(), "registration", true);


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validations
                String phoneNumberEntered = phoneNumber.getText().toString().trim();
                String passwordEntered = password.getText().toString().trim();

                MainActivity mainActivity = AppSingleton.getSingleton().getMainActivity();


                if (phoneNumberEntered.length() == 0) {


                    phoneNumber.setError("Enter phone number");
                    phoneNumber.requestFocus();

                    return;
                }

                if (phoneNumberEntered.length() != 10) {
                    phoneNumber.setError("Enter valid phone number");
                    phoneNumber.requestFocus();

                    return;
                }


                if (passwordEntered.length() == 0) {

                    password.setError("Enter password");
                    password.requestFocus();
                    return;
                }


                if (passwordEntered.length() < 4) {

                    password.setError("Password should be atleast 4 characters");
                    password.requestFocus();

                    return;
                }


                //replace notes fragment
                //need to check whether login credentials are present in db or not

                if (isLoggedIn(phoneNumberEntered, passwordEntered)) {


                    if (!AppPreferences.getAppPreferences(mainActivity).isFirstTime()) {

                        //already some one logged in, so check is new one logging in
                        if (isNewUser(phoneNumberEntered)) {

                            showWarningDialog(phoneNumberEntered);

                            return;
                        }

                    } else {
                        AppPreferences.getAppPreferences(mainActivity).setFirstTime(false);
                        AppPreferences.getAppPreferences(mainActivity).saveOldLoginPhone(phoneNumberEntered);
                    }

                    phoneNumber.setText("");
                    password.setText("");

                    mainActivity.showToast("Successfully logged in");

                    AppSingleton.getSingleton().replaceFragment(new AllNotesFragment(person), "all_notes", true);

                } else {

                    //not yet registerd
                    mainActivity.showToast("Invalid credentaisl");
                }

            }
        });
    }

    private void showWarningDialog(final String phone) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.warning_dialog);

        TextView ok, cancel;
        ok = (TextView) dialog.findViewById(R.id.ok);
        cancel = (TextView) dialog.findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //need to erase old data
                AppPreferences.getAppPreferences(AppSingleton.getSingleton().getMainActivity()).insertNotes(null);

                AppPreferences.getAppPreferences(AppSingleton.getSingleton().getMainActivity()).saveOldLoginPhone(phone);

                dialog.cancel();

                AppSingleton.getSingleton().replaceFragment(new AllNotesFragment(person), "all_notes", true);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });

        dialog.show();
    }

    private boolean isNewUser(String phone) {

        if (AppPreferences.getAppPreferences(AppSingleton.getSingleton().getMainActivity()).getOldLoginPhone().equalsIgnoreCase(phone)) {

            return false;
        } else {
            return true;
        }
    }


    private boolean isLoggedIn(String phoneNumber, String password) {

        Type type = new TypeToken<List<Person>>() {
        }.getType();

        Gson gson = new Gson();

        ArrayList<Person> personsArrayList = gson.fromJson(AppPreferences.getAppPreferences(AppSingleton.getSingleton().getMainActivity()).getPersons(), type);

        if (personsArrayList != null) {

            for (Person persons : personsArrayList) {

                //checking wheter those phonenumber and password are present or not

                if (persons.getPhoneNumber().contains(phoneNumber) && persons.getPassword().contains(password)) {

                    person = persons;

                    return true;

                }

            }

        } else {
            return false;
        }

        return false;
    }
}
