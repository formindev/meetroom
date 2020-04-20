package com.formindev.meetroom.domain;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User owner;

    @NotNull
    private String title;

    @Size(max = 2048)
    private String description;

    @NotNull
    private String startDate;

    @NotNull
    private String finishDate;
}
