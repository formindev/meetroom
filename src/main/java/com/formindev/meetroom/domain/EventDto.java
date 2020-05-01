package com.formindev.meetroom.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotNull
    private String startTime;

    @NotNull
    private String duration;

    @NotNull(message = "Title can't be empty")
    @Size(min=3, message = "Title length must be min 8")
    private String title;

    @Size(max = 2048, message = "Description too long")
    private String description;

    @NotNull
    private Long owner;
}
