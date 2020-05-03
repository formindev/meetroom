package com.formindev.meetroom.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EventDto {

    @NotNull
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;

    @NotNull(message = "Start time can`t be empty")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "Duration can`t be empty")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime duration;

    @NotEmpty(message = "Title can't be empty")
    private String title;

    @Size(max = 2048, message = "Description too long")
    private String description;

    private User owner;

    private Long eventId;

    public long getDurationInMinute() {
        return duration.get(ChronoField.MINUTE_OF_DAY);
    }

    public long getStartTimeInMinute() {
        return startTime.get(ChronoField.MINUTE_OF_DAY);
    }
}
