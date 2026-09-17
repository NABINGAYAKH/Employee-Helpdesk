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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MockitoDemoTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    @Test
    void shouldFindEmployeeById(){

        //Arrange
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Nabin");
        employee.setEmail("nabin@gmail.com");
        employee.setDepartment("IT");

        when(employeeRepository.findById(1))
                .thenReturn(Optional.of(employee));

        //Act
        EmployeeResponse response = employeeService.findById(1);

        //Assert
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Nabin", response.getName());
        assertEquals("nabin@gmail.com", response.getEmail());
        assertEquals("IT", response.getDepartment());

        //Verify
        verify(employeeRepository).findById(1);
    }


    @Test
    void shouldThrowExceptionWhenEmployeeNotFound(){

        //Arrange
        when(employeeRepository.findById(999))
                .thenReturn(Optional.empty());

        //Act + Assert
        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.findById(999)
        );

        //Verify
        verify(employeeRepository).findById(999);

    }


    @Test
    void shouldSaveEmployee(){

        //Arrange
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

        //Act
        EmployeeResponse response = employeeService.save(request);

        //Assert
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Nabin", response.getName());
        assertEquals("nabin@gmail.com", response.getEmail());
        assertEquals("IT", response.getDepartment());

        //Verify
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void shouldDeleteEmployee(){
        //Arrange
        when(employeeRepository.existsById(1))
                .thenReturn(true);

        //Act
        employeeService.deleteById(1);

        //Verify
        verify(employeeRepository).existsById(1);
        verify(employeeRepository).deleteById(1);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingEmployee(){
        //Arrange
        when(employeeRepository.existsById(999))
                .thenReturn(false);

        //Act+Assert
        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.deleteById(999)
        );

        //Verify
        verify(employeeRepository).existsById(999);
        verify(employeeRepository, never()).deleteById(999);
    }

    @Test
    void shouldUpdateEmployee(){
        //Arrange
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

        //Act

        EmployeeResponse response = employeeService.updateEmployee(1, request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Nabin", response.getName());
        assertEquals("nabin@gmail.com", response.getEmail());
        assertEquals("IT", response.getDepartment());

        // Verify
        verify(employeeRepository).findById(1);
        verify(employeeRepository).save(existingEmployee);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingEmployee() {

        // Arrange
        EmployeeRequest request = new EmployeeRequest();
        request.setName("Nabin");
        request.setEmail("nabin@gmail.com");
        request.setDepartment("IT");

        when(employeeRepository.findById(999))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.updateEmployee(999, request)
        );

        // Verify
        verify(employeeRepository).findById(999);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void shouldFindAllEmployees() {

        // Arrange
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

        when(employeeRepository.findAll())
                .thenReturn(List.of(employee1, employee2));

        // Act
        List<EmployeeResponse> responses = employeeService.findAll();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());

        assertEquals(1, responses.get(0).getId());
        assertEquals("Nabin", responses.get(0).getName());

        assertEquals(2, responses.get(1).getId());
        assertEquals("John", responses.get(1).getName());

        // Verify
        verify(employeeRepository).findAll();
    }
}
