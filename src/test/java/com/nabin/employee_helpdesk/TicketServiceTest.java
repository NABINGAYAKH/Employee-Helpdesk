package com.nabin.employee_helpdesk;

import com.nabin.employee_helpdesk.dto.TicketRequest;
import com.nabin.employee_helpdesk.dto.TicketResponse;
import com.nabin.employee_helpdesk.entity.*;
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

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {


    @Mock
    TicketRepository ticketRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    SupportAgentRepository supportAgentRepository;

    @Mock
    Authentication authentication;

    @Mock
    SecurityContext securityContext;


    @InjectMocks
    TicketService ticketService;


    @Test
    void shouldSaveTicket() {

        // Arrange

        TicketRequest request = new TicketRequest();

        request.setTitle("Laptop Issue");
        request.setDescription("Laptop is not starting");
        request.setStatus(TicketStatus.OPEN);
        request.setPriority(TicketPriority.HIGH);
        request.setEmployeeId(1);
        request.setSupportAgentId(2);


        Employee employee = new Employee();

        employee.setId(1);
        employee.setName("Nabin");
        employee.setEmail("nabin@gmail.com");
        employee.setDepartment("IT");


        SupportAgent supportAgent = new SupportAgent();

        supportAgent.setId(2);
        supportAgent.setName("John");
        supportAgent.setEmail("john@gmail.com");
        supportAgent.setDepartment("Support");


        Ticket savedTicket = new Ticket();

        savedTicket.setId(1);
        savedTicket.setTitle("Laptop Issue");
        savedTicket.setDescription("Laptop is not starting");
        savedTicket.setStatus(TicketStatus.OPEN);
        savedTicket.setPriority(TicketPriority.HIGH);
        savedTicket.setEmployee(employee);
        savedTicket.setSupportAgent(supportAgent);


        when(employeeRepository.findById(1))
                .thenReturn(Optional.of(employee));


        when(supportAgentRepository.findById(2))
                .thenReturn(Optional.of(supportAgent));


        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(savedTicket);


        // Act

        TicketResponse response =
                ticketService.save(request);


        // Assert

        assertNotNull(response);

        assertEquals(1, response.getId());

        assertEquals(
                "Laptop Issue",
                response.getTitle()
        );

        assertEquals(
                "Laptop is not starting",
                response.getDescription()
        );

        assertEquals(
                TicketStatus.OPEN,
                response.getStatus()
        );

        assertEquals(
                TicketPriority.HIGH,
                response.getPriority()
        );

        assertEquals(
                1,
                response.getEmployeeId()
        );

        assertEquals(
                2,
                response.getSupportAgentId()
        );


        // Verify

        verify(employeeRepository)
                .findById(1);

        verify(supportAgentRepository)
                .findById(2);

        verify(ticketRepository)
                .save(any(Ticket.class));
    }


    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {

        // Arrange

        TicketRequest request = new TicketRequest();

        request.setTitle("Laptop Issue");
        request.setDescription("Laptop is not starting");
        request.setStatus(TicketStatus.OPEN);
        request.setPriority(TicketPriority.HIGH);
        request.setEmployeeId(999);
        request.setSupportAgentId(2);


        when(employeeRepository.findById(999))
                .thenReturn(Optional.empty());


        // Act + Assert

        assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.save(request)
        );


        // Verify

        verify(employeeRepository)
                .findById(999);


        verify(supportAgentRepository, never())
                .findById(2);


        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }


    @Test
    void shouldThrowExceptionWhenSupportAgentNotFound() {

        // Arrange

        TicketRequest request = new TicketRequest();

        request.setTitle("Laptop Issue");
        request.setDescription("Laptop is not starting");
        request.setStatus(TicketStatus.OPEN);
        request.setPriority(TicketPriority.HIGH);
        request.setEmployeeId(1);
        request.setSupportAgentId(999);


        Employee employee = new Employee();

        employee.setId(1);
        employee.setName("Nabin");
        employee.setEmail("nabin@gmail.com");
        employee.setDepartment("IT");


        when(employeeRepository.findById(1))
                .thenReturn(Optional.of(employee));


        when(supportAgentRepository.findById(999))
                .thenReturn(Optional.empty());


        // Act + Assert

        assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.save(request)
        );


        // Verify

        verify(employeeRepository)
                .findById(1);


        verify(supportAgentRepository)
                .findById(999);


        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }



    @Test
    void shouldFindAllTickets() {

        // Arrange

        Employee employee = new Employee();

        employee.setId(1);
        employee.setName("Nabin");


        SupportAgent supportAgent = new SupportAgent();

        supportAgent.setId(2);
        supportAgent.setName("John");


        Ticket ticket1 = new Ticket();

        ticket1.setId(1);
        ticket1.setTitle("Laptop Issue");
        ticket1.setDescription("Laptop is not starting");
        ticket1.setStatus(TicketStatus.OPEN);
        ticket1.setPriority(TicketPriority.HIGH);
        ticket1.setEmployee(employee);
        ticket1.setSupportAgent(supportAgent);


        Ticket ticket2 = new Ticket();

        ticket2.setId(2);
        ticket2.setTitle("Password Issue");
        ticket2.setDescription("Cannot login");
        ticket2.setStatus(TicketStatus.IN_PROGRESS);
        ticket2.setPriority(TicketPriority.MEDIUM);
        ticket2.setEmployee(employee);
        ticket2.setSupportAgent(supportAgent);


        when(ticketRepository.findAll())
                .thenReturn(List.of(ticket1, ticket2));


        // Act

        List<TicketResponse> responses =
                ticketService.findAll();


        // Assert

        assertNotNull(responses);

        assertEquals(
                2,
                responses.size()
        );


        assertEquals(
                1,
                responses.get(0).getId()
        );

        assertEquals(
                "Laptop Issue",
                responses.get(0).getTitle()
        );

        assertEquals(
                TicketStatus.OPEN,
                responses.get(0).getStatus()
        );

        assertEquals(
                TicketPriority.HIGH,
                responses.get(0).getPriority()
        );

        assertEquals(
                1,
                responses.get(0).getEmployeeId()
        );

        assertEquals(
                2,
                responses.get(0).getSupportAgentId()
        );


        assertEquals(
                2,
                responses.get(1).getId()
        );

        assertEquals(
                "Password Issue",
                responses.get(1).getTitle()
        );

        assertEquals(
                TicketStatus.IN_PROGRESS,
                responses.get(1).getStatus()
        );

        assertEquals(
                TicketPriority.MEDIUM,
                responses.get(1).getPriority()
        );


        // Verify

        verify(ticketRepository)
                .findAll();
    }


    @Test
    void employeeShouldAccessOwnTicket() {

        // Arrange

        Employee employee = new Employee();

        employee.setId(1);
        employee.setName("Nabin");
        employee.setEmail("nabin@gmail.com");
        employee.setDepartment("IT");


        Ticket ticket = new Ticket();

        ticket.setId(1);
        ticket.setTitle("Laptop Issue");
        ticket.setDescription("Laptop is not starting");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setEmployee(employee);


        // Mock SecurityContext

        when(securityContext.getAuthentication())
                .thenReturn(authentication);


        // Logged-in user's email

        when(authentication.getName())
                .thenReturn("nabin@gmail.com");


        // Create employee role

        Collection<GrantedAuthority> authorities =
                new ArrayList<>();

        authorities.add(
                new SimpleGrantedAuthority("ROLE_EMPLOYEE")
        );


        // IMPORTANT:
        // Use doReturn instead of when(...).thenReturn(...)

        doReturn(authorities)
                .when(authentication)
                .getAuthorities();


        // Find employee by email

        when(employeeRepository.findByEmail(
                "nabin@gmail.com"
        )).thenReturn(employee);


        // Find ticket

        when(ticketRepository.findById(1))
                .thenReturn(Optional.of(ticket));


        // Put SecurityContext into SecurityContextHolder

        SecurityContextHolder.setContext(
                securityContext
        );


        // Act

        TicketResponse response =
                ticketService.findById(1);


        // Assert

        assertNotNull(response);

        assertEquals(
                1,
                response.getId()
        );

        assertEquals(
                "Laptop Issue",
                response.getTitle()
        );

        assertEquals(
                TicketStatus.OPEN,
                response.getStatus()
        );

        assertEquals(
                TicketPriority.HIGH,
                response.getPriority()
        );

        assertEquals(
                1,
                response.getEmployeeId()
        );


        // Verify

        verify(employeeRepository)
                .findByEmail("nabin@gmail.com");

        verify(ticketRepository)
                .findById(1);
    }

    @Test
    void employeeShouldNotAccessAnotherEmployeesTicket() {

        // Arrange

        // Logged-in employee: Nabin
        Employee nabin = new Employee();

        nabin.setId(1);
        nabin.setName("Nabin");
        nabin.setEmail("nabin@gmail.com");
        nabin.setDepartment("IT");


        // Ticket owner: Rahul
        Employee rahul = new Employee();

        rahul.setId(7);
        rahul.setName("Rahul");
        rahul.setEmail("rahul@gmail.com");
        rahul.setDepartment("IT");


        // Ticket belongs to Rahul
        Ticket ticket = new Ticket();

        ticket.setId(1);
        ticket.setTitle("Laptop Issue");
        ticket.setDescription("Laptop is not starting");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setEmployee(rahul);


        // Spring Security says Nabin is logged in

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn("nabin@gmail.com");


        // Give Nabin EMPLOYEE role

        Collection<GrantedAuthority> authorities =
                new ArrayList<>();

        authorities.add(
                new SimpleGrantedAuthority("ROLE_EMPLOYEE")
        );


        doReturn(authorities)
                .when(authentication)
                .getAuthorities();


        // Find logged-in employee

        when(employeeRepository.findByEmail(
                "nabin@gmail.com"
        )).thenReturn(nabin);


        // Find requested ticket

        when(ticketRepository.findById(1))
                .thenReturn(Optional.of(ticket));


        // Put fake SecurityContext into SecurityContextHolder

        SecurityContextHolder.setContext(
                securityContext
        );


        // Act + Assert

        assertThrows(
                AccessDeniedException.class,
                () -> ticketService.findById(1)
        );


        // Verify

        verify(employeeRepository)
                .findByEmail("nabin@gmail.com");

        verify(ticketRepository)
                .findById(1);
    }

    @Test
    void shouldThrowExceptionWhenTicketNotFound() {

        // Arrange

        // Create logged-in employee

        Employee employee = new Employee();

        employee.setId(1);
        employee.setName("Nabin");
        employee.setEmail("nabin@gmail.com");
        employee.setDepartment("IT");


        // Tell SecurityContext to return Authentication

        when(securityContext.getAuthentication())
                .thenReturn(authentication);


        // Logged-in user's email

        when(authentication.getName())
                .thenReturn("nabin@gmail.com");


        // Find logged-in employee

        when(employeeRepository.findByEmail(
                "nabin@gmail.com"
        )).thenReturn(employee);


        // Ticket does NOT exist

        when(ticketRepository.findById(999))
                .thenReturn(Optional.empty());


        // Put fake SecurityContext into SecurityContextHolder

        SecurityContextHolder.setContext(
                securityContext
        );


        // Act + Assert

        assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.findById(999)
        );


        // Verify

        verify(employeeRepository)
                .findByEmail("nabin@gmail.com");

        verify(ticketRepository)
                .findById(999);
    }

    @Test
    void shoudFindTicketEntityId(){

        //Arrange
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setTitle("Laptop Issue");
        ticket.setDescription("Laptop is not starting");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.HIGH);
        when(ticketRepository.findById(1))
                .thenReturn(Optional.of(ticket));

        //Act
        Ticket result = ticketService.findTicketEntityById(1);

        //Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Laptop Issue", result.getTitle());
        assertEquals("Laptop is not starting", result.getDescription());
        assertEquals(TicketStatus.OPEN, result.getStatus());
        assertEquals(TicketPriority.HIGH, result.getPriority());

        //Verify
        verify(ticketRepository).findById(1);
    }

    @Test
    void shoudThowExceptionWhenTicketEntityNotFound(){

        //Arrange
        when(ticketRepository.findById(999))
                .thenReturn(Optional.empty());

        //Act + Assert
        assertThrows(ResourceNotFoundException.class,
                ()->ticketService.findTicketEntityById(999));

        //Verify
        verify(ticketRepository).findById(999);
    }

    @Test
    void shouldUpdateTicket() {

        // Arrange

        // Existing employee

        Employee employee = new Employee();

        employee.setId(1);
        employee.setName("Nabin");
        employee.setEmail("nabin@gmail.com");
        employee.setDepartment("IT");


        // Existing support agent

        SupportAgent supportAgent = new SupportAgent();

        supportAgent.setId(2);
        supportAgent.setName("John");
        supportAgent.setEmail("john@gmail.com");
        supportAgent.setDepartment("Support");


        // Existing ticket in database

        Ticket ticket = new Ticket();

        ticket.setId(1);
        ticket.setTitle("Laptop Issue");
        ticket.setDescription("Laptop is not starting");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setEmployee(employee);
        ticket.setSupportAgent(supportAgent);


        // Update request

        TicketRequest request = new TicketRequest();

        request.setTitle("Laptop Issue Updated");
        request.setDescription("Laptop is still not starting");
        request.setStatus(TicketStatus.IN_PROGRESS);
        request.setPriority(TicketPriority.HIGH);
        request.setEmployeeId(1);
        request.setSupportAgentId(2);


        // Repository finds existing ticket

        when(ticketRepository.findById(1))
                .thenReturn(Optional.of(ticket));


        // Repository finds employee

        when(employeeRepository.findById(1))
                .thenReturn(Optional.of(employee));


        // Repository finds support agent

        when(supportAgentRepository.findById(2))
                .thenReturn(Optional.of(supportAgent));


        // Repository saves updated ticket

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(ticket);


        // Act

        TicketResponse response =
                ticketService.updateTicket(1, request);


        // Assert

        assertNotNull(response);

        assertEquals(
                1,
                response.getId()
        );

        assertEquals(
                "Laptop Issue Updated",
                response.getTitle()
        );

        assertEquals(
                "Laptop is still not starting",
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

        assertEquals(
                1,
                response.getEmployeeId()
        );

        assertEquals(
                2,
                response.getSupportAgentId()
        );


        // Verify

        verify(ticketRepository)
                .findById(1);

        verify(employeeRepository)
                .findById(1);

        verify(supportAgentRepository)
                .findById(2);

        verify(ticketRepository)
                .save(any(Ticket.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingTicket() {

        // Arrange

        TicketRequest request = new TicketRequest();

        request.setTitle("Updated Ticket");
        request.setDescription("Updated description");
        request.setStatus(TicketStatus.IN_PROGRESS);
        request.setPriority(TicketPriority.HIGH);
        request.setEmployeeId(1);
        request.setSupportAgentId(2);

        when(ticketRepository.findById(99))
                .thenReturn(Optional.empty());

        // Act + Assert

        assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.updateTicket(99, request)
        );

        // Verify

        verify(ticketRepository).findById(99);

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }

    @Test
    void shouldThrowExceptionForInvalidStatusTransition() {

        // Arrange

        Employee employee = new Employee();
        employee.setId(1);

        SupportAgent supportAgent = new SupportAgent();
        supportAgent.setId(2);

        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setTitle("Laptop Issue");
        ticket.setDescription("Laptop not starting");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setEmployee(employee);
        ticket.setSupportAgent(supportAgent);

        TicketRequest request = new TicketRequest();
        request.setTitle("Laptop Issue");
        request.setDescription("Laptop still not starting");
        request.setStatus(TicketStatus.RESOLVED);
        request.setPriority(TicketPriority.HIGH);
        request.setEmployeeId(1);
        request.setSupportAgentId(2);

        when(ticketRepository.findById(1))
                .thenReturn(Optional.of(ticket));

        // Act + Assert

        assertThrows(
                IllegalStateException.class,
                () -> ticketService.updateTicket(1, request)
        );

        // Verify

        verify(ticketRepository).findById(1);

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }


    @Test
    void shouldThrowExceptionWhenEmployeeNotFoundDuringUpdate() {

        // Arrange

        Employee employee = new Employee();
        employee.setId(1);

        SupportAgent supportAgent = new SupportAgent();
        supportAgent.setId(2);

        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setTitle("Laptop Issue");
        ticket.setDescription("Laptop not starting");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setEmployee(employee);
        ticket.setSupportAgent(supportAgent);

        TicketRequest request = new TicketRequest();
        request.setTitle("Updated Laptop Issue");
        request.setDescription("Still not starting");
        request.setStatus(TicketStatus.IN_PROGRESS);
        request.setPriority(TicketPriority.HIGH);

        // Employee ID 99 does not exist
        request.setEmployeeId(99);
        request.setSupportAgentId(2);

        when(ticketRepository.findById(1))
                .thenReturn(Optional.of(ticket));

        when(employeeRepository.findById(99))
                .thenReturn(Optional.empty());

        // Act + Assert

        assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.updateTicket(1, request)
        );

        // Verify

        verify(ticketRepository).findById(1);

        verify(employeeRepository).findById(99);

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }


    @Test
    void shouldThrowExceptionWhenSupportAgentNotFoundDuringUpdate() {

        // Arrange

        Employee employee = new Employee();
        employee.setId(1);

        SupportAgent supportAgent = new SupportAgent();
        supportAgent.setId(2);

        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setTitle("Laptop Issue");
        ticket.setDescription("Laptop not starting");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setEmployee(employee);
        ticket.setSupportAgent(supportAgent);

        TicketRequest request = new TicketRequest();
        request.setTitle("Updated Laptop Issue");
        request.setDescription("Still not starting");
        request.setStatus(TicketStatus.IN_PROGRESS);
        request.setPriority(TicketPriority.HIGH);
        request.setEmployeeId(1);

        // Support Agent ID 99 does not exist
        request.setSupportAgentId(99);

        when(ticketRepository.findById(1))
                .thenReturn(Optional.of(ticket));

        when(employeeRepository.findById(1))
                .thenReturn(Optional.of(employee));

        when(supportAgentRepository.findById(99))
                .thenReturn(Optional.empty());

        // Act + Assert

        assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.updateTicket(1, request)
        );

        // Verify

        verify(ticketRepository).findById(1);

        verify(employeeRepository).findById(1);

        verify(supportAgentRepository).findById(99);

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }


    @Test
    void shouldDeleteTicket() {

        // Arrange

        when(ticketRepository.existsById(1))
                .thenReturn(true);

        // Act

        ticketService.deleteById(1);

        // Verify

        verify(ticketRepository).existsById(1);

        verify(ticketRepository).deleteById(1);
    }


    @Test
    void shouldThrowExceptionWhenDeletingNonExistingTicket() {

        // Arrange

        when(ticketRepository.existsById(99))
                .thenReturn(false);

        // Act + Assert

        assertThrows(
                ResourceNotFoundException.class,
                () -> ticketService.deleteById(99)
        );

        // Verify

        verify(ticketRepository).existsById(99);

        verify(ticketRepository, never())
                .deleteById(99);
    }


    @Test
    void shouldFindTicketsByStatus() {

        // Arrange

        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Nabin");
        employee.setEmail("nabin@gmail.com");
        employee.setDepartment("IT");

        SupportAgent supportAgent = new SupportAgent();
        supportAgent.setId(2);
        supportAgent.setName("John");
        supportAgent.setEmail("john@gmail.com");
        supportAgent.setDepartment("Support");

        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setTitle("Laptop Issue");
        ticket.setDescription("Laptop not starting");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setEmployee(employee);
        ticket.setSupportAgent(supportAgent);

        when(ticketRepository.findByStatus(TicketStatus.OPEN))
                .thenReturn(List.of(ticket));

        // Act

        List<TicketResponse> responses =
                ticketService.findByStatus(TicketStatus.OPEN);

        // Assert

        assertNotNull(responses);
        assertEquals(1, responses.size());

        assertEquals(1, responses.get(0).getId());
        assertEquals("Laptop Issue", responses.get(0).getTitle());
        assertEquals(TicketStatus.OPEN, responses.get(0).getStatus());
        assertEquals(TicketPriority.HIGH, responses.get(0).getPriority());
        assertEquals(1, responses.get(0).getEmployeeId());
        assertEquals(2, responses.get(0).getSupportAgentId());

        // Verify

        verify(ticketRepository)
                .findByStatus(TicketStatus.OPEN);
    }

    @Test
    void shouldFindTicketsByPriority() {

        // Arrange

        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Nabin");
        employee.setEmail("nabin@gmail.com");
        employee.setDepartment("IT");

        SupportAgent supportAgent = new SupportAgent();
        supportAgent.setId(2);
        supportAgent.setName("John");
        supportAgent.setEmail("john@gmail.com");
        supportAgent.setDepartment("Support");

        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setTitle("Laptop Issue");
        ticket.setDescription("Laptop not starting");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setEmployee(employee);
        ticket.setSupportAgent(supportAgent);

        when(ticketRepository.findByPriority(TicketPriority.HIGH))
                .thenReturn(List.of(ticket));

        // Act

        List<TicketResponse> responses =
                ticketService.findByPriority(TicketPriority.HIGH);

        // Assert

        assertNotNull(responses);
        assertEquals(1, responses.size());

        assertEquals(1, responses.get(0).getId());
        assertEquals("Laptop Issue", responses.get(0).getTitle());
        assertEquals(TicketPriority.HIGH, responses.get(0).getPriority());
        assertEquals(TicketStatus.OPEN, responses.get(0).getStatus());
        assertEquals(1, responses.get(0).getEmployeeId());
        assertEquals(2, responses.get(0).getSupportAgentId());

        // Verify

        verify(ticketRepository)
                .findByPriority(TicketPriority.HIGH);
    }

    @Test
    void shouldFindTicketsByStatusAndPriority() {

        // Arrange

        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Nabin");
        employee.setEmail("nabin@gmail.com");
        employee.setDepartment("IT");

        SupportAgent supportAgent = new SupportAgent();
        supportAgent.setId(2);
        supportAgent.setName("John");
        supportAgent.setEmail("john@gmail.com");
        supportAgent.setDepartment("Support");

        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setTitle("Laptop Issue");
        ticket.setDescription("Laptop not starting");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setEmployee(employee);
        ticket.setSupportAgent(supportAgent);

        when(ticketRepository.findByStatusAndPriority(
                TicketStatus.OPEN,
                TicketPriority.HIGH
        )).thenReturn(List.of(ticket));

        // Act

        List<TicketResponse> responses =
                ticketService.findByStatusAndPriority(
                        TicketStatus.OPEN,
                        TicketPriority.HIGH
                );

        // Assert

        assertNotNull(responses);
        assertEquals(1, responses.size());

        assertEquals(1, responses.get(0).getId());
        assertEquals("Laptop Issue", responses.get(0).getTitle());
        assertEquals(TicketStatus.OPEN, responses.get(0).getStatus());
        assertEquals(TicketPriority.HIGH, responses.get(0).getPriority());
        assertEquals(1, responses.get(0).getEmployeeId());
        assertEquals(2, responses.get(0).getSupportAgentId());

        // Verify

        verify(ticketRepository).findByStatusAndPriority(
                TicketStatus.OPEN,
                TicketPriority.HIGH
        );
    }


    @AfterEach
    void tearDown() {

        SecurityContextHolder.clearContext();
    }
}