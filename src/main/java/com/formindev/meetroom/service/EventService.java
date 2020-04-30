package com.formindev.meetroom.service;

import com.formindev.meetroom.domain.Event;
import com.formindev.meetroom.domain.User;
import com.formindev.meetroom.repository.EventRepository;
import com.formindev.meetroom.utils.DateUtils;
import com.formindev.meetroom.utils.EventInfo;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

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
        //TODO replace to validation
        boolean isMinDuration = Duration.between(startDate, finishDate).toMinutes() >= 30;
        // Checking if event is not overlap with other events
        if (checkEvent(startDate, finishDate) && isMinDuration) {
            Event event = new Event(owner, title, description, startDate, finishDate);
            eventRepository.save(event);
        }
    }

    public Map<LocalDateTime, List<EventInfo>> getEventsByWeek() throws CloneNotSupportedException{
        Map<LocalDateTime, List<EventInfo>> events = new HashMap<>();

        Event tempEvent = null;

        for (int i = 0; i < DateUtils.DAYS_OF_WEEK - 1; i++) {
            LocalDateTime date = DateUtils.currentMonday.plusDays(i);

            List<Event> eventsByDate = eventRepository.findByStartDateBetween
                    (
                            date, date.plusDays(1)
                    );

            List<EventInfo> eventInfoList = new ArrayList<>();

            //Add split event to the list for next date
            if (tempEvent != null) {
                if (tempEvent.getStartDate().equals(date)) {
                    eventInfoList.add(new EventInfo(tempEvent));
                    tempEvent = null;
                }
            }
            for(Event event : eventsByDate) {
                // Split event if finish date is different than today
                if (!event.getStartDate().truncatedTo(ChronoUnit.DAYS).equals(event.getFinishDate().truncatedTo(ChronoUnit.DAYS))) {
                    //TODO check if finish date is next week
                    tempEvent = event.clone();
                    tempEvent.setStartDate(event.getFinishDate().truncatedTo(ChronoUnit.DAYS));

                    event.setFinishDate(event.getStartDate().truncatedTo(ChronoUnit.DAYS).plusHours(23).plusMinutes(59));
                }
                eventInfoList.add(new EventInfo(event));
            }
            events.put(date, eventInfoList);
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
