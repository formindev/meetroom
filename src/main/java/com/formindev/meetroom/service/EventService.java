package com.formindev.meetroom.service;

import com.formindev.meetroom.domain.Event;
import com.formindev.meetroom.domain.User;
import com.formindev.meetroom.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public void saveEvent(
            User owner,
            String title,
            String description,
            ZonedDateTime startDate,
            ZonedDateTime finishDate) {
        Event event = new Event(owner, title, description, startDate, finishDate);
        eventRepository.save(event);
    }

    public List<Event> getEventsByDateRange(ZonedDateTime startDate, ZonedDateTime finishDate) {
        List<Event> events = eventRepository.findByStartDateAfterAndFinishDateBefore(startDate, finishDate);

        return events;
    }

}
