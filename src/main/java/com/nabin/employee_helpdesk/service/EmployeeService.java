package com.nabin.employee_helpdesk.service;

import com.nabin.employee_helpdesk.dto.EmployeeRequest;
import com.nabin.employee_helpdesk.dto.EmployeeResponse;
import com.nabin.employee_helpdesk.entity.Employee;
import com.nabin.employee_helpdesk.exception.ResourceNotFoundException;
import com.nabin.employee_helpdesk.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<EmployeeResponse> findAll() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeResponse> responses = new ArrayList<>();

        for(Employee employee : employees){
            EmployeeResponse response = new EmployeeResponse();
            response.setId(employee.getId());
            response.setName(employee.getName());
            response.setEmail(employee.getEmail());
            response.setDepartment(employee.getDepartment());

            responses.add(response);
        }
        return responses;
    }
    public EmployeeResponse findById(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);

        EmployeeResponse response = new EmployeeResponse();
        if(employee.isPresent()){

            response.setId(employee.get().getId());
            response.setName(employee.get().getName());
            response.setEmail(employee.get().getEmail());
            response.setDepartment(employee.get().getDepartment());
            return response;
        }
        throw new ResourceNotFoundException("Employee not found with id: " + id);
    }
    public EmployeeResponse save(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        Employee savedEmployee = employeeRepository.save(employee);

        EmployeeResponse response = new EmployeeResponse();

        response.setId(savedEmployee.getId());
        response.setName(savedEmployee.getName());
        response.setEmail(savedEmployee.getEmail());
        response.setDepartment(savedEmployee.getDepartment());

        return response;
    }
    public void deleteById(int id) {
        if(employeeRepository.existsById(id)){
            employeeRepository.deleteById(id);
        }else{
            throw new ResourceNotFoundException("Employee not found with id: "+id);
        }

    }

    public EmployeeResponse updateEmployee(int id, EmployeeRequest request) {
        Employee existingEmployee = employeeRepository.findById(id).orElse(null);
        if(existingEmployee!=null){
            existingEmployee.setName(request.getName());
            existingEmployee.setEmail(request.getEmail());
            existingEmployee.setDepartment(request.getDepartment());
            Employee savedEmployee = employeeRepository.save(existingEmployee);

            EmployeeResponse response = new EmployeeResponse();
            response.setId(savedEmployee.getId());
            response.setName(savedEmployee.getName());
            response.setEmail(savedEmployee.getEmail());
            response.setDepartment(savedEmployee.getDepartment());
            return response;
        }
        throw new ResourceNotFoundException("Employee not found with id: "+id);
    }
}
