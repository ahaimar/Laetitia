package com.pack.Laetitia.conttroller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class MailController {

    @GetMapping
    public String sendEmail() {
        // Send email logic goes here
        return "Email sent successfully";
    }
}
