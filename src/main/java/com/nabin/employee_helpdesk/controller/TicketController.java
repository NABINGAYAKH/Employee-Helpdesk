package com.nabin.employee_helpdesk.controller;

import com.nabin.employee_helpdesk.dto.TicketRequest;
import com.nabin.employee_helpdesk.dto.TicketResponse;
import com.nabin.employee_helpdesk.entity.TicketPriority;
import com.nabin.employee_helpdesk.entity.TicketStatus;
import com.nabin.employee_helpdesk.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/api/tickets")
    public TicketResponse createTicket(@RequestBody @Valid TicketRequest request){
        return ticketService.save(request);
    }

    @GetMapping("/api/tickets")
    public List<TicketResponse> getTickets(
            @RequestParam(required = false) TicketStatus status, @RequestParam(required = false) TicketPriority priority) {

        if( status != null && priority != null){
            return ticketService.findByStatusAndPriority(status, priority);
        }

        if (status != null) {
            return ticketService.findByStatus(status);
        }

        if( priority != null){
            return ticketService.findByPriority(priority);
        }

        return ticketService.findAll();
    }

    @GetMapping("/api/tickets/{id}")
    public TicketResponse getById(@PathVariable int id){
        return ticketService.findById(id);
    }

    @PutMapping("/api/tickets/{id}")
    public TicketResponse updateTicket(@PathVariable int id, @RequestBody @Valid TicketRequest request){
        return ticketService.updateTicket(id, request);
    }

    @DeleteMapping("/api/tickets/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable int id){
        ticketService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
