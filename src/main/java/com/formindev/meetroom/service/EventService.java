package com.formindev.meetroom.service;

import com.formindev.meetroom.domain.Event;
import com.formindev.meetroom.domain.User;
import com.formindev.meetroom.repository.EventRepository;
import com.formindev.meetroom.utils.DateUtils;
import com.formindev.meetroom.utils.EventDuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        List<Event> events = eventRepository.findByStartDateAfterAndFinishDateBeforeOrderByStartDate(startDate, finishDate);
        return events;
    }

    public Map<Long, EventDuration> getEventDurations() {
        ZonedDateTime finishDate = DateUtils.currentMonday.plusDays(DateUtils.DAYS_OF_WEEK);
        Iterable<Event> eventsByWeek = getEventsByDateRange(DateUtils.currentMonday, finishDate);
        Map<Long, EventDuration> eventDurations = new LinkedHashMap<>();
        for(Event event : eventsByWeek) {
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
            eventDurations.put(event.getId(), new EventDuration(startInMinute, durationInMinute));
        }

        return eventDurations;
    }

    public Map<String, List<Event>> getEventsByWeek() {
        Map<String, List<Event>> events = new LinkedHashMap<> ();

        for (int i = 0; i < DateUtils.DAYS_OF_WEEK - 1; i++) {
            ZonedDateTime date = DateUtils.currentMonday.plusDays(i);
            List<Event> eventsByDate = eventRepository.findByStartDateAfterAndFinishDateBeforeOrderByStartDate
                    (
                            date, date.plusDays(1)
                    );
            events.put(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), eventsByDate);
        }

        return events;
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
