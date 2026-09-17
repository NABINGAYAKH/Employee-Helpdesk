package com.nabin.employee_helpdesk.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Employees",
        description = "Employee management APIs"
)
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Operation(
            summary = "Get all employees",
            description = "Returns a list of all employees"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employees retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            )
    })
    @GetMapping("/api/employees")
    public List<EmployeeResponse> getAllEmployees(){
        return employeeService.findAll();
    }




    @Operation(
            summary = "Get employee by ID",
            description = "Returns an employee using the employee ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee found"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )
    })
    @GetMapping("/api/employees/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable int id){
        return employeeService.findById(id);
    }




    @Operation(
            summary = "Create a new employee",
            description = "Creates a new employee using the provided employee details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid employee data"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            )
    })
    @PostMapping("/api/employees")
    public EmployeeResponse createEmployee(@RequestBody @Valid EmployeeRequest request){
        return employeeService.save(request);
    }




    @Operation(
            summary = "Update an employee",
            description = "Updates an existing employee using the employee ID and provided employee details"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid employee data"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PutMapping("/api/employees/{id}")
    public EmployeeResponse updateEmployee(@PathVariable int id, @RequestBody @Valid EmployeeRequest request){
        return employeeService.updateEmployee(id,request);
    }


    @Operation(
            summary = "Delete an employee",
            description = "Deletes an employee using the employee ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied. ADMIN role required"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/api/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id){
        employeeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
