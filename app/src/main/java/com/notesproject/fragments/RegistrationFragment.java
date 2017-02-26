package com.notesproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

public class RegistrationFragment extends Fragment {

    private EditText name, phoneNumber, password, reEnterPassword;
    private Button submit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.registration_layout, container, false);

        initViews(rootView);
        return rootView;
    }

    private void initViews(View view) {

        name = (EditText) view.findViewById(R.id.name);
        phoneNumber = (EditText) view.findViewById(R.id.phoneNumber);
        password = (EditText) view.findViewById(R.id.passowrd);
        reEnterPassword = (EditText) view.findViewById(R.id.confirm_password);

        submit = (Button) view.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAndSaveInDB();

            }
        });
    }

    private void makeAllFieldsEmpty() {
        name.setText("");
        phoneNumber.setText("");
        password.setText("");
        reEnterPassword.setText("");
    }

    private void checkAndSaveInDB() {
        String nameEntered = name.getText().toString().trim();
        String phoneNumberEntered = phoneNumber.getText().toString().trim();
        String passwordEntered = password.getText().toString().trim();
        String reEnteredPasswordEntered = reEnterPassword.getText().toString().trim();

        MainActivity mainActivity = AppSingleton.getSingleton().getMainActivity();

        if (nameEntered.length() == 0) {

            name.setError("Enter name please");
            name.requestFocus();
            return;
        }

        if (nameEntered.length() < 2) {
            name.setError("Name shold atleast contain 2 characters");
            name.requestFocus();
            return;
        }
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
            password.setError("Enter passwrod");
            password.requestFocus();
            return;
        }

        if (passwordEntered.length() < 4) {
            reEnterPassword.setError("Password must contain atleast 4 characters");
            reEnterPassword.requestFocus();
            return;
        }

        if (reEnteredPasswordEntered.length() == 0) {
            reEnterPassword.setError("Enter password again");
            reEnterPassword.requestFocus();
            return;
        }

        if (!reEnteredPasswordEntered.equalsIgnoreCase(passwordEntered)) {
            reEnterPassword.setError("Password doesn't match");
            reEnterPassword.requestFocus();
            return;
        } else {

            if (isAlreadyRegistered(phoneNumber.getText().toString())) {
                //this phone number is already existed

                AppSingleton.getSingleton().getMainActivity().showToast("Phone number already existed, can't register again...");
            } else {
                //save the details in local db

                //get the old list from local db and save the new person in the db and update

                ArrayList<Person> personArrayList = getPersonList();

                if (personArrayList == null) {
                    personArrayList = new ArrayList<Person>();
                }
                personArrayList.add(new Person(AppPreferences.getAppPreferences(mainActivity).getCurrentId(), nameEntered, phoneNumberEntered, passwordEntered));

                Gson gson = new Gson();
                String json = gson.toJson(personArrayList);

                //now this new list should be updated in the local db
                AppPreferences.getAppPreferences(mainActivity).insertPersons(json);

                AppSingleton.getSingleton().getMainActivity().showToast("Successfully registered");

                makeAllFieldsEmpty();

                AppSingleton.getSingleton().getMainActivity().onBackPressed();

            }
        }
    }

    private boolean isAlreadyRegistered(String phone) {

        ArrayList<Person> personsArrayList = getPersonList();

        if (personsArrayList != null) {
            for (Person person : personsArrayList) {
                if (person.getPhoneNumber().contains(phone)) {
                    return true;
                }
            }
        }

        return false;
    }

    public ArrayList<Person> getPersonList() {

        Type type = new TypeToken<List<Person>>() {
        }.getType();

        Gson gson = new Gson();

        ArrayList<Person> personsArrayList = gson.fromJson(AppPreferences.getAppPreferences(AppSingleton.getSingleton().getMainActivity()).getPersons(), type);

        return personsArrayList;
    }

}
