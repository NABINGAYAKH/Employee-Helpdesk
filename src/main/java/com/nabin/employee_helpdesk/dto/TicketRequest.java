package com.nabin.employee_helpdesk.dto;

import com.nabin.employee_helpdesk.entity.TicketPriority;
import com.nabin.employee_helpdesk.entity.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TicketRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private TicketStatus status;

    @NotNull
    private TicketPriority priority;

    @NotNull
    private Integer employeeId;

    @NotNull
    private Integer supportAgentId;
}
