package com.formindev.meetroom.utils;

import com.formindev.meetroom.domain.Event;
import com.formindev.meetroom.domain.EventDto;
import com.formindev.meetroom.domain.User;
import com.formindev.meetroom.repository.EventRepository;
import com.formindev.meetroom.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class EventValidator implements Validator {

    private final EventRepository eventRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return Event.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        EventDto eventDto = (EventDto) o;

        if (eventDto.getStartTime() != null && eventDto.getDuration() != null) {
            if (eventDto.getDurationInMinute() < 30) {
                errors.rejectValue("duration", null, "Duration can`t be less than 30 minute");
            }

            LocalDateTime startDate = LocalDateTime.of(eventDto.getStartDate(), eventDto.getStartTime());
            LocalDateTime finishDate = startDate.plus(eventDto.getDurationInMinute(), ChronoUnit.MINUTES);

            if (!checkEvent(startDate, finishDate)) {
                errors.rejectValue("eventId", null, "Events can`t overlap");
            }

            if (finishDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                errors.rejectValue("duration", null, "Booking not available for the next week");
            }
        }
    }

    public void ownerValidate(EventDto eventDto, User user, Errors errors) {
        if (!eventDto.getOwner().getUsername().equals(user.getUsername())) {
            errors.rejectValue("owner", null, "Only owner can delete event");
        }
    }

    private boolean checkEvent(LocalDateTime startDate, LocalDateTime finishDate) {
        Integer eventsCount = eventRepository
                .findByStartDateBetweenOrFinishDateBetween(startDate, finishDate);
        return eventsCount == 0;
    }
}
