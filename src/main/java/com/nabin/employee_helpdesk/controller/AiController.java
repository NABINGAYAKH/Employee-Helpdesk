package com.nabin.employee_helpdesk.controller;

import com.nabin.employee_helpdesk.dto.AiTicketAnalysis;
import com.nabin.employee_helpdesk.entity.Ticket;
import com.nabin.employee_helpdesk.service.AiService;
import com.nabin.employee_helpdesk.service.TicketService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "AI Ticket Analysis",
        description = "AI-assisted ticket analysis and approval workflow APIs"
)
@RestController
public class AiController {

    private final TicketService ticketService;
    private final AiService aiService;

    public AiController(AiService aiService, TicketService ticketService) {
        this.aiService = aiService;
        this.ticketService = ticketService;
    }

    @Operation(
            summary = "Analyze a ticket",
            description = "Analyzes a support ticket and generates an AI-based category, priority recommendation, and suggested solution"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket analyzed successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ticket not found"
            )
    })
    @PostMapping("/api/ai/analyze/{ticketId}")
    public AiTicketAnalysis analyzeTicket(
            @PathVariable Integer ticketId) {

        Ticket ticket = ticketService.findTicketEntityById(ticketId);

        return aiService.analyzeTicket(ticket);
    }

    @Operation(
            summary = "Approve AI analysis",
            description = "Approves the AI analysis for a ticket and applies the recommended priority"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "AI analysis approved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. SUPPORT_AGENT or ADMIN role required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "AI analysis not found"
            )
    })
    @PostMapping("/api/ai/approve/{ticketId}")
    public String approveAnalysis(
            @PathVariable Integer ticketId) {

        aiService.approveAnalysis(ticketId);

        return "AI analysis approved for ticket: " + ticketId;
    }

    @Operation(
            summary = "Reject AI analysis",
            description = "Rejects the AI analysis generated for a ticket"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "AI analysis rejected successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. SUPPORT_AGENT or ADMIN role required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "AI analysis not found"
            )
    })
    @PostMapping("/api/ai/reject/{ticketId}")
    public String rejectAnalysis(
            @PathVariable Integer ticketId) {

        aiService.rejectAnalysis(ticketId);

        return "AI analysis rejected for ticket: " + ticketId;
    }
}