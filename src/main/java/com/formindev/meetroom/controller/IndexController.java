package com.formindev.meetroom.controller;


import com.formindev.meetroom.domain.Event;
import com.formindev.meetroom.domain.User;
import com.formindev.meetroom.repository.UserRepository;
import com.formindev.meetroom.service.EventService;
import com.formindev.meetroom.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public String main(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        return "index";
    }
}
