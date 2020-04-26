package com.formindev.meetroom.controller;

import com.formindev.meetroom.domain.Event;
import com.formindev.meetroom.domain.User;
import com.formindev.meetroom.service.EventService;
import com.formindev.meetroom.utils.DateUtils;
import com.formindev.meetroom.utils.EventDuration;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    EventService eventService;

    @GetMapping
    public String getEventsByWeek(Model model) {
        Map<LocalDateTime, List<Event>> events = eventService.getEventsByWeek();
        Iterable<LocalDateTime> daysOfWeek = DateUtils.getDaysOfWeek();
        Map<Long, EventDuration> eventDurations = eventService.getEventDurations();
        model.addAttribute("daysOfWeek", daysOfWeek);
        model.addAttribute("hoursOfDay", DateUtils.hoursOfDay);
        model.addAttribute("events", events);
        model.addAttribute("eventDurations", eventDurations);

        return "booking";
    }

    @PostMapping("event/addEvent")
    public String addEvent(
            @AuthenticationPrincipal User owner,
            @RequestParam String reserveDate,
            @RequestParam String title,
            @RequestParam String startTime,
            @RequestParam String duration,
            @RequestParam String description
    ) {
        eventService.saveEvent(owner, reserveDate, startTime, duration, title, description);

        return "redirect:/booking";
    }

    @GetMapping("event/{id}")
    public String showEvent(
            @PathVariable long id,
            Model model
    ) {
        Event event = eventService.getEventById(id);
        EventDuration eventDuration = new EventDuration(event);
        model.addAttribute("event", event);
        model.addAttribute("eventDuration", eventDuration);
        return "details";
    }

    @PostMapping("/event/{id}")
    public String deleteEvent(@PathVariable long id) {
        eventService.deleteEventById(id);

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

    @PostMapping("/event")
    public String getEventForm(
            @RequestParam String startDate,
            Model model
    ) {
        LocalDateTime date = LocalDateTime.parse(startDate);
        model.addAttribute("startDate", date);

        return "event";
    }
}
