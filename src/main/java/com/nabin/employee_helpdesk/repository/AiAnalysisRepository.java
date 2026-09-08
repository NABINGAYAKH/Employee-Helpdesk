package com.nabin.employee_helpdesk.repository;

import com.nabin.employee_helpdesk.entity.AiAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiAnalysisRepository extends JpaRepository<AiAnalysis, Integer> {

    Optional<AiAnalysis> findByTicketId(Integer ticketId);



}
