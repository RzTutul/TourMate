package com.example.tourmate.pojos;

public class DairyPojo {
    String eventID;
    String dairyID;
    String date;
    String title;
    String note;

    public DairyPojo() {
    }

    public DairyPojo(String eventID, String dairyID, String date, String title, String note) {
        this.eventID = eventID;
        this.dairyID = dairyID;
        this.date = date;
        this.title = title;
        this.note = note;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getDairyID() {
        return dairyID;
    }

    public void setDairyID(String dairyID) {
        this.dairyID = dairyID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
