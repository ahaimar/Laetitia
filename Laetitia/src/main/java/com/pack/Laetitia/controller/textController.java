package com.pack.Laetitia.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin")
public class textController {

    @GetMapping
    public String sendEmail() {
        // Send email logic goes here
        System.out.println("Send email");
        log.info("Send email");
        return "Email sent successfully";
    }
}
