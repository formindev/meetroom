package com.formindev.meetroom.utils;

import com.formindev.meetroom.domain.Event;
import com.formindev.meetroom.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.Validation;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EventValidator implements Validator {

    private EventRepository eventRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return Event.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Event event = (Event) o;

        if (Duration.between(event.getStartDate(), event.getFinishDate()).toMinutes() >= 30) {
            errors.rejectValue("durationError", "Duration can`t be less than 30 minute");
        }

        if (!checkEvent(event.getStartDate(), event.getFinishDate())) {
            errors.rejectValue("eventOverlapError", "Events can`t overlap");
        }
    }

    public void ownerValidate(Object o, Errors errors) {

    }

    private boolean checkEvent(LocalDateTime startDate, LocalDateTime finishDate) {
        Integer eventsCount = eventRepository
                .findByStartDateBetweenOrFinishDateBetween(startDate, finishDate);

        return eventsCount == 0;
    }
}
