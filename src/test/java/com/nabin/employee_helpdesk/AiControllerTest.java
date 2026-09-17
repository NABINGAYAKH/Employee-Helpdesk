package com.nabin.employee_helpdesk;

import com.nabin.employee_helpdesk.controller.AiController;
import com.nabin.employee_helpdesk.dto.AiTicketAnalysis;
import com.nabin.employee_helpdesk.entity.AiAnalysisStatus;
import com.nabin.employee_helpdesk.entity.Ticket;
import com.nabin.employee_helpdesk.entity.TicketPriority;
import com.nabin.employee_helpdesk.entity.TicketStatus;
import com.nabin.employee_helpdesk.exception.ResourceNotFoundException;
import com.nabin.employee_helpdesk.service.AiService;
import com.nabin.employee_helpdesk.service.CustomUserDetailsService;
import com.nabin.employee_helpdesk.service.JwtService;
import com.nabin.employee_helpdesk.service.TicketService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AiController.class)
@AutoConfigureMockMvc(addFilters = false)
class AiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AiService aiService;

    @MockitoBean
    TicketService ticketService;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    CustomUserDetailsService customUserDetailsService;


    // Test 1
    @Test
    void shouldAnalyzeTicket() throws Exception {

        Ticket ticket = new Ticket();

        ticket.setId(1);
        ticket.setTitle("VPN not working");
        ticket.setDescription("Unable to connect to VPN");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.LOW);

        AiTicketAnalysis analysis =
                new AiTicketAnalysis(
                        "NETWORK",
                        "HIGH",
                        "VPN issue detected",
                        "Verify VPN credentials and network connectivity.",
                        AiAnalysisStatus.PENDING
                );

        when(ticketService.findTicketEntityById(1))
                .thenReturn(ticket);

        when(aiService.analyzeTicket(ticket))
                .thenReturn(analysis);

        mockMvc.perform(
                        post("/api/ai/analyze/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category")
                        .value("NETWORK"))
                .andExpect(jsonPath("$.priority")
                        .value("HIGH"))
                .andExpect(jsonPath("$.summary")
                        .value("VPN issue detected"))
                .andExpect(jsonPath("$.suggestedSolution")
                        .value(
                                "Verify VPN credentials and network connectivity."
                        ))
                .andExpect(jsonPath("$.status")
                        .value("PENDING"));
    }


    // Test 2
    @Test
    void shouldReturnNotFoundWhenAnalyzingNonExistingTicket()
            throws Exception {

        when(ticketService.findTicketEntityById(99))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Ticket not found with id: 99"
                        )
                );

        mockMvc.perform(
                        post("/api/ai/analyze/99")
                )
                .andExpect(status().isNotFound());
    }


    // Test 3
    @Test
    void shouldApproveAnalysis() throws Exception {

        mockMvc.perform(
                        post("/api/ai/approve/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                "AI analysis approved for ticket: 1"
                        )
                );
    }


    // Test 4
    @Test
    void shouldRejectAnalysis() throws Exception {

        mockMvc.perform(
                        post("/api/ai/reject/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                "AI analysis rejected for ticket: 1"
                        )
                );
    }


    // Test 5
    @Test
    void shouldPassTicketToAiService() throws Exception {

        Ticket ticket = new Ticket();

        ticket.setId(1);
        ticket.setTitle("VPN not working");
        ticket.setDescription("Unable to connect to VPN");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.LOW);

        AiTicketAnalysis analysis =
                new AiTicketAnalysis(
                        "NETWORK",
                        "HIGH",
                        "VPN issue detected",
                        "Verify VPN credentials and network connectivity.",
                        AiAnalysisStatus.PENDING
                );

        when(ticketService.findTicketEntityById(1))
                .thenReturn(ticket);

        when(aiService.analyzeTicket(any(Ticket.class)))
                .thenReturn(analysis);

        mockMvc.perform(
                        post("/api/ai/analyze/1")
                )
                .andExpect(status().isOk());

        verify(ticketService)
                .findTicketEntityById(1);

        verify(aiService)
                .analyzeTicket(ticket);
    }
}