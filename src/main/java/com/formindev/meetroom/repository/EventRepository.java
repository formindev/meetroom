package com.formindev.meetroom.repository;

import com.formindev.meetroom.domain.Event;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {
    List<Event> findByStartDateAfterAndFinishDateBefore(ZonedDateTime startDate, ZonedDateTime finishDate);
}
