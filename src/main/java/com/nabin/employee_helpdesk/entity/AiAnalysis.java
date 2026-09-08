package com.nabin.employee_helpdesk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AiAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String category;
    private String priority;
    private String summary;
    private String suggestedSolution;

    @Enumerated(EnumType.STRING)
    private AiAnalysisStatus status;

    private LocalDateTime analyzedAt;

    @OneToOne
    private Ticket ticket;

}
