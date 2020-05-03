package com.formindev.meetroom.service;

import com.formindev.meetroom.domain.Event;
import com.formindev.meetroom.domain.EventDto;
import com.formindev.meetroom.mapper.EventMapper;
import com.formindev.meetroom.repository.EventRepository;
import com.formindev.meetroom.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public void saveEvent(EventDto eventDto) {
        Event event = EventMapper.INSTANCE.eventDtoToEvent(eventDto);
        eventRepository.save(event);
    }

    public Map<LocalDate, List<EventDto>> getEventDtosByWeek() throws CloneNotSupportedException{
        Map<LocalDate, List<EventDto>> events = new HashMap<>();

        EventDto splitEventDto = null;

        for (int i = 0; i < DateUtils.DAYS_OF_WEEK - 1; i++) {
            LocalDateTime date = LocalDateTime.of(
                    DateUtils.currentMonday.plusDays(i),
                    LocalTime.of(0, 0)
            );

            List<Event> eventsByDate = eventRepository.findByStartDateBetween(
                    date, date.plusDays(1)
            );

            List<EventDto> eventDtos = new ArrayList<>();

            //Add split event to the list for next date
            if (splitEventDto != null) {
                if (splitEventDto.getStartDate().equals(date.toLocalDate())) {
                    eventDtos.add(splitEventDto);
                    splitEventDto = null;
                }
            }

            for(Event event : eventsByDate) {
               // Split event if finish date is different than today
                if (!event.getStartDate().truncatedTo(ChronoUnit.DAYS)
                        .equals(event.getFinishDate().truncatedTo(ChronoUnit.DAYS))) {

                    Event splitEvent = event.clone();
                    splitEvent.setStartDate(event.getFinishDate().truncatedTo(ChronoUnit.DAYS));

                    splitEventDto = EventMapper.INSTANCE.eventToEventDto(splitEvent);

                    event.setFinishDate(
                            event.getStartDate().truncatedTo(ChronoUnit.DAYS)
                                    .plusHours(23).plusMinutes(59)
                    );
                }

                EventDto eventDto = EventMapper.INSTANCE.eventToEventDto(event);
                eventDtos.add(eventDto);
            }
            events.put(date.toLocalDate(), eventDtos);
        }

        return events;
    }

    public EventDto getEventDtoById(long id) {
        return EventMapper.INSTANCE.eventToEventDto(getEventById(id));
    }

    public void deleteEventById(long id) {
        eventRepository.deleteById(id);
    }

    private Event getEventById(long id) {
        return eventRepository.findById(id);
    }
}
