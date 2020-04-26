package com.formindev.meetroom.utils;

import com.formindev.meetroom.domain.Event;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Formatter;

public class EventDuration {

    private long startInMinute;

    private  long durationInMinute;

    public EventDuration(Event event) {
        long startInMinute = Duration.between
                (
                        event.getStartDate().truncatedTo(ChronoUnit.DAYS),
                        event.getStartDate()
                ).toMinutes();
        long durationInMinute = Duration.between
                (
                        event.getStartDate(),
                        event.getFinishDate()
                ).toMinutes();
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

    public String getStartTime() {
        return getTime(startInMinute);
    }

    public String getDurationTime() {
        return getTime(durationInMinute);
    }

    private String getTime(long timeInMinute) {
        long hour = timeInMinute / 60;
        long minute = timeInMinute % 60;
        StringBuilder time = new StringBuilder();
        Formatter formatter = new Formatter(time);
        formatter.format("%02d:%02d", hour, minute);
        return time.toString();
    }
}
