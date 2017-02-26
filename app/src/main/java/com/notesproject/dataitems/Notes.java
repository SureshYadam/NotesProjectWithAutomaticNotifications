package com.notesproject.dataitems;

/**
 * Created by suresh on 25/2/17.
 */

public class Notes {

    private String notes;
    private String date;

    public Notes(String notes, String date) {

        this.notes = notes;
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}

