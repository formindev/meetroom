package com.formindev.meetroom.utils;

public class EventDuration {

    private long startInMinute;

    private  long durationInMinute;

    public EventDuration(long startInMinute, long durationInMinute) {
        this.startInMinute = startInMinute;
        this.durationInMinute = durationInMinute;
    }

    public void setDurationInMinute(long durationInMinute) {
        this.durationInMinute = durationInMinute;
    }

    public long getDurationInMinute() {
        return durationInMinute;
    }

    public long getStartInMinute() {
        return startInMinute;
    }

    public void setStartInMinute(long startInMinute) {
        this.startInMinute = startInMinute;
    }
}
