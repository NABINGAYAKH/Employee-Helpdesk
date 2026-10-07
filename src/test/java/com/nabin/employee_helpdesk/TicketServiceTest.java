package com.nabin.employee_helpdesk;

import com.nabin.employee_helpdesk.dto.TicketRequest;
import com.nabin.employee_helpdesk.dto.TicketResponse;
import com.nabin.employee_helpdesk.entity.Employee;
import com.nabin.employee_helpdesk.entity.SupportAgent;
import com.nabin.employee_helpdesk.entity.Ticket;
import com.nabin.employee_helpdesk.entity.TicketPriority;
import com.nabin.employee_helpdesk.entity.TicketStatus;
import com.nabin.employee_helpdesk.exception.ResourceNotFoundException;
import com.nabin.employee_helpdesk.repository.EmployeeRepository;
import com.nabin.employee_helpdesk.repository.SupportAgentRepository;
import com.nabin.employee_helpdesk.repository.TicketRepository;
import com.nabin.employee_helpdesk.service.TicketService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SupportAgentRepository supportAgentRepository;

    @InjectMocks
    private TicketService ticketService;


    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }


    // =========================================================
    // CREATE TICKET
    // =========================================================

    @Test
    void shouldCreateTicket() {

        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Nabin");
        employee.setEmail("nabin@gmail.com");
        employee.setDepartment("IT");

        SupportAgent supportAgent = new SupportAgent();
        supportAgent.setId(1);
        supportAgent.setName("Raj");
        supportAgent.setEmail("raj.support@gmail.com");
        supportAgent.setDepartment("IT Support");

        TicketRequest request = new TicketRequest(
                "VPN issue",
                "VPN not connecting",
                TicketStatus.OPEN,
                TicketPriority.HIGH,
                1,
                1
        );

        Ticket savedTicket = new Ticket(
                1,
                "VPN issue",
                "VPN not connecting",
                TicketStatus.OPEN,
                TicketPriority.HIGH,
                employee,
                supportAgent
        );

        when(employeeRepository.findById(1))
                .thenReturn(Optional.of(employee));

        when(supportAgentRepository.findById(1))
                .thenReturn(Optional.of(supportAgent));

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(savedTicket);

        TicketResponse response = ticketService.save(request);

        assertEquals(1, response.getId());
        assertEquals("VPN issue", response.getTitle());
        assertEquals("VPN not connecting", response.getDescription());
        assertEquals(TicketStatus.OPEN, response.getStatus());
        assertEquals(TicketPriority.HIGH, response.getPriority());
        assertEquals(1, response.getEmployeeId());
        assertEquals(1, response.getSupportAgentId());

        verify(employeeRepository).findById(1);
        verify(supportAgentRepository).findById(1);
        verify(ticketRepository).save(any(Ticket.class));
    }


    // =========================================================
    // GET ALL TICKETS - PAGINATION
    // =========================================================

    @Test
    void shouldReturnAllTickets() {

        Employee employee = new Employee();
        employee.setId(1);

        SupportAgent supportAgent = new SupportAgent();
        supportAgent.setId(1);

        Ticket ticket = new Ticket(
                1,
                "VPN issue",
                "VPN not connecting",
                TicketStatus.OPEN,
                TicketPriority.HIGH,
                employee,
                supportAgent
        );

        Page<Ticket> ticketPage =
                new PageImpl<>(
                        List.of(ticket),
                        PageRequest.of(0, 10),
                        1
                );

        when(ticketRepository.findAll(any(Pageable.class)))
                .thenReturn(ticketPage);

        Page<TicketResponse> result =
                ticketService.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getContent().get(0).getId());
        assertEquals(
                "VPN issue",
                result.getContent().get(0).getTitle()
        );

        verify(ticketRepository)
                .findAll(any(Pageable.class));
    }


    // =========================================================
    // FIND BY STATUS
    // =========================================================

    @Test
    void shouldFindTicketsByStatus() {

        Employee employee = new Employee();
        employee.setId(1);

        SupportAgent supportAgent = new SupportAgent();
        supportAgent.setId(1);

        Ticket ticket = new Ticket(
                1,
                "VPN issue",
                "VPN not connecting",
                TicketStatus.OPEN,
                TicketPriority.HIGH,
                employee,
                supportAgent
        );

        Page<Ticket> ticketPage =
                new PageImpl<>(
                        List.of(ticket),
                        PageRequest.of(0, 10),
                        1
                );

        when(ticketRepository.findByStatus(
                eq(TicketStatus.OPEN),
                any(Pageable.class)
        )).thenReturn(ticketPage);

        Page<TicketResponse> result =
                ticketService.findByStatus(
                        TicketStatus.OPEN,
                        PageRequest.of(0, 10)
                );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(
                TicketStatus.OPEN,
                result.getContent().get(0).getStatus()
        );

        verify(ticketRepository).findByStatus(
                eq(TicketStatus.OPEN),
                any(Pageable.class)
        );
    }


    // =========================================================
    // FIND BY PRIORITY
    // =========================================================

    @Test
    void shouldFindTicketsByPriority() {

        Employee employee = new Employee();
        employee.setId(1);

        SupportAgent supportAgent = new SupportAgent();
        supportAgent.setId(1);

        Ticket ticket = new Ticket(
                1,
                "VPN issue",
                "VPN not connecting",
                TicketStatus.OPEN,
                TicketPriority.HIGH,
                employee,
                supportAgent
        );

        Page<Ticket> ticketPage =
                new PageImpl<>(
                        List.of(ticket),
                        PageRequest.of(0, 10),
                        1
                );

        when(ticketRepository.findByPriority(
                eq(TicketPriority.HIGH),
                any(Pageable.class)
        )).thenReturn(ticketPage);

        Page<TicketResponse> result =
                ticketService.findByPriority(
                        TicketPriority.HIGH,
                        PageRequest.of(0, 10)
                );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(
                TicketPriority.HIGH,
                result.getContent().get(0).getPriority()
        );

        verify(ticketRepository).findByPriority(
                eq(TicketPriority.HIGH),
                any(Pageable.class)
        );
    }


    // =========================================================
    // FIND BY STATUS + PRIORITY
    // =========================================================

    @Test
    void shouldFindTicketsByStatusAndPriority() {

        Employee employee = new Employee();
        employee.setId(1);

        SupportAgent supportAgent = new SupportAgent();
        supportAgent.setId(1);

        Ticket ticket = new Ticket(
                1,
                "Critical VPN issue",
                "VPN completely unavailable",
                TicketStatus.OPEN,
                TicketPriority.CRITICAL,
                employee,
                supportAgent
        );

        Page<Ticket> ticketPage =
                new PageImpl<>(
                        List.of(ticket),
                        PageRequest.of(0, 10),
                        1
                );

        when(ticketRepository.findByStatusAndPriority(
                eq(TicketStatus.OPEN),
                eq(TicketPriority.CRITICAL),
                any(Pageable.class)
        )).thenReturn(ticketPage);

        Page<TicketResponse> result =
                ticketService.findByStatusAndPriority(
                        TicketStatus.OPEN,
                        TicketPriority.CRITICAL,
                        PageRequest.of(0, 10)
                );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        assertEquals(
                TicketStatus.OPEN,
                result.getContent().get(0).getStatus()
        );

        assertEquals(
                TicketPriority.CRITICAL,
                result.getContent().get(0).getPriority()
        );

        verify(ticketRepository).findByStatusAndPriority(
                eq(TicketStatus.OPEN),
                eq(TicketPriority.CRITICAL),
                any(Pageable.class)
        );
    }


    // =========================================================
    // FIND TICKET BY ID
    // =========================================================

    @Test
    void shouldFindTicketById() {

        Employee employee = new Employee();
        employee.setId(1);
        employee.setEmail("nabin@gmail.com");

        SupportAgent supportAgent = new SupportAgent();
        supportAgent.setId(1);

        Ticket ticket = new Ticket(
                1,
                "VPN issue",
                "VPN not connecting",
                TicketStatus.OPEN,
                TicketPriority.HIGH,
                employee,
                supportAgent
        );

        // Mock logged-in SUPPORT_AGENT
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        "nabin@gmail.com",
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_SUPPORT_AGENT"
                                )
                        )
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        when(employeeRepository.findByEmail("nabin@gmail.com"))
                .thenReturn(employee);

        when(ticketRepository.findById(1))
                .thenReturn(Optional.of(ticket));

        TicketResponse response =
                ticketService.findById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("VPN issue", response.getTitle());
        assertEquals("VPN not connecting", response.getDescription());
        assertEquals(TicketStatus.OPEN, response.getStatus());
        assertEquals(TicketPriority.HIGH, response.getPriority());
        assertEquals(1, response.getEmployeeId());
        assertEquals(1, response.getSupportAgentId());

        verify(employeeRepository)
                .findByEmail("nabin@gmail.com");

        verify(ticketRepository)
                .findById(1);
    }


    // =========================================================
    // TICKET NOT FOUND
    // =========================================================

    @Test
    void shouldThrowExceptionWhenTicketNotFound() {

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        "nabin@gmail.com",
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_SUPPORT_AGENT"
                                )
                        )
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        Employee employee = new Employee();
        employee.setId(1);
        employee.setEmail("nabin@gmail.com");

        when(employeeRepository.findByEmail("nabin@gmail.com"))
                .thenReturn(employee);

        when(ticketRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.findById(99)
        );

        verify(ticketRepository)
                .findById(99);
    }


    // =========================================================
    // FIND TICKET ENTITY BY ID
    // =========================================================

    @Test
    void shouldFindTicketEntityById() {

        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setTitle("VPN issue");

        when(ticketRepository.findById(1))
                .thenReturn(Optional.of(ticket));

        Ticket result =
                ticketService.findTicketEntityById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("VPN issue", result.getTitle());

        verify(ticketRepository)
                .findById(1);
    }


    @Test
    void shouldThrowExceptionWhenTicketEntityNotFound() {

        when(ticketRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.findTicketEntityById(99)
        );

        verify(ticketRepository)
                .findById(99);
    }


    // =========================================================
    // UPDATE TICKET
    // =========================================================

    @Test
    void shouldUpdateTicket() {

        Employee employee = new Employee();
        employee.setId(1);

        SupportAgent supportAgent = new SupportAgent();
        supportAgent.setId(1);

        Ticket existingTicket = new Ticket(
                1,
                "Old title",
                "Old description",
                TicketStatus.OPEN,
                TicketPriority.LOW,
                employee,
                supportAgent
        );

        TicketRequest request = new TicketRequest(
                "Updated title",
                "Updated description",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                1,
                1
        );

        Ticket updatedTicket = new Ticket(
                1,
                "Updated title",
                "Updated description",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                employee,
                supportAgent
        );

        when(ticketRepository.findById(1))
                .thenReturn(Optional.of(existingTicket));

        when(employeeRepository.findById(1))
                .thenReturn(Optional.of(employee));

        when(supportAgentRepository.findById(1))
                .thenReturn(Optional.of(supportAgent));

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(updatedTicket);

        TicketResponse response =
                ticketService.updateTicket(1, request);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(
                "Updated title",
                response.getTitle()
        );
        assertEquals(
                "Updated description",
                response.getDescription()
        );
        assertEquals(
                TicketStatus.IN_PROGRESS,
                response.getStatus()
        );
        assertEquals(
                TicketPriority.HIGH,
                response.getPriority()
        );

        verify(ticketRepository)
                .findById(1);

        verify(employeeRepository)
                .findById(1);

        verify(supportAgentRepository)
                .findById(1);

        verify(ticketRepository)
                .save(existingTicket);
    }


    // =========================================================
    // UPDATE NON-EXISTING TICKET
    // =========================================================

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingTicket() {

        TicketRequest request = new TicketRequest(
                "Updated title",
                "Updated description",
                TicketStatus.IN_PROGRESS,
                TicketPriority.HIGH,
                1,
                1
        );

        when(ticketRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.updateTicket(99, request)
        );

        verify(ticketRepository)
                .findById(99);

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }


    // =========================================================
    // INVALID STATUS TRANSITION
    // =========================================================

    @Test
    void shouldRejectInvalidStatusTransition() {

        Employee employee = new Employee();
        employee.setId(1);

        SupportAgent supportAgent = new SupportAgent();
        supportAgent.setId(1);

        Ticket existingTicket = new Ticket(
                1,
                "VPN issue",
                "VPN not connecting",
                TicketStatus.OPEN,
                TicketPriority.HIGH,
                employee,
                supportAgent
        );

        TicketRequest request = new TicketRequest(
                "VPN issue",
                "VPN not connecting",
                TicketStatus.RESOLVED,
                TicketPriority.HIGH,
                1,
                1
        );

        when(ticketRepository.findById(1))
                .thenReturn(Optional.of(existingTicket));

        assertThrows(
                IllegalStateException.class,
                () -> ticketService.updateTicket(1, request)
        );

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }


    // =========================================================
    // DELETE TICKET
    // =========================================================

    @Test
    void shouldDeleteTicket() {

        when(ticketRepository.existsById(1))
                .thenReturn(true);

        ticketService.deleteById(1);

        verify(ticketRepository)
                .existsById(1);

        verify(ticketRepository)
                .deleteById(1);
    }


    // =========================================================
    // DELETE NON-EXISTING TICKET
    // =========================================================

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingTicket() {

        when(ticketRepository.existsById(99))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.deleteById(99)
        );

        verify(ticketRepository)
                .existsById(99);

        verify(ticketRepository, never())
                .deleteById(99);
    }
}