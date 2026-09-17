package com.nabin.employee_helpdesk;

import com.nabin.employee_helpdesk.controller.TicketController;
import com.nabin.employee_helpdesk.dto.TicketRequest;
import com.nabin.employee_helpdesk.dto.TicketResponse;
import com.nabin.employee_helpdesk.entity.TicketPriority;
import com.nabin.employee_helpdesk.entity.TicketStatus;
import com.nabin.employee_helpdesk.exception.ResourceNotFoundException;
import com.nabin.employee_helpdesk.service.CustomUserDetailsService;
import com.nabin.employee_helpdesk.service.JwtService;
import com.nabin.employee_helpdesk.service.TicketService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
class TicketControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TicketService ticketService;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    CustomUserDetailsService customUserDetailsService;


    // Test 1
    @Test
    void shouldGetAllTickets() throws Exception {

        List<TicketResponse> tickets = List.of(
                new TicketResponse(
                        1,
                        "VPN not working",
                        "Unable to connect to VPN",
                        TicketStatus.OPEN,
                        TicketPriority.HIGH,
                        1,
                        2
                ),
                new TicketResponse(
                        2,
                        "Laptop issue",
                        "Laptop is not starting",
                        TicketStatus.IN_PROGRESS,
                        TicketPriority.MEDIUM,
                        2,
                        3
                )
        );

        when(ticketService.findAll())
                .thenReturn(tickets);

        mockMvc.perform(
                        get("/api/tickets")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title")
                        .value("VPN not working"))
                .andExpect(jsonPath("$[0].description")
                        .value("Unable to connect to VPN"))
                .andExpect(jsonPath("$[0].status")
                        .value("OPEN"))
                .andExpect(jsonPath("$[0].priority")
                        .value("HIGH"))
                .andExpect(jsonPath("$[0].employeeId")
                        .value(1))
                .andExpect(jsonPath("$[0].supportAgentId")
                        .value(2))
                .andExpect(jsonPath("$[1].id")
                        .value(2));
    }


    // Test 2
    @Test
    void shouldGetTicketById() throws Exception {

        TicketResponse ticket =
                new TicketResponse(
                        1,
                        "VPN not working",
                        "Unable to connect to VPN",
                        TicketStatus.OPEN,
                        TicketPriority.HIGH,
                        1,
                        2
                );

        when(ticketService.findById(1))
                .thenReturn(ticket);

        mockMvc.perform(
                        get("/api/tickets/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("VPN not working"))
                .andExpect(jsonPath("$.description")
                        .value("Unable to connect to VPN"))
                .andExpect(jsonPath("$.status")
                        .value("OPEN"))
                .andExpect(jsonPath("$.priority")
                        .value("HIGH"))
                .andExpect(jsonPath("$.employeeId")
                        .value(1))
                .andExpect(jsonPath("$.supportAgentId")
                        .value(2));
    }


    // Test 3
    @Test
    void shouldReturnNotFoundWhenTicketDoesNotExist()
            throws Exception {

        when(ticketService.findById(99))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Ticket not found with id: 99"
                        )
                );

        mockMvc.perform(
                        get("/api/tickets/99")
                )
                .andExpect(status().isNotFound());
    }


    // Test 4
    @Test
    void shouldCreateTicket() throws Exception {

        TicketResponse response =
                new TicketResponse(
                        1,
                        "VPN not working",
                        "Unable to connect to VPN",
                        TicketStatus.OPEN,
                        TicketPriority.HIGH,
                        1,
                        2
                );

        when(ticketService.save(any(TicketRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/tickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "VPN not working",
                                            "description": "Unable to connect to VPN",
                                            "status": "OPEN",
                                            "priority": "HIGH",
                                            "employeeId": 1,
                                            "supportAgentId": 2
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("VPN not working"))
                .andExpect(jsonPath("$.description")
                        .value("Unable to connect to VPN"))
                .andExpect(jsonPath("$.status")
                        .value("OPEN"))
                .andExpect(jsonPath("$.priority")
                        .value("HIGH"))
                .andExpect(jsonPath("$.employeeId")
                        .value(1))
                .andExpect(jsonPath("$.supportAgentId")
                        .value(2));
    }


    // Test 5
    @Test
    void shouldRejectInvalidTicket() throws Exception {

        mockMvc.perform(
                        post("/api/tickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "",
                                            "description": "",
                                            "status": "OPEN",
                                            "priority": "HIGH",
                                            "employeeId": 1,
                                            "supportAgentId": null
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());
    }


    // Test 6
    @Test
    void shouldUpdateTicket() throws Exception {

        TicketResponse response =
                new TicketResponse(
                        1,
                        "VPN fixed",
                        "VPN connection has been restored",
                        TicketStatus.RESOLVED,
                        TicketPriority.HIGH,
                        1,
                        2
                );

        when(ticketService.updateTicket(
                eq(1),
                any(TicketRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/api/tickets/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "VPN fixed",
                                            "description": "VPN connection has been restored",
                                            "status": "RESOLVED",
                                            "priority": "HIGH",
                                            "employeeId": 1,
                                            "supportAgentId": 2
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("VPN fixed"))
                .andExpect(jsonPath("$.description")
                        .value("VPN connection has been restored"))
                .andExpect(jsonPath("$.status")
                        .value("RESOLVED"))
                .andExpect(jsonPath("$.priority")
                        .value("HIGH"));
    }


    // Test 7
    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingTicket()
            throws Exception {

        when(ticketService.updateTicket(
                eq(99),
                any(TicketRequest.class)
        )).thenThrow(
                new ResourceNotFoundException(
                        "Ticket not found with id: 99"
                )
        );

        mockMvc.perform(
                        put("/api/tickets/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "VPN fixed",
                                            "description": "VPN connection restored",
                                            "status": "RESOLVED",
                                            "priority": "HIGH",
                                            "employeeId": 1,
                                            "supportAgentId": 2
                                        }
                                        """)
                )
                .andExpect(status().isNotFound());
    }


    // Test 8
    @Test
    void shouldDeleteTicket() throws Exception {

        mockMvc.perform(
                        delete("/api/tickets/1")
                )
                .andExpect(status().isNoContent());

        verify(ticketService).deleteById(1);
    }


    // Test 9
    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingTicket()
            throws Exception {

        doThrow(
                new ResourceNotFoundException(
                        "Ticket not found with id: 99"
                )
        ).when(ticketService).deleteById(99);

        mockMvc.perform(
                        delete("/api/tickets/99")
                )
                .andExpect(status().isNotFound());
    }


    // Test 10
    @Test
    void shouldFilterTicketsByStatus() throws Exception {

        List<TicketResponse> tickets = List.of(
                new TicketResponse(
                        1,
                        "VPN issue",
                        "VPN not connecting",
                        TicketStatus.OPEN,
                        TicketPriority.HIGH,
                        1,
                        2
                )
        );

        when(ticketService.findByStatus(TicketStatus.OPEN))
                .thenReturn(tickets);

        mockMvc.perform(
                        get("/api/tickets")
                                .param("status", "OPEN")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status")
                        .value("OPEN"));
    }


    // Test 11
    @Test
    void shouldFilterTicketsByPriority() throws Exception {

        List<TicketResponse> tickets = List.of(
                new TicketResponse(
                        1,
                        "VPN issue",
                        "VPN not connecting",
                        TicketStatus.OPEN,
                        TicketPriority.HIGH,
                        1,
                        2
                )
        );

        when(ticketService.findByPriority(TicketPriority.HIGH))
                .thenReturn(tickets);

        mockMvc.perform(
                        get("/api/tickets")
                                .param("priority", "HIGH")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].priority")
                        .value("HIGH"));
    }


    // Test 12
    @Test
    void shouldFilterTicketsByStatusAndPriority()
            throws Exception {

        List<TicketResponse> tickets = List.of(
                new TicketResponse(
                        1,
                        "Critical VPN issue",
                        "VPN completely unavailable",
                        TicketStatus.OPEN,
                        TicketPriority.CRITICAL,
                        1,
                        2
                )
        );

        when(ticketService.findByStatusAndPriority(
                TicketStatus.OPEN,
                TicketPriority.CRITICAL
        )).thenReturn(tickets);

        mockMvc.perform(
                        get("/api/tickets")
                                .param("status", "OPEN")
                                .param("priority", "CRITICAL")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status")
                        .value("OPEN"))
                .andExpect(jsonPath("$[0].priority")
                        .value("CRITICAL"));
    }

}