package com.nabin.employee_helpdesk;

import com.nabin.employee_helpdesk.controller.EmployeeController;
import com.nabin.employee_helpdesk.controller.TicketController;
import com.nabin.employee_helpdesk.service.CustomUserDetailsService;
import com.nabin.employee_helpdesk.service.EmployeeService;
import com.nabin.employee_helpdesk.service.JwtService;
import com.nabin.employee_helpdesk.service.TicketService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        EmployeeController.class,
        TicketController.class
})
@AutoConfigureMockMvc
class SecurityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    EmployeeService employeeService;

    @MockitoBean
    TicketService ticketService;


    // Test 1
    @Test
    void shouldRejectUnauthenticatedRequest() throws Exception {

        mockMvc.perform(
                        get("/api/employees")
                )
                .andExpect(status().isUnauthorized());
    }


    // Test 2
    @Test
    void shouldRejectUnauthenticatedTicketRequest() throws Exception {

        mockMvc.perform(
                        get("/api/tickets")
                )
                .andExpect(status().isUnauthorized());
    }


    // Test 3
    @Test
    void shouldRejectUnauthenticatedEmployeeDelete()
            throws Exception {

        mockMvc.perform(
                        delete("/api/employees/1")
                                .with(
                                        SecurityMockMvcRequestPostProcessors
                                                .csrf()
                                )
                )
                .andExpect(status().isUnauthorized());
    }


    // Test 4
    @Test
    void shouldRejectUnauthenticatedTicketUpdate()
            throws Exception {

        mockMvc.perform(
                        put("/api/tickets/1")
                                .contentType("application/json")
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
                                .with(
                                        SecurityMockMvcRequestPostProcessors
                                                .csrf()
                                )
                )
                .andExpect(status().isUnauthorized());
    }
}