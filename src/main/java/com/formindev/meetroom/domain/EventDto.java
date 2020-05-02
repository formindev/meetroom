package com.formindev.meetroom.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDto {

    @NotNull
    private LocalDate startDate;

    @NotEmpty(message = "Start time can`t be empty")
    private String startTime;

    @NotEmpty(message = "Duration can`t be empty")
    private String duration;

    @NotEmpty(message = "Title can't be empty")
    private String title;

    @Size(max = 2048, message = "Description too long")
    private String description;

    private Long owner;
}
