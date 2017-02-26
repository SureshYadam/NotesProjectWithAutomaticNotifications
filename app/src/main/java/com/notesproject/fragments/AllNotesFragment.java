package com.notesproject.fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.notesproject.AppPreferences;
import com.notesproject.AppSingleton;
import com.notesproject.R;
import com.notesproject.adapters.NotesAdapter;
import com.notesproject.dataitems.Notes;
import com.notesproject.dataitems.Person;
import com.notesproject.recievers.NotificationsReciever;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by suresh on 25/2/17.
 */

public class AllNotesFragment extends Fragment implements View.OnClickListener {

    private Person person;
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private TextView no_notes_text;
    private ImageView add_notes_button;
    private TextView welcome_message;
    private ArrayList<Notes> notesArrayList;

    public AllNotesFragment() {

    }

    @SuppressLint("ValidFragment")
    public AllNotesFragment(Person person) {

        this.person = person;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notes_layout, container, false);

        welcome_message = (TextView) rootView.findViewById(R.id.welcome_message);

        if (person != null) {
            welcome_message.setText("Welcome\t" + person.getName() + "!. Here is your notes...");
        }

        add_notes_button = (ImageView) rootView.findViewById(R.id.add_notes_button);
        add_notes_button.setOnClickListener(this);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.notes_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        no_notes_text = (TextView) rootView.findViewById(R.id.no_notes_added_yet);

        notesArrayList = getNotesArrayList();

        if (notesArrayList == null) {

            no_notes_text.setVisibility(View.VISIBLE);

        } else if (notesArrayList.size() == 0) {
            no_notes_text.setVisibility(View.VISIBLE);
        } else {

            no_notes_text.setVisibility(View.GONE);

            notesAdapter = new NotesAdapter(getActivity(), notesArrayList, new NotesAdapter.NotesDeleted() {
                @Override
                public void onAllNotesDeleted() {

                    no_notes_text.setVisibility(View.VISIBLE);
                }
            });

            recyclerView.setAdapter(notesAdapter);
        }

        //star showing notifcations once we enter into notes screen

        startShowingNotifications();

        return rootView;
    }

    private ArrayList<Notes> getNotesArrayList() {

        //shold get the notes corresponding to that phone number

        Type type = new TypeToken<List<Notes>>() {
        }.getType();

        Gson gson = new Gson();

        ArrayList<Notes> notesArrayList = gson.fromJson(AppPreferences.getAppPreferences(AppSingleton.getSingleton().getMainActivity()).getNotes(), type);

        return notesArrayList;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_notes_button) {
            //adding notes

            AppSingleton.getSingleton().addFragment(new AddingNotesFragment(new AddingNotesFragment.addNotes() {
                @Override
                public void onNotesAdded(String notes) {

                    Log.e("Notes", "is" + notes);
                    no_notes_text.setVisibility(View.GONE);

                    if (notesArrayList == null) {
                        notesArrayList = new ArrayList<Notes>();
                    }

                    notesArrayList.add(new Notes(notes, getCurrentDate()));
                    updateLocalDB();

                    if (notesAdapter == null) {

                        notesAdapter = new NotesAdapter(getActivity(), notesArrayList, new NotesAdapter.NotesDeleted() {
                            @Override
                            public void onAllNotesDeleted() {

                                no_notes_text.setVisibility(View.VISIBLE);
                            }
                        });

                        recyclerView.setAdapter(notesAdapter);

                    }

                    notesAdapter.notifyDataSetChanged();

                    AppSingleton.getSingleton().getMainActivity().showToast("Notes added successfully");
                }
            }), "adding_notes", true);

        }
    }

    private void updateLocalDB() {

        Gson gson = new Gson();
        String json = gson.toJson(notesArrayList);

        //now this new list should be updated in the local db
        AppPreferences.getAppPreferences(AppSingleton.getSingleton().getMainActivity()).insertNotes(json);

    }

    private String getCurrentDate() {
        Date date = new Date();
        long time = date.getTime();

        return DateFormat.format("dd/MM/yyyy hh:mm:ss", time).toString();

    }

    private void startShowingNotifications() {

        Intent intent = new Intent(getActivity(), NotificationsReciever.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getActivity(), 100, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        //need to start notifcaiton after every 30 min
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 30000, pendingIntent); // 30 second repeat for testing

    }

}
