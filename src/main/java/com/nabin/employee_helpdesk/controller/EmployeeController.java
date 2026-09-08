package com.nabin.employee_helpdesk.controller;

import com.nabin.employee_helpdesk.dto.EmployeeRequest;
import com.nabin.employee_helpdesk.dto.EmployeeResponse;
import com.nabin.employee_helpdesk.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;


@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/api/employees")
    public List<EmployeeResponse> getAllEmployees(){
        return employeeService.findAll();
    }

    @GetMapping("/api/employees/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable int id){
        return employeeService.findById(id);
    }

    @PostMapping("/api/employees")
    public EmployeeResponse createEmployee(@RequestBody @Valid EmployeeRequest request){
        return employeeService.save(request);
    }

    @PutMapping("/api/employees/{id}")
    public EmployeeResponse updateEmployee(@PathVariable int id, @RequestBody @Valid EmployeeRequest request){
        return employeeService.updateEmployee(id,request);
    }

    @DeleteMapping("/api/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id){
        employeeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
