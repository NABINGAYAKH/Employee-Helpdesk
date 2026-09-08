package com.nabin.employee_helpdesk.repository;

import com.nabin.employee_helpdesk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
}
