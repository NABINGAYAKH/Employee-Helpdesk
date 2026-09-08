package com.nabin.employee_helpdesk.dto;

import com.nabin.employee_helpdesk.entity.AiAnalysisStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiTicketAnalysis {
    private String category;
    private String priority;
    private String summary;
    private String suggestedSolution;
    private AiAnalysisStatus status;
}
