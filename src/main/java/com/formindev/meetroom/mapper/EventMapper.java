package com.formindev.meetroom.mapper;

import com.formindev.meetroom.domain.Event;
import com.formindev.meetroom.domain.EventDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Mapper
public abstract class EventMapper {

    public static final EventMapper INSTANCE = Mappers.getMapper( EventMapper.class );

    public EventDto eventToEventDto(Event event) {
        LocalDate startDate = event.getStartDate().toLocalDate();
        LocalTime startTime = event.getStartDate().toLocalTime();
        Duration durationTime = Duration.between(event.getStartDate(), event.getFinishDate());
        int hours = (int) durationTime.toMinutes() / 60;
        int minutes = (int )durationTime.toMinutes() % 60;
        LocalTime duration = LocalTime.of(hours, minutes);
        EventDto eventDto = EventDto.builder()
                .eventId(event.getId())
                .startDate(startDate)
                .startTime(startTime)
                .duration(duration)
                .title(event.getTitle())
                .description(event.getDescription())
                .owner(event.getOwner())
                .build();

        return eventDto;
    }

    public Event eventDtoToEvent(EventDto eventDto) {
        Event event = new Event();
        LocalDateTime startDate = LocalDateTime.of(eventDto.getStartDate(), eventDto.getStartTime());
        LocalDateTime finishDate = startDate.plus(eventDto.getDurationInMinute(), ChronoUnit.MINUTES);
        if (eventDto.getEventId() != null)
            event.setId(eventDto.getEventId());
        event.setStartDate(startDate);
        event.setFinishDate(finishDate);
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setOwner(eventDto.getOwner());
        return event;
    }
}
