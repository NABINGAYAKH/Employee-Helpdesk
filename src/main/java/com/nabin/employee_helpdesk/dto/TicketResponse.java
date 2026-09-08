package com.nabin.employee_helpdesk.dto;

import com.nabin.employee_helpdesk.entity.TicketPriority;
import com.nabin.employee_helpdesk.entity.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TicketResponse {

    private Integer id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private Integer employeeId;
    private Integer supportAgentId;
}
