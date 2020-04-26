package com.formindev.meetroom.repository;

import com.formindev.meetroom.domain.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {
    List<Event> findByStartDateAfterAndFinishDateBefore(LocalDateTime startDate, LocalDateTime finishDate);

    @Query("select count(ev) from Event ev where (ev.startDate<=?1 and ev.finishDate>=?1)or(ev.startDate<=?2 and ev.finishDate>=?2)")
    Integer findByStartDateBetweenOrFinishDateBetween(LocalDateTime startDate, LocalDateTime finishDate);

    Event findById(long id);
}
