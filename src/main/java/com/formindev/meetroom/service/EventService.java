package com.formindev.meetroom.service;

import com.formindev.meetroom.domain.Event;
import com.formindev.meetroom.domain.EventDto;
import com.formindev.meetroom.domain.User;
import com.formindev.meetroom.repository.EventRepository;
import com.formindev.meetroom.repository.UserRepository;
import com.formindev.meetroom.utils.DateUtils;
import com.formindev.meetroom.utils.EventInfo;
import com.formindev.meetroom.utils.EventValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    public void saveEvent(EventDto eventDto) {
        int[] startTimeArray = timeParse(eventDto.getStartTime());
        LocalDateTime startDate = eventDto.getStartDate().atTime(startTimeArray[0], startTimeArray[1]);
        int[] finishTimeArray = timeParse(eventDto.getDuration());
        LocalDateTime finishDate = startDate.plusHours(finishTimeArray[0]).plusMinutes(finishTimeArray[1]);
        //TODO replace to validation
        User owner = userRepository.findById(eventDto.getOwner()).get();
        // Checking if event is not overlap with other events
        Event event = new Event(owner, eventDto.getTitle(), eventDto.getDescription(), startDate, finishDate);
            eventRepository.save(event);
    }

    public Map<LocalDateTime, List<EventInfo>> getEventsByWeek() throws CloneNotSupportedException{
        Map<LocalDateTime, List<EventInfo>> events = new HashMap<>();

        Event tempEvent = null;

        for (int i = 0; i < DateUtils.DAYS_OF_WEEK - 1; i++) {
            LocalDateTime date = DateUtils.currentMonday.plusDays(i);

            List<Event> eventsByDate = eventRepository.findByStartDateBetween(
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
        return eventRepository.findById(id);
    }

    public void deleteEventById(User user, long id) {
        Event event = getEventById(id);

        if (event.getOwner().getUsername().equals(user.getUsername())) {
            eventRepository.deleteById(id);
        }
    }

    private int[] timeParse(String timeString) {
        String[] timeArray = timeString.split(":");
        int[] timeInteger = new int[2];
        timeInteger[0] = Integer.parseInt(timeArray[0]);
        timeInteger[1] = Integer.parseInt(timeArray[1]);
        return timeInteger;
    }
}
