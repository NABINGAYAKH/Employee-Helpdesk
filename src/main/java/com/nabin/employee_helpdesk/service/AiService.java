package com.nabin.employee_helpdesk.service;

import com.nabin.employee_helpdesk.dto.AiTicketAnalysis;
import com.nabin.employee_helpdesk.entity.AiAnalysis;
import com.nabin.employee_helpdesk.entity.AiAnalysisStatus;
import com.nabin.employee_helpdesk.entity.Ticket;
import com.nabin.employee_helpdesk.entity.TicketPriority;
import com.nabin.employee_helpdesk.exception.ResourceNotFoundException;
import com.nabin.employee_helpdesk.repository.AiAnalysisRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AiService {

    private final AiAnalysisRepository aiAnalysisRepository;

    public AiService(AiAnalysisRepository aiAnalysisRepository) {
        this.aiAnalysisRepository = aiAnalysisRepository;
    }

    public AiTicketAnalysis analyzeTicket(Ticket ticket) {

        AiAnalysis existingAnalysis=aiAnalysisRepository
                .findByTicketId(ticket.getId())
                .orElse(null);

        if(existingAnalysis!=null){
            return new AiTicketAnalysis(
                    existingAnalysis.getCategory(),
                    existingAnalysis.getPriority(),
                    existingAnalysis.getSummary(),
                    existingAnalysis.getSuggestedSolution(),
                    existingAnalysis.getStatus()
            );
        }

        String title = ticket.getTitle().toLowerCase();
        String description = ticket.getDescription().toLowerCase();

        String category;
        String priority;
        String suggestedSolution;

        if (title.contains("vpn") || description.contains("vpn")) {

            category = "NETWORK";
            priority = "HIGH";
            suggestedSolution =
                    "Verify VPN credentials and network connectivity, then reconnect to the VPN.";

        } else if (title.contains("password") || description.contains("password")) {

            category = "ACCESS";
            priority = "MEDIUM";
            suggestedSolution =
                    "Verify the user's identity and initiate the password reset process.";

        } else if (title.contains("laptop") || description.contains("laptop")) {

            category = "HARDWARE";
            priority = "HIGH";
            suggestedSolution =
                    "Check the power connection and attempt a restart. If the issue continues, escalate to hardware support.";

        } else if (title.contains("outlook") || description.contains("outlook")) {

            category = "APP";
            priority = "MEDIUM";
            suggestedSolution =
                    "Check Outlook connectivity and updates, then restart Outlook.";

        } else {

            category = "OTHER";
            priority = "LOW";
            suggestedSolution =
                    "Review the issue and investigate the root cause.";
        }

        String summary =
                "Ticket: " + ticket.getTitle()
                        + " - " + ticket.getDescription();

        AiAnalysis analysis = new AiAnalysis();

        analysis.setCategory(category);
        analysis.setPriority(priority);
        analysis.setSummary(summary);
        analysis.setSuggestedSolution(suggestedSolution);
        analysis.setStatus(AiAnalysisStatus.PENDING);
        analysis.setAnalyzedAt(LocalDateTime.now());
        analysis.setTicket(ticket);

        AiAnalysis savedAnalysis = aiAnalysisRepository.save(analysis);

        return new AiTicketAnalysis(
                savedAnalysis.getCategory(),
                savedAnalysis.getPriority(),
                savedAnalysis.getSummary(),
                savedAnalysis.getSuggestedSolution(),
                savedAnalysis.getStatus()
        );
    }

    public String approveAnalysis(Integer ticketId) {

        AiAnalysis analysis = aiAnalysisRepository
                .findByTicketId(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "AI analysis not found for ticket: " + ticketId));

        if (analysis.getStatus() != AiAnalysisStatus.PENDING) {
            throw new IllegalStateException(
                    "AI analysis has already been processed");
        }

        analysis.setStatus(AiAnalysisStatus.APPROVED);

        Ticket ticket = analysis.getTicket();

        ticket.setPriority(
                TicketPriority.valueOf(analysis.getPriority())
        );

        aiAnalysisRepository.save(analysis);

        return "AI analysis approved and recommendation applied to ticket: "
                + ticketId;
    }

    public String rejectAnalysis(Integer ticketId) {

        AiAnalysis analysis = aiAnalysisRepository
                .findByTicketId(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "AI analysis not found for ticket: " + ticketId));

        if (analysis.getStatus() != AiAnalysisStatus.PENDING) {
            throw new IllegalStateException(
                    "AI analysis has already been processed");
        }

        analysis.setStatus(AiAnalysisStatus.REJECTED);

        aiAnalysisRepository.save(analysis);

        return "AI analysis rejected for ticket: " + ticketId;
    }



}