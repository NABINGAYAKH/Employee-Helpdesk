package com.nabin.employee_helpdesk.controller;

import com.nabin.employee_helpdesk.dto.SupportAgentRequest;
import com.nabin.employee_helpdesk.dto.SupportAgentResponse;
import com.nabin.employee_helpdesk.service.SupportAgentService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SupportAgentController {

    @Autowired
    private SupportAgentService supportAgentService;

    @PostMapping("/api/support-agents")
    public SupportAgentResponse createSupportAgent(@RequestBody @Valid SupportAgentRequest request){
        return supportAgentService.save(request);
    }

    @GetMapping("/api/support-agents")
    public List<SupportAgentResponse> getSupportAgents(){
        return supportAgentService.findAll();
    }

    @GetMapping("/api/support-agents/{id}")
    public SupportAgentResponse getById(@PathVariable int id) {
        return supportAgentService.findById(id);
    }

    @PutMapping("/api/support-agents/{id}")
    public SupportAgentResponse updateSupportAgent(
            @PathVariable int id,
            @RequestBody @Valid SupportAgentRequest request) {

        return supportAgentService.update(id, request);
    }

    @DeleteMapping("/api/support-agents/{id}")
    public ResponseEntity<Void> deleteSupportAgent(@PathVariable int id) {
        supportAgentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
