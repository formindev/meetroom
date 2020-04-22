package com.formindev.meetroom.controller;

import com.formindev.meetroom.domain.Event;
import com.formindev.meetroom.domain.User;
import com.formindev.meetroom.service.EventService;
import com.formindev.meetroom.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    EventService eventService;

    @GetMapping
    public String getEventsByWeek(Model model) {
        LocalDateTime localStartDate = LocalDateTime.of(2020, Month.APRIL, 15, 00, 30);
        ZoneId localZone = ZoneId.of("Europe/Moscow");
        ZonedDateTime startDate = ZonedDateTime.of(localStartDate, localZone);
        LocalDateTime localEndDate = LocalDateTime.of(2020, Month.APRIL, 21, 00, 30);
        ZonedDateTime endDate = ZonedDateTime.of(localEndDate, localZone);
        List<Event> events = eventService.getEventsByDateRange(startDate, endDate);
        Map<String, String> daysOfWeek = DateUtils.getDaysOfWeek();
        model.addAttribute("daysOfWeek", daysOfWeek);
        model.addAttribute("hoursOfDay", DateUtils.hoursOfDay);
        model.addAttribute("events", events);
        return "booking";
    }

    @PostMapping("/addEvent")
    public String addEvent(
            @AuthenticationPrincipal User owner,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String startDate,
            @RequestParam String finishDate
    ) {
        ZonedDateTime tempStartDate = ZonedDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME);
        ZonedDateTime tempFinishDate = ZonedDateTime.parse(finishDate, DateTimeFormatter.ISO_DATE_TIME);
        eventService.saveEvent(owner, title, description, tempStartDate, tempFinishDate);

        return "redirect:/booking";
    }

    @PostMapping("/nextWeek")
    public String getNextWeek() {
        DateUtils.setNextWeek();

        return "redirect:/booking";
    }

    @PostMapping("/prevWeek")
    public String getPrevWeek() {
        DateUtils.setPrevWeek();

        return "redirect:/booking";
    }
}
