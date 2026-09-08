package com.nabin.employee_helpdesk.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String home(){
        return "Employee Helpdesk API is running";
    }

}
