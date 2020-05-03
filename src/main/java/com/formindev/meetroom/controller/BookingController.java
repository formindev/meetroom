package com.formindev.meetroom.controller;

import com.formindev.meetroom.domain.EventDto;
import com.formindev.meetroom.domain.User;
import com.formindev.meetroom.service.EventService;
import com.formindev.meetroom.utils.DateUtils;
import com.formindev.meetroom.utils.EventValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final EventService eventService;

    private final EventValidator eventValidator;

    @GetMapping
    public String getEventsByWeek(Model model) throws CloneNotSupportedException{
        Map<LocalDate, List<EventDto>> events = eventService.getEventDtosByWeek();
        Iterable<LocalDate> daysOfWeek = DateUtils.getDaysOfWeek();
        model.addAttribute("daysOfWeek", daysOfWeek);
        model.addAttribute("hoursOfDay", DateUtils.hoursOfDay);
        model.addAttribute("events", events);

        return "booking";
    }

    @PostMapping("event/saveEvent")
    public String saveEvent(
            @AuthenticationPrincipal User owner,
            @Valid EventDto eventDto,
            BindingResult bindingResult
    ) {
        eventValidator.validate(eventDto, bindingResult);

        if (bindingResult.hasErrors()){
            return "event";
        }

        eventDto.setOwner(owner);
        eventService.saveEvent(eventDto);

        return "redirect:/booking";
    }

    @GetMapping("event/{id}")
    public String showEvent(
            @PathVariable long id,
            Model model
    ) {
        EventDto eventDto = eventService.getEventDtoById(id);
        model.addAttribute("eventDto", eventDto);
        return "details";
    }

    @PostMapping("/event/{id}")
    public String deleteEvent(
            @AuthenticationPrincipal User user,
            @Valid EventDto eventDto,
            BindingResult bindingResult
    ) {
        eventValidator.ownerValidate(eventDto, user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "details";
        }

        eventService.deleteEventById(eventDto.getEventId());
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
        LocalDate date = LocalDate.parse(startDate);
        EventDto eventDto = EventDto.builder().startDate(date).build();
        model.addAttribute("eventDto", eventDto);

        return "event";
    }
}
