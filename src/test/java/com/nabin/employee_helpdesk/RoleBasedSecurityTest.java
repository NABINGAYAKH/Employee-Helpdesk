package com.nabin.employee_helpdesk;

import com.nabin.employee_helpdesk.config.SecurityConfig;
import com.nabin.employee_helpdesk.controller.EmployeeController;
import com.nabin.employee_helpdesk.controller.TicketController;
import com.nabin.employee_helpdesk.dto.TicketResponse;
import com.nabin.employee_helpdesk.service.CustomUserDetailsService;
import com.nabin.employee_helpdesk.service.EmployeeService;
import com.nabin.employee_helpdesk.service.JwtService;
import com.nabin.employee_helpdesk.service.TicketService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest({
        EmployeeController.class,
        TicketController.class
})
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
class RoleBasedSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private TicketService ticketService;


    // ============================================================
    // DELETE EMPLOYEE
    // ============================================================

    @Test
    @WithMockUser(
            username = "employee@gmail.com",
            roles = "EMPLOYEE"
    )
    void employeeShouldNotDeleteEmployee() throws Exception {

        mockMvc.perform(
                        delete("/api/employees/1")
                )
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(
            username = "support@gmail.com",
            roles = "SUPPORT_AGENT"
    )
    void supportAgentShouldNotDeleteEmployee() throws Exception {

        mockMvc.perform(
                        delete("/api/employees/1")
                )
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(
            username = "admin@gmail.com",
            roles = "ADMIN"
    )
    void adminShouldDeleteEmployee() throws Exception {

        doNothing()
                .when(employeeService)
                .deleteById(1);

        mockMvc.perform(
                        delete("/api/employees/1")
                )
                .andExpect(status().isNoContent());
    }


    // ============================================================
    // UPDATE TICKET
    // ============================================================

    @Test
    @WithMockUser(
            username = "employee@gmail.com",
            roles = "EMPLOYEE"
    )
    void employeeShouldNotUpdateTicket() throws Exception {

        mockMvc.perform(
                        put("/api/tickets/1")
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
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(
            username = "support@gmail.com",
            roles = "SUPPORT_AGENT"
    )
    void supportAgentShouldUpdateTicket() throws Exception {

        TicketResponse response = new TicketResponse(
                1,
                "VPN fixed",
                "VPN connection restored",
                com.nabin.employee_helpdesk.entity.TicketStatus.RESOLVED,
                com.nabin.employee_helpdesk.entity.TicketPriority.HIGH,
                1,
                2
        );

        when(ticketService.updateTicket(
                any(Integer.class),
                any()
        )).thenReturn(response);

        mockMvc.perform(
                        put("/api/tickets/1")
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
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(
            username = "admin@gmail.com",
            roles = "ADMIN"
    )
    void adminShouldUpdateTicket() throws Exception {

        TicketResponse response = new TicketResponse(
                1,
                "VPN fixed",
                "VPN connection restored",
                com.nabin.employee_helpdesk.entity.TicketStatus.RESOLVED,
                com.nabin.employee_helpdesk.entity.TicketPriority.HIGH,
                1,
                2
        );

        when(ticketService.updateTicket(
                any(Integer.class),
                any()
        )).thenReturn(response);

        mockMvc.perform(
                        put("/api/tickets/1")
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
                .andExpect(status().isOk());
    }
}
