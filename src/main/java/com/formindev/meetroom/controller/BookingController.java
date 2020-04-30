package com.formindev.meetroom.controller;

import com.formindev.meetroom.domain.Event;
import com.formindev.meetroom.domain.User;
import com.formindev.meetroom.service.EventService;
import com.formindev.meetroom.utils.DateUtils;
import com.formindev.meetroom.utils.EventInfo;
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

    private final EventService eventService;

    public BookingController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String getEventsByWeek(Model model) throws CloneNotSupportedException{
        Map<LocalDateTime, List<EventInfo>> events = eventService.getEventsByWeek();
        Iterable<LocalDateTime> daysOfWeek = DateUtils.getDaysOfWeek();
        model.addAttribute("daysOfWeek", daysOfWeek);
        model.addAttribute("hoursOfDay", DateUtils.hoursOfDay);
        model.addAttribute("events", events);

        return "booking";
    }

    @PostMapping("event/saveEvent")
    public String saveEvent(
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
        EventInfo eventInfo = new EventInfo(event);
        model.addAttribute("eventInfo", eventInfo);
        return "details";
    }

    @PostMapping("/event/{id}")
    public String deleteEvent(
            @AuthenticationPrincipal User user,
            @PathVariable long id) {
        eventService.deleteEventById(user, id);
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

    @PostMapping("/event/addEvent")
    public String getEventForm(
            @RequestParam String startDate,
            Model model
    ) {
        LocalDateTime date = LocalDateTime.parse(startDate);
        model.addAttribute("startDate", date);

        return "event";
    }
}
