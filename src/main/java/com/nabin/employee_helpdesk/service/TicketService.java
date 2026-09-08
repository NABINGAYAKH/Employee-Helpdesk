package com.nabin.employee_helpdesk.service;

import com.nabin.employee_helpdesk.dto.TicketRequest;
import com.nabin.employee_helpdesk.dto.TicketResponse;
import com.nabin.employee_helpdesk.entity.*;
import com.nabin.employee_helpdesk.exception.ResourceNotFoundException;
import com.nabin.employee_helpdesk.repository.EmployeeRepository;
import com.nabin.employee_helpdesk.repository.SupportAgentRepository;
import com.nabin.employee_helpdesk.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SupportAgentRepository supportAgentRepository;

    public TicketResponse save(TicketRequest request) {
        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(request.getStatus());
        ticket.setPriority(request.getPriority());

        Employee employee=employeeRepository
                .findById(request.getEmployeeId())
                .orElse(null);

        if(employee == null){
            throw new ResourceNotFoundException(
                    "Employee not found with id: "+request.getEmployeeId()
            );
        }
        ticket.setEmployee(employee);

        SupportAgent supportAgent = supportAgentRepository
                .findById(request.getSupportAgentId())
                .orElse(null);

        if (supportAgent == null) {
            throw new ResourceNotFoundException(
                    "Support agent not found with id: " + request.getSupportAgentId()
            );
        }

        ticket.setSupportAgent(supportAgent);

        Ticket savedTicket = ticketRepository.save(ticket);

        TicketResponse response = new TicketResponse();

        response.setId(savedTicket.getId());
        response.setTitle(savedTicket.getTitle());
        response.setDescription(savedTicket.getDescription());
        response.setStatus(savedTicket.getStatus());
        response.setPriority(savedTicket.getPriority());
        response.setEmployeeId(savedTicket.getEmployee().getId());
        response.setSupportAgentId(savedTicket.getSupportAgent().getId());

        return response;
    }

    public List<TicketResponse> findAll(){
        List<Ticket> tickets=ticketRepository.findAll();
        List<TicketResponse> responses = new ArrayList<>();
        for(Ticket ticket : tickets) {
            TicketResponse response = new TicketResponse();
            response.setId(ticket.getId());
            response.setTitle(ticket.getTitle());
            response.setDescription(ticket.getDescription());
            response.setStatus(ticket.getStatus());
            response.setPriority(ticket.getPriority());
            if(ticket.getEmployee()!=null){
                response.setEmployeeId(ticket.getEmployee().getId());
            }
            if (ticket.getSupportAgent() != null) {
                response.setSupportAgentId(ticket.getSupportAgent().getId());
            }
            responses.add(response);
        }
        return responses;
    }

    public TicketResponse findById(int id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Employee employee = employeeRepository.findByEmail(email);

        Optional<Ticket> ticket = ticketRepository.findById(id);

        if (ticket.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Ticket not found with id: " + id);
        }

        // Check whether the logged-in user is an EMPLOYEE
        boolean isEmployee = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));

        // EMPLOYEE can access only their own tickets
        // SUPPORT_AGENT and ADMIN can access any ticket
        if (isEmployee) {

            if (employee == null
                    || ticket.get().getEmployee() == null
                    || !ticket.get().getEmployee().getId().equals(employee.getId())) {

                throw new AccessDeniedException(
                        "You are not allowed to access this ticket");
            }
        }

        TicketResponse response = new TicketResponse();

        response.setId(ticket.get().getId());
        response.setTitle(ticket.get().getTitle());
        response.setDescription(ticket.get().getDescription());
        response.setStatus(ticket.get().getStatus());
        response.setPriority(ticket.get().getPriority());

        if (ticket.get().getEmployee() != null) {
            response.setEmployeeId(
                    ticket.get().getEmployee().getId());
        }

        if (ticket.get().getSupportAgent() != null) {
            response.setSupportAgentId(
                    ticket.get().getSupportAgent().getId());
        }

        return response;
    }

    public Ticket findTicketEntityById(int id) {

        Optional<Ticket> ticket = ticketRepository.findById(id);

        if (ticket.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Ticket not found with id: " + id);
        }

        return ticket.get();
    }

    public TicketResponse updateTicket(int id, TicketRequest request){
        Ticket existingTicket =ticketRepository.findById(id).orElse(null);
        if(existingTicket!=null){
            existingTicket.setTitle(request.getTitle());
            System.out.println("Current Status: " + existingTicket.getStatus());
            System.out.println("New Status: " + request.getStatus());
            validateStatusTransition(
                    existingTicket.getStatus(),
                    request.getStatus()
            );
            existingTicket.setStatus(request.getStatus());
            existingTicket.setDescription(request.getDescription());
            existingTicket.setPriority(request.getPriority());
            Employee employee = employeeRepository.findById(request.getEmployeeId()).orElse(null);
            if (employee == null) {
                throw new ResourceNotFoundException(
                        "Employee not found with id: " + request.getEmployeeId()
                );
            }
            existingTicket.setEmployee(employee);
            SupportAgent supportAgent = supportAgentRepository
                    .findById(request.getSupportAgentId())
                    .orElse(null);

            if (supportAgent == null) {
                throw new ResourceNotFoundException(
                        "Support agent not found with id: " + request.getSupportAgentId()
                );
            }
            existingTicket.setSupportAgent(supportAgent);
            Ticket savedTicket = ticketRepository.save(existingTicket);

            TicketResponse response = new TicketResponse();
            response.setId(savedTicket.getId());
            response.setTitle(savedTicket.getTitle());
            response.setStatus(savedTicket.getStatus());
            response.setDescription(savedTicket.getDescription());
            response.setPriority(savedTicket.getPriority());
            response.setEmployeeId(savedTicket.getEmployee().getId());
            response.setSupportAgentId(savedTicket.getSupportAgent().getId());
            return response;
        }
        throw new ResourceNotFoundException("Ticket not found with id: "+id);
    }


    private void validateStatusTransition(
            TicketStatus currentStatus,
            TicketStatus newStatus) {

        if (currentStatus == TicketStatus.OPEN
                && newStatus != TicketStatus.OPEN
                && newStatus != TicketStatus.IN_PROGRESS) {

            throw new IllegalStateException(
                    "OPEN ticket can only move to IN_PROGRESS"
            );
        }

        if (currentStatus == TicketStatus.IN_PROGRESS
                && newStatus != TicketStatus.IN_PROGRESS
                && newStatus != TicketStatus.RESOLVED) {

            throw new IllegalStateException(
                    "IN_PROGRESS ticket can only move to RESOLVED"
            );
        }

        if (currentStatus == TicketStatus.RESOLVED
                && newStatus != TicketStatus.RESOLVED
                && newStatus != TicketStatus.CLOSED) {

            throw new IllegalStateException(
                    "RESOLVED ticket can only move to CLOSED"
            );
        }

        if (currentStatus == TicketStatus.CLOSED
                && newStatus != TicketStatus.CLOSED) {

            throw new IllegalStateException(
                    "CLOSED ticket cannot be changed"
            );
        }
    }


    public void deleteById(int id){
        if(ticketRepository.existsById(id)){
            ticketRepository.deleteById(id);
        }else{
            throw new ResourceNotFoundException("Ticket not found with id: "+id);
        }
    }

    public List<TicketResponse> findByStatus(TicketStatus status){
        List<Ticket> tickets = ticketRepository.findByStatus(status);
        List<TicketResponse> responses = new ArrayList<>();

        for(Ticket ticket:tickets){
            TicketResponse response = new TicketResponse();
            response.setId(ticket.getId());
            response.setTitle(ticket.getTitle());
            response.setStatus(ticket.getStatus());
            response.setDescription(ticket.getDescription());
            response.setPriority(ticket.getPriority());
            if(ticket.getEmployee()!=null){
                response.setEmployeeId(ticket.getEmployee().getId());
            }
            if (ticket.getSupportAgent() != null) {
                response.setSupportAgentId(ticket.getSupportAgent().getId());
            }
            responses.add(response);
        }
    return responses;
    }

    public List<TicketResponse> findByPriority(TicketPriority priority){
        List<Ticket> tickets =ticketRepository.findByPriority(priority);
        List<TicketResponse> responses = new ArrayList<>();

        for(Ticket ticket:tickets){
            TicketResponse response = new TicketResponse();
            response.setId(ticket.getId());
            response.setTitle(ticket.getTitle());
            response.setStatus(ticket.getStatus());
            response.setDescription(ticket.getDescription());
            response.setPriority(ticket.getPriority());

            if(ticket.getEmployee()!=null){
                response.setEmployeeId(ticket.getEmployee().getId());
            }
            if (ticket.getSupportAgent() != null) {
                response.setSupportAgentId(ticket.getSupportAgent().getId());
            }

            responses.add(response);
        }
        return responses;
    }

    public List<TicketResponse> findByStatusAndPriority(TicketStatus status, TicketPriority priority){
        List<Ticket> tickets = ticketRepository.findByStatusAndPriority(status, priority);

        List<TicketResponse> responses = new ArrayList<>();

        for(Ticket ticket:tickets){
            TicketResponse response = new TicketResponse();
            response.setId(ticket.getId());
            response.setTitle(ticket.getTitle());
            response.setStatus(ticket.getStatus());
            response.setDescription(ticket.getDescription());
            response.setPriority(ticket.getPriority());

            if(ticket.getEmployee()!=null){
                response.setEmployeeId(ticket.getEmployee().getId());
            }
            if (ticket.getSupportAgent() != null) {
                response.setSupportAgentId(ticket.getSupportAgent().getId());
            }

            responses.add(response);
        }
        return responses;
    }
}
