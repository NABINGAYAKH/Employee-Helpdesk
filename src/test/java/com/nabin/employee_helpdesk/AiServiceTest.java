package com.nabin.employee_helpdesk;

import com.nabin.employee_helpdesk.dto.AiTicketAnalysis;
import com.nabin.employee_helpdesk.entity.AiAnalysis;
import com.nabin.employee_helpdesk.entity.AiAnalysisStatus;
import com.nabin.employee_helpdesk.entity.Ticket;
import com.nabin.employee_helpdesk.entity.TicketPriority;
import com.nabin.employee_helpdesk.exception.ResourceNotFoundException;
import com.nabin.employee_helpdesk.repository.AiAnalysisRepository;
import com.nabin.employee_helpdesk.service.AiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AiServiceTest {

    @Mock
    AiAnalysisRepository aiAnalysisRepository;

    @InjectMocks
    AiService aiService;

    @Test
    void shouldAnalyzeVpnTicket() {

        // Arrange

        Ticket ticket = new Ticket();

        ticket.setId(1);
        ticket.setTitle("VPN Issue");
        ticket.setDescription("Unable to connect to VPN");


        when(aiAnalysisRepository.findByTicketId(1))
                .thenReturn(Optional.empty());


        AiAnalysis savedAnalysis = new AiAnalysis();

        savedAnalysis.setCategory("NETWORK");
        savedAnalysis.setPriority("HIGH");
        savedAnalysis.setSummary(
                "Ticket: VPN Issue - Unable to connect to VPN"
        );
        savedAnalysis.setSuggestedSolution(
                "Verify VPN credentials and network connectivity, then reconnect to the VPN."
        );
        savedAnalysis.setStatus(AiAnalysisStatus.PENDING);


        when(aiAnalysisRepository.save(any(AiAnalysis.class)))
                .thenReturn(savedAnalysis);


        // Act

        AiTicketAnalysis result =
                aiService.analyzeTicket(ticket);


        // Assert

        assertNotNull(result);

        assertEquals("NETWORK", result.getCategory());
        assertEquals("HIGH", result.getPriority());

        assertEquals(
                "Ticket: VPN Issue - Unable to connect to VPN",
                result.getSummary()
        );

        assertEquals(
                "Verify VPN credentials and network connectivity, then reconnect to the VPN.",
                result.getSuggestedSolution()
        );

        assertEquals(
                AiAnalysisStatus.PENDING,
                result.getStatus()
        );


        // Verify

        verify(aiAnalysisRepository)
                .findByTicketId(1);

        verify(aiAnalysisRepository)
                .save(any(AiAnalysis.class));

    }


    @Test
    void shouldReturnExistingAnalysis() {

        // Arrange

        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setTitle("VPN Issue");
        ticket.setDescription("Unable to connect to VPN");

        AiAnalysis existingAnalysis = new AiAnalysis();

        existingAnalysis.setCategory("NETWORK");
        existingAnalysis.setPriority("HIGH");
        existingAnalysis.setSummary(
                "Existing VPN analysis"
        );
        existingAnalysis.setSuggestedSolution(
                "Reconnect to the VPN."
        );
        existingAnalysis.setStatus(AiAnalysisStatus.APPROVED);

        when(aiAnalysisRepository.findByTicketId(1))
                .thenReturn(Optional.of(existingAnalysis));


        // Act

        AiTicketAnalysis result =
                aiService.analyzeTicket(ticket);


        // Assert

        assertNotNull(result);

        assertEquals("NETWORK", result.getCategory());
        assertEquals("HIGH", result.getPriority());
        assertEquals("Existing VPN analysis", result.getSummary());
        assertEquals(
                "Reconnect to the VPN.",
                result.getSuggestedSolution()
        );
        assertEquals(
                AiAnalysisStatus.APPROVED,
                result.getStatus()
        );


        // Verify

        verify(aiAnalysisRepository)
                .findByTicketId(1);

        verify(aiAnalysisRepository, never())
                .save(any(AiAnalysis.class));
    }


    @Test
    void shouldAnalyzePasswordTicket() {

        // Arrange

        Ticket ticket = new Ticket();

        ticket.setId(2);
        ticket.setTitle("Account Problem");
        ticket.setDescription("I forgot my password");


        when(aiAnalysisRepository.findByTicketId(2))
                .thenReturn(Optional.empty());


        AiAnalysis savedAnalysis = new AiAnalysis();

        savedAnalysis.setCategory("ACCESS");
        savedAnalysis.setPriority("MEDIUM");
        savedAnalysis.setSummary(
                "Ticket: Account Problem - I forgot my password"
        );
        savedAnalysis.setSuggestedSolution(
                "Verify the user's identity and initiate the password reset process."
        );
        savedAnalysis.setStatus(AiAnalysisStatus.PENDING);


        when(aiAnalysisRepository.save(any(AiAnalysis.class)))
                .thenReturn(savedAnalysis);


        // Act

        AiTicketAnalysis result =
                aiService.analyzeTicket(ticket);


        // Assert

        assertNotNull(result);

        assertEquals("ACCESS", result.getCategory());
        assertEquals("MEDIUM", result.getPriority());

        assertEquals(
                "Ticket: Account Problem - I forgot my password",
                result.getSummary()
        );

        assertEquals(
                "Verify the user's identity and initiate the password reset process.",
                result.getSuggestedSolution()
        );

        assertEquals(
                AiAnalysisStatus.PENDING,
                result.getStatus()
        );


        // Verify

        verify(aiAnalysisRepository)
                .findByTicketId(2);

        verify(aiAnalysisRepository)
                .save(any(AiAnalysis.class));
    }

    @Test
    void shouldAnalyzeLaptopTicket() {

        // Arrange

        Ticket ticket = new Ticket();

        ticket.setId(3);
        ticket.setTitle("Laptop Issue");
        ticket.setDescription("Laptop is not starting");

        when(aiAnalysisRepository.findByTicketId(3))
                .thenReturn(Optional.empty());

        AiAnalysis savedAnalysis = new AiAnalysis();

        savedAnalysis.setCategory("HARDWARE");
        savedAnalysis.setPriority("HIGH");
        savedAnalysis.setSummary(
                "Ticket: Laptop Issue - Laptop is not starting"
        );
        savedAnalysis.setSuggestedSolution(
                "Check the power connection and attempt a restart. If the issue continues, escalate to hardware support."
        );
        savedAnalysis.setStatus(AiAnalysisStatus.PENDING);

        when(aiAnalysisRepository.save(any(AiAnalysis.class)))
                .thenReturn(savedAnalysis);

        // Act

        AiTicketAnalysis result =
                aiService.analyzeTicket(ticket);

        // Assert

        assertNotNull(result);

        assertEquals("HARDWARE", result.getCategory());
        assertEquals("HIGH", result.getPriority());

        assertEquals(
                "Ticket: Laptop Issue - Laptop is not starting",
                result.getSummary()
        );

        assertEquals(
                "Check the power connection and attempt a restart. If the issue continues, escalate to hardware support.",
                result.getSuggestedSolution()
        );

        assertEquals(
                AiAnalysisStatus.PENDING,
                result.getStatus()
        );

        // Verify

        verify(aiAnalysisRepository)
                .findByTicketId(3);

        verify(aiAnalysisRepository)
                .save(any(AiAnalysis.class));
    }

    @Test
    void shouldAnalyzeOutlookTicket() {

        // Arrange

        Ticket ticket = new Ticket();

        ticket.setId(4);
        ticket.setTitle("Outlook Issue");
        ticket.setDescription("Outlook is not opening");

        when(aiAnalysisRepository.findByTicketId(4))
                .thenReturn(Optional.empty());

        AiAnalysis savedAnalysis = new AiAnalysis();

        savedAnalysis.setCategory("APP");
        savedAnalysis.setPriority("MEDIUM");
        savedAnalysis.setSummary(
                "Ticket: Outlook Issue - Outlook is not opening"
        );
        savedAnalysis.setSuggestedSolution(
                "Check Outlook connectivity and updates, then restart Outlook."
        );
        savedAnalysis.setStatus(AiAnalysisStatus.PENDING);

        when(aiAnalysisRepository.save(any(AiAnalysis.class)))
                .thenReturn(savedAnalysis);

        // Act

        AiTicketAnalysis result =
                aiService.analyzeTicket(ticket);

        // Assert

        assertNotNull(result);

        assertEquals("APP", result.getCategory());
        assertEquals("MEDIUM", result.getPriority());

        assertEquals(
                "Ticket: Outlook Issue - Outlook is not opening",
                result.getSummary()
        );

        assertEquals(
                "Check Outlook connectivity and updates, then restart Outlook.",
                result.getSuggestedSolution()
        );

        assertEquals(
                AiAnalysisStatus.PENDING,
                result.getStatus()
        );

        // Verify

        verify(aiAnalysisRepository)
                .findByTicketId(4);

        verify(aiAnalysisRepository)
                .save(any(AiAnalysis.class));
    }

    @Test
    void shouldAnalyzeUnknownTicket() {

        // Arrange

        Ticket ticket = new Ticket();

        ticket.setId(5);
        ticket.setTitle("Printer Problem");
        ticket.setDescription("Printer is showing an unknown error");

        when(aiAnalysisRepository.findByTicketId(5))
                .thenReturn(Optional.empty());

        AiAnalysis savedAnalysis = new AiAnalysis();

        savedAnalysis.setCategory("OTHER");
        savedAnalysis.setPriority("LOW");
        savedAnalysis.setSummary(
                "Ticket: Printer Problem - Printer is showing an unknown error"
        );
        savedAnalysis.setSuggestedSolution(
                "Review the issue and investigate the root cause."
        );
        savedAnalysis.setStatus(AiAnalysisStatus.PENDING);

        when(aiAnalysisRepository.save(any(AiAnalysis.class)))
                .thenReturn(savedAnalysis);

        // Act

        AiTicketAnalysis result =
                aiService.analyzeTicket(ticket);

        // Assert

        assertNotNull(result);

        assertEquals("OTHER", result.getCategory());
        assertEquals("LOW", result.getPriority());

        assertEquals(
                "Ticket: Printer Problem - Printer is showing an unknown error",
                result.getSummary()
        );

        assertEquals(
                "Review the issue and investigate the root cause.",
                result.getSuggestedSolution()
        );

        assertEquals(
                AiAnalysisStatus.PENDING,
                result.getStatus()
        );

        // Verify

        verify(aiAnalysisRepository)
                .findByTicketId(5);

        verify(aiAnalysisRepository)
                .save(any(AiAnalysis.class));
    }
    @Test
    void shouldApproveAnalysis() {

        // Arrange

        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setPriority(TicketPriority.LOW);

        AiAnalysis analysis = new AiAnalysis();
        analysis.setCategory("NETWORK");
        analysis.setPriority("HIGH");
        analysis.setStatus(AiAnalysisStatus.PENDING);
        analysis.setTicket(ticket);

        when(aiAnalysisRepository.findByTicketId(1))
                .thenReturn(Optional.of(analysis));

        when(aiAnalysisRepository.save(any(AiAnalysis.class)))
                .thenReturn(analysis);

        // Act

        String result =
                aiService.approveAnalysis(1);

        // Assert

        assertEquals(
                AiAnalysisStatus.APPROVED,
                analysis.getStatus()
        );

        assertEquals(
                TicketPriority.HIGH,
                ticket.getPriority()
        );

        assertEquals(
                "AI analysis approved and recommendation applied to ticket: 1",
                result
        );

        // Verify

        verify(aiAnalysisRepository)
                .findByTicketId(1);

        verify(aiAnalysisRepository)
                .save(analysis);
    }

    @Test
    void shouldRejectAnalysis() {

        // Arrange

        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setPriority(TicketPriority.LOW);

        AiAnalysis analysis = new AiAnalysis();
        analysis.setCategory("NETWORK");
        analysis.setPriority("HIGH");
        analysis.setStatus(AiAnalysisStatus.PENDING);
        analysis.setTicket(ticket);

        when(aiAnalysisRepository.findByTicketId(1))
                .thenReturn(Optional.of(analysis));

        when(aiAnalysisRepository.save(any(AiAnalysis.class)))
                .thenReturn(analysis);

        // Act

        String result =
                aiService.rejectAnalysis(1);

        // Assert

        assertEquals(
                AiAnalysisStatus.REJECTED,
                analysis.getStatus()
        );

        // Priority should remain unchanged
        assertEquals(
                TicketPriority.LOW,
                ticket.getPriority()
        );

        assertEquals(
                "AI analysis rejected for ticket: 1",
                result
        );

        // Verify

        verify(aiAnalysisRepository)
                .findByTicketId(1);

        verify(aiAnalysisRepository)
                .save(analysis);
    }

    @Test
    void shouldThrowExceptionWhenAnalysisNotFoundDuringApproval() {

        // Arrange

        when(aiAnalysisRepository.findByTicketId(99))
                .thenReturn(Optional.empty());

        // Act + Assert

        assertThrows(
                ResourceNotFoundException.class,
                () -> aiService.approveAnalysis(99)
        );

        // Verify

        verify(aiAnalysisRepository)
                .findByTicketId(99);

        verify(aiAnalysisRepository, never())
                .save(any(AiAnalysis.class));
    }

    @Test
    void shouldThrowExceptionWhenAnalysisNotFoundDuringRejection() {

        // Arrange

        when(aiAnalysisRepository.findByTicketId(99))
                .thenReturn(Optional.empty());

        // Act + Assert

        assertThrows(
                ResourceNotFoundException.class,
                () -> aiService.rejectAnalysis(99)
        );

        // Verify

        verify(aiAnalysisRepository)
                .findByTicketId(99);

        verify(aiAnalysisRepository, never())
                .save(any(AiAnalysis.class));
    }
}
