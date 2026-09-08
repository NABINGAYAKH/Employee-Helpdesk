package com.nabin.employee_helpdesk.repository;

import com.nabin.employee_helpdesk.entity.SupportAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportAgentRepository extends JpaRepository<SupportAgent, Integer>{

}
