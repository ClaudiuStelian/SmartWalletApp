package com.example.smartwallet;

public class Events {
    public String date, event, hour;

    public Events(){



    }

    public Events(String date, String hour, String event){

        this.date = date;
        this.hour = hour;
        this.event = event;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public String getEvent() {
        return event;
    }

    public String getHour() {
        return hour;
    }
}
