package com.notesproject.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.notesproject.AppSingleton;
import com.notesproject.R;

/**
 * Created by suresh on 25/2/17.
 */

public class AddingNotesFragment extends Fragment {

    private addNotes addNotes;
    private EditText notesEditText;
    private Button save;

    public AddingNotesFragment() {

    }

    @SuppressLint("ValidFragment")
    public AddingNotesFragment(addNotes addNotes) {
        this.addNotes = addNotes;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.adding_notes_layout, container, false);

        notesEditText = (EditText) rootView.findViewById(R.id.enter_notes);
        save = (Button) rootView.findViewById(R.id.save_notes);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (notesEditText.getText().toString().trim().length() < 5) {

                    AppSingleton.getSingleton().getMainActivity().showToast("Notes should atleast contains 5 characters");

                    return;
                } else {

                    addNotes.onNotesAdded(notesEditText.getText().toString());
                    AppSingleton.getSingleton().getMainActivity().hideVirtualKeyboard();
                    getActivity().onBackPressed();

                }
            }
        });

        return rootView;
    }


    public interface addNotes {
        void onNotesAdded(String notes);
    }
}
