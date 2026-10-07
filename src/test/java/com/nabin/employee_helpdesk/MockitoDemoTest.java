package com.nabin.employee_helpdesk;

import com.nabin.employee_helpdesk.dto.EmployeeRequest;
import com.nabin.employee_helpdesk.dto.EmployeeResponse;
import com.nabin.employee_helpdesk.entity.Employee;
import com.nabin.employee_helpdesk.exception.ResourceNotFoundException;
import com.nabin.employee_helpdesk.repository.EmployeeRepository;
import com.nabin.employee_helpdesk.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MockitoDemoTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;


    @Test
    void shouldFindEmployeeById() {

        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Nabin");
        employee.setEmail("nabin@gmail.com");
        employee.setDepartment("IT");

        when(employeeRepository.findById(1))
                .thenReturn(Optional.of(employee));

        EmployeeResponse response =
                employeeService.findById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Nabin", response.getName());
        assertEquals("nabin@gmail.com", response.getEmail());
        assertEquals("IT", response.getDepartment());

        verify(employeeRepository).findById(1);
    }


    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {

        when(employeeRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.findById(999)
        );

        verify(employeeRepository).findById(999);
    }


    @Test
    void shouldSaveEmployee() {

        EmployeeRequest request = new EmployeeRequest();
        request.setName("Nabin");
        request.setEmail("nabin@gmail.com");
        request.setDepartment("IT");

        Employee savedEmployee = new Employee();
        savedEmployee.setId(1);
        savedEmployee.setName("Nabin");
        savedEmployee.setEmail("nabin@gmail.com");
        savedEmployee.setDepartment("IT");

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(savedEmployee);

        EmployeeResponse response =
                employeeService.save(request);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Nabin", response.getName());
        assertEquals("nabin@gmail.com", response.getEmail());
        assertEquals("IT", response.getDepartment());

        verify(employeeRepository)
                .save(any(Employee.class));
    }


    @Test
    void shouldDeleteEmployee() {

        when(employeeRepository.existsById(1))
                .thenReturn(true);

        employeeService.deleteById(1);

        verify(employeeRepository).existsById(1);
        verify(employeeRepository).deleteById(1);
    }


    @Test
    void shouldThrowExceptionWhenDeletingNonExistingEmployee() {

        when(employeeRepository.existsById(999))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.deleteById(999)
        );

        verify(employeeRepository).existsById(999);

        verify(employeeRepository, never())
                .deleteById(999);
    }


    @Test
    void shouldUpdateEmployee() {

        Employee existingEmployee = new Employee();
        existingEmployee.setId(1);
        existingEmployee.setName("Old Name");
        existingEmployee.setEmail("old@gmail.com");
        existingEmployee.setDepartment("HR");

        EmployeeRequest request = new EmployeeRequest();
        request.setName("Nabin");
        request.setEmail("nabin@gmail.com");
        request.setDepartment("IT");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1);
        updatedEmployee.setName("Nabin");
        updatedEmployee.setEmail("nabin@gmail.com");
        updatedEmployee.setDepartment("IT");

        when(employeeRepository.findById(1))
                .thenReturn(Optional.of(existingEmployee));

        when(employeeRepository.save(existingEmployee))
                .thenReturn(updatedEmployee);

        EmployeeResponse response =
                employeeService.updateEmployee(1, request);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Nabin", response.getName());
        assertEquals("nabin@gmail.com", response.getEmail());
        assertEquals("IT", response.getDepartment());

        verify(employeeRepository).findById(1);
        verify(employeeRepository).save(existingEmployee);
    }


    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingEmployee() {

        EmployeeRequest request = new EmployeeRequest();
        request.setName("Nabin");
        request.setEmail("nabin@gmail.com");
        request.setDepartment("IT");

        when(employeeRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.updateEmployee(999, request)
        );

        verify(employeeRepository).findById(999);

        verify(employeeRepository, never())
                .save(any(Employee.class));
    }


    @Test
    void shouldFindAllEmployees() {

        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setName("Nabin");
        employee1.setEmail("nabin@gmail.com");
        employee1.setDepartment("IT");

        Employee employee2 = new Employee();
        employee2.setId(2);
        employee2.setName("John");
        employee2.setEmail("john@gmail.com");
        employee2.setDepartment("HR");

        Page<Employee> employeePage =
                new PageImpl<>(
                        List.of(employee1, employee2),
                        PageRequest.of(0, 10),
                        2
                );

        when(employeeRepository.findAll(any(Pageable.class)))
                .thenReturn(employeePage);

        Page<EmployeeResponse> responses =
                employeeService.findAll(PageRequest.of(0, 10));

        assertNotNull(responses);
        assertEquals(2, responses.getContent().size());

        assertEquals(1, responses.getContent().get(0).getId());
        assertEquals(
                "Nabin",
                responses.getContent().get(0).getName()
        );

        assertEquals(2, responses.getContent().get(1).getId());
        assertEquals(
                "John",
                responses.getContent().get(1).getName()
        );

        verify(employeeRepository)
                .findAll(any(Pageable.class));
    }
}