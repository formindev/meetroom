package com.formindev.meetroom.controller;

import com.formindev.meetroom.domain.Event;
import com.formindev.meetroom.domain.EventDto;
import com.formindev.meetroom.domain.User;
import com.formindev.meetroom.service.EventService;
import com.formindev.meetroom.utils.DateUtils;
import com.formindev.meetroom.utils.EventInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final EventService eventService;

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
            @Valid EventDto eventDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()){
            model.addAttribute("eventDto", eventDto);
            return "event";
        }

        eventDto.setOwner(owner.getId());
        eventService.saveEvent(eventDto);

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(startDate, formatter);
        EventDto eventDto = EventDto.builder().startDate(date).build();
        //model.addAttribute("startDate", date);
        model.addAttribute("eventDto", eventDto);

        return "event";
    }
}
