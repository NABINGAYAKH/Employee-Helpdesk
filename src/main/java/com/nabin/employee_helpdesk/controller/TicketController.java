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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Tickets",
        description = "Ticket management APIs"
)
@RestController
public class TicketController {

    @Autowired
    private TicketService ticketService;




    @Operation(
            summary = "Create a new ticket",
            description = "Creates a new support ticket using the provided ticket details"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ticket data"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    @PostMapping("/api/tickets")
    public TicketResponse createTicket(@RequestBody @Valid TicketRequest request){
        return ticketService.save(request);
    }



    @Operation(
            summary = "Get tickets",
            description = "Returns tickets with optional filtering by status and priority"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tickets retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
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



    @Operation(
            summary = "Get ticket by ID",
            description = "Returns a ticket using the ticket ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket found"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @GetMapping("/api/tickets/{id}")
    public TicketResponse getById(@PathVariable int id){
        return ticketService.findById(id);
    }



    @Operation(
            summary = "Update a ticket",
            description = "Updates an existing ticket using the ticket ID and provided ticket details"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ticket data"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied. SUPPORT_AGENT or ADMIN role required"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @PutMapping("/api/tickets/{id}")
    public TicketResponse updateTicket(@PathVariable int id, @RequestBody @Valid TicketRequest request){
        return ticketService.updateTicket(id, request);
    }


    @Operation(
            summary = "Delete a ticket",
            description = "Deletes an existing ticket using the ticket ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ticket deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied. SUPPORT_AGENT or ADMIN role required"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @DeleteMapping("/api/tickets/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable int id){
        ticketService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
