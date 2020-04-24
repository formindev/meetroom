package com.formindev.meetroom.service;

import com.formindev.meetroom.domain.Event;
import com.formindev.meetroom.domain.User;
import com.formindev.meetroom.repository.EventRepository;
import com.formindev.meetroom.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public void saveEvent(
            User owner,
            String reserveDate,
            String startTime,
            String duration,
            String title,
            String description
    ) {
        ZonedDateTime startDate = ZonedDateTime.parse(reserveDate + 'T' + startTime + 'Z', DateTimeFormatter.ISO_DATE_TIME);
        String[] time = duration.split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);
        ZonedDateTime finishDate = startDate.plusHours(hour).plusMinutes(minute);
        if (checkEvent(startDate, finishDate)) {
            Event event = new Event(owner, title, description, startDate, finishDate);
            eventRepository.save(event);
        }
    }

    public List<Event> getEventsByDateRange(ZonedDateTime startDate, ZonedDateTime finishDate) {
        List<Event> events = eventRepository.findByStartDateAfterAndFinishDateBefore(startDate, finishDate);
        return events;
    }

    public List<Event> getEventsByCurrentWeek(ZonedDateTime startDate) {
        ZonedDateTime finishDate = startDate.plusDays(DateUtils.DAYS_OF_WEEK);
        return getEventsByDateRange(startDate, finishDate);
    }

    private boolean checkEvent(ZonedDateTime startDate, ZonedDateTime finishDate) {
        Integer eventsCount = eventRepository
                .findByStartDateBetweenOrFinishDateBetween(startDate, finishDate);
        if (eventsCount == 0) {
            return true;
        }

        return false;
    }
}
