package com.notesproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.notesproject.AppPreferences;
import com.notesproject.AppSingleton;
import com.notesproject.R;
import com.notesproject.dataitems.Notes;

import java.util.ArrayList;

/**
 * Created by suresh on 25/2/17.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private ArrayList<Notes> notesArrayList;
    private Context context;
    private NotesDeleted notesDeleted;

    public NotesAdapter(Context context, ArrayList<Notes> notesArrayList, NotesDeleted notesDeleted) {

        this.context = context;
        this.notesArrayList = notesArrayList;
        this.notesDeleted = notesDeleted;

    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.note_row, parent, false);

        NotesViewHolder notesViewHolder = new NotesViewHolder(view);

        return notesViewHolder;
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {

        holder.notes.setText(notesArrayList.get(position).getNotes());
        holder.date.setText(notesArrayList.get(position).getDate());

    }

    @Override
    public int getItemCount() {

        return notesArrayList.size();

    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {

        private TextView notes;
        private ImageView delete;
        private TextView date;

        public NotesViewHolder(View itemView) {
            super(itemView);

            notes = (TextView) itemView.findViewById(R.id.note);
            date = (TextView) itemView.findViewById(R.id.note_time);

            delete = (ImageView) itemView.findViewById(R.id.note_delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    notesArrayList.remove(getAdapterPosition());

                    //update the local db...

                    Gson gson = new Gson();
                    String json = gson.toJson(notesArrayList);

                    //now this new list should be updated in the local db
                    AppPreferences.getAppPreferences(AppSingleton.getSingleton().getMainActivity()).insertNotes(json);

                    AppSingleton.getSingleton().getMainActivity().showToast("Deleted Successfully");

                    if (notesArrayList.size() == 0) {
                        notesDeleted.onAllNotesDeleted();
                    }

                    notifyDataSetChanged();
                }
            });
        }
    }

    public interface NotesDeleted {
        void onAllNotesDeleted();
    }

}
