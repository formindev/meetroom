package com.formindev.meetroom.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
public class Event implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User owner;

    @NotNull
    private String title;

    private String description;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime finishDate;

    public Event(
            User owner,
            @NotNull String title,
            String description,
            @NotNull LocalDateTime startDate,
            @NotNull LocalDateTime finishDate
    ) {
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    @Override
    public Event clone() throws CloneNotSupportedException {
        return (Event) super.clone();
    }
}
