package com.nabin.employee_helpdesk.controller;

import com.nabin.employee_helpdesk.dto.AiTicketAnalysis;
import com.nabin.employee_helpdesk.entity.Ticket;
import com.nabin.employee_helpdesk.service.AiService;
import com.nabin.employee_helpdesk.service.TicketService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {

    private final TicketService ticketService;

    private final AiService aiService;

    public AiController(AiService aiService, TicketService ticketService) {
        this.aiService = aiService;
        this.ticketService = ticketService;
    }

    @PostMapping("/api/ai/analyze/{ticketId}")
    public AiTicketAnalysis analyzeTicket(@PathVariable Integer ticketId){
        Ticket ticket = ticketService.findTicketEntityById(ticketId);
        return aiService.analyzeTicket(ticket);
    }

    @PostMapping("/api/ai/approve/{ticketId}")
    public String approveAnalysis(@PathVariable Integer ticketId) {

        return "AI analysis approved for ticket: " + ticketId;
    }

    @PostMapping("/api/ai/reject/{ticketId}")
    public String rejectAnalysis(@PathVariable Integer ticketId) {

        return "AI analysis rejected for ticket: " + ticketId;
    }

}
