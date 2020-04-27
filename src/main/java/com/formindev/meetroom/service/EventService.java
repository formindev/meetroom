package com.formindev.meetroom.service;

import com.formindev.meetroom.domain.Event;
import com.formindev.meetroom.domain.User;
import com.formindev.meetroom.repository.EventRepository;
import com.formindev.meetroom.utils.DateUtils;
import com.formindev.meetroom.utils.EventDuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
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
        String[] startTimeArray = startTime.split(":");
        int startHour = Integer.parseInt(startTimeArray[0]);
        int startMinute = Integer.parseInt(startTimeArray[1]);
        LocalDateTime startDate = LocalDateTime.parse(reserveDate).plusHours(startHour).plusMinutes(startMinute);
        String[] finishTimeArray = duration.split(":");
        int finishHour = Integer.parseInt(finishTimeArray[0]);
        int finishMinute = Integer.parseInt(finishTimeArray[1]);
        LocalDateTime finishDate = startDate.plusHours(finishHour).plusMinutes(finishMinute);
        if (checkEvent(startDate, finishDate)) {
            Event event = new Event(owner, title, description, startDate, finishDate);
            eventRepository.save(event);
        }
    }

    public List<Event> getEventsByDateRange(LocalDateTime startDate, LocalDateTime finishDate) {
        List<Event> events = eventRepository.findByStartDateBetween(startDate, finishDate);
        return events;
    }

    public Map<Long, EventDuration> getEventDurations() {
        LocalDateTime finishDate = DateUtils.currentMonday.plusDays(DateUtils.DAYS_OF_WEEK);
        Iterable<Event> eventsByWeek = getEventsByDateRange(DateUtils.currentMonday, finishDate);
        Map<Long, EventDuration> eventDurations = new LinkedHashMap<>();
        for(Event event : eventsByWeek) {
            eventDurations.put(event.getId(), new EventDuration(event));
        }

        return eventDurations;
    }

    public Map<LocalDateTime, List<Event>> getEventsByWeek() {
        Map<LocalDateTime, List<Event>> events = new LinkedHashMap<> ();

        for (int i = 0; i < DateUtils.DAYS_OF_WEEK - 1; i++) {
            LocalDateTime date = DateUtils.currentMonday.plusDays(i);
            List<Event> eventsByDate = eventRepository.findByStartDateBetween
                    (
                            date, date.plusDays(1)
                    );
            events.put(date, eventsByDate);
        }

        return events;
    }

    public Event getEventById(long id) {
        Event event = eventRepository.findById(id);
        return event;
    }

    public void deleteEventById(User user, long id) {
        Event event = getEventById(id);

        if (event.getOwner().getUsername().equals(user.getUsername())) {
            eventRepository.deleteById(id);
        }
    }

    private boolean checkEvent(LocalDateTime startDate, LocalDateTime finishDate) {
        Integer eventsCount = eventRepository
                .findByStartDateBetweenOrFinishDateBetween(startDate, finishDate);
        if (eventsCount == 0) {
            return true;
        }

        return false;
    }
}
