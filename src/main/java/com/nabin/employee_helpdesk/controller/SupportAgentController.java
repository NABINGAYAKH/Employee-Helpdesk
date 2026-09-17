package com.nabin.employee_helpdesk.controller;

import com.nabin.employee_helpdesk.dto.SupportAgentRequest;
import com.nabin.employee_helpdesk.dto.SupportAgentResponse;
import com.nabin.employee_helpdesk.service.SupportAgentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Support Agents",
        description = "Support agent management APIs"
)
@RestController
public class SupportAgentController {

    @Autowired
    private SupportAgentService supportAgentService;


    @Operation(
            summary = "Create a support agent",
            description = "Creates a new support agent using the provided details"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Support agent created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid support agent data"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    @PostMapping("/api/support-agents")
    public SupportAgentResponse createSupportAgent(
            @RequestBody @Valid SupportAgentRequest request) {

        return supportAgentService.save(request);
    }


    @Operation(
            summary = "Get all support agents",
            description = "Returns a list of all support agents"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Support agents retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    @GetMapping("/api/support-agents")
    public List<SupportAgentResponse> getSupportAgents() {

        return supportAgentService.findAll();
    }


    @Operation(
            summary = "Get support agent by ID",
            description = "Returns a support agent using the support agent ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Support agent found"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Support agent not found")
    })
    @GetMapping("/api/support-agents/{id}")
    public SupportAgentResponse getById(@PathVariable int id) {

        return supportAgentService.findById(id);
    }


    @Operation(
            summary = "Update a support agent",
            description = "Updates an existing support agent using the support agent ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Support agent updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid support agent data"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Support agent not found")
    })
    @PutMapping("/api/support-agents/{id}")
    public SupportAgentResponse updateSupportAgent(
            @PathVariable int id,
            @RequestBody @Valid SupportAgentRequest request) {

        return supportAgentService.update(id, request);
    }


    @Operation(
            summary = "Delete a support agent",
            description = "Deletes a support agent using the support agent ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Support agent deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Support agent not found")
    })
    @DeleteMapping("/api/support-agents/{id}")
    public ResponseEntity<Void> deleteSupportAgent(@PathVariable int id) {

        supportAgentService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}