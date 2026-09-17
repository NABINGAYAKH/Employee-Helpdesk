package com.nabin.employee_helpdesk.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Hello",
        description = "Basic application test API"
)
@RestController
public class HelloController {

    @Operation(
            summary = "Test API",
            description = "Returns a simple message to verify that the application is running"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application is running")
    })
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
