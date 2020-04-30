package com.formindev.meetroom.utils;

import com.formindev.meetroom.domain.Event;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Formatter;

@Getter
public class EventInfo {

    private final long startInMinute;

    private final long durationInMinute;

    @Setter
    private Event event;

    public EventInfo(Event event) {
        this.event = event;

        long startInMinute = Duration.between(
                event.getStartDate().truncatedTo(ChronoUnit.DAYS),
                event.getStartDate()
        ).toMinutes();

        long durationInMinute = Duration.between(
                event.getStartDate(),
                event.getFinishDate()
        ).toMinutes();

        this.startInMinute = startInMinute;
        this.durationInMinute = durationInMinute;
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
