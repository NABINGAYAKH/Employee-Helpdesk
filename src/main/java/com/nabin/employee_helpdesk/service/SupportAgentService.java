package com.nabin.employee_helpdesk.service;

import com.nabin.employee_helpdesk.dto.SupportAgentRequest;
import com.nabin.employee_helpdesk.dto.SupportAgentResponse;
import com.nabin.employee_helpdesk.entity.SupportAgent;
import com.nabin.employee_helpdesk.exception.ResourceNotFoundException;
import com.nabin.employee_helpdesk.repository.SupportAgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SupportAgentService {

    @Autowired
    private SupportAgentRepository supportAgentRepository;

    public SupportAgentResponse save(SupportAgentRequest request){

        SupportAgent supportAgent = new SupportAgent();
        supportAgent.setName(request.getName());
        supportAgent.setEmail(request.getEmail());
        supportAgent.setDepartment(request.getDepartment());

        SupportAgent savedSupportAgent = supportAgentRepository.save(supportAgent);

        SupportAgentResponse response = new SupportAgentResponse();
        response.setId(savedSupportAgent.getId());
        response.setName(savedSupportAgent.getName());
        response.setEmail(savedSupportAgent.getEmail());
        response.setDepartment(savedSupportAgent.getDepartment());

        return response;
    }

    public List<SupportAgentResponse> findAll() {
        List<SupportAgent> supportAgents =supportAgentRepository.findAll();
        List<SupportAgentResponse> responses = new ArrayList<>();

        for(SupportAgent supportAgent: supportAgents){
            SupportAgentResponse response = new SupportAgentResponse();
            response.setId(supportAgent.getId());
            response.setName(supportAgent.getName());
            response.setEmail(supportAgent.getEmail());
            response.setDepartment(supportAgent.getDepartment());
            responses.add(response);
        }
        return responses;
    }

    public SupportAgentResponse findById(int id) {

        Optional<SupportAgent> supportAgent =
                supportAgentRepository.findById(id);

        if (supportAgent.isPresent()) {

            SupportAgentResponse response = new SupportAgentResponse();

            response.setId(supportAgent.get().getId());
            response.setName(supportAgent.get().getName());
            response.setEmail(supportAgent.get().getEmail());
            response.setDepartment(supportAgent.get().getDepartment());

            return response;
        }

        throw new ResourceNotFoundException(
                "Support agent not found with id: " + id
        );
    }

    public SupportAgentResponse update(int id, SupportAgentRequest request) {

        SupportAgent existingAgent =
                supportAgentRepository.findById(id).orElse(null);

        if (existingAgent == null) {
            throw new ResourceNotFoundException(
                    "Support agent not found with id: " + id
            );
        }

        existingAgent.setName(request.getName());
        existingAgent.setEmail(request.getEmail());
        existingAgent.setDepartment(request.getDepartment());

        SupportAgent savedAgent =
                supportAgentRepository.save(existingAgent);

        SupportAgentResponse response = new SupportAgentResponse();

        response.setId(savedAgent.getId());
        response.setName(savedAgent.getName());
        response.setEmail(savedAgent.getEmail());
        response.setDepartment(savedAgent.getDepartment());

        return response;
    }

    public void deleteById(int id) {

        if (supportAgentRepository.existsById(id)) {
            supportAgentRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(
                    "Support agent not found with id: " + id
            );
        }
    }
}
