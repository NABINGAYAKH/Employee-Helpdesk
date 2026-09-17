package com.nabin.employee_helpdesk;

import com.nabin.employee_helpdesk.controller.EmployeeController;
import com.nabin.employee_helpdesk.dto.EmployeeRequest;
import com.nabin.employee_helpdesk.dto.EmployeeResponse;
import com.nabin.employee_helpdesk.exception.ResourceNotFoundException;
import com.nabin.employee_helpdesk.service.EmployeeService;
import com.nabin.employee_helpdesk.service.JwtService;
import com.nabin.employee_helpdesk.service.CustomUserDetailsService;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    EmployeeService employeeService;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    CustomUserDetailsService customUserDetailsService;


    @Test
    void shouldGetAllEmployees() throws Exception {

        List<EmployeeResponse> employees = List.of(
                new EmployeeResponse(
                        1,
                        "Nabin",
                        "nabin@gmail.com",
                        "IT"
                ),
                new EmployeeResponse(
                        2,
                        "Rahul",
                        "rahul@gmail.com",
                        "HR"
                )
        );

        when(employeeService.findAll())
                .thenReturn(employees);

        mockMvc.perform(
                        get("/api/employees")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Nabin"))
                .andExpect(jsonPath("$[0].email").value("nabin@gmail.com"))
                .andExpect(jsonPath("$[0].department").value("IT"))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void shouldGetEmployeeById() throws Exception {

        EmployeeResponse employee =
                new EmployeeResponse(
                        1,
                        "Nabin",
                        "nabin@gmail.com",
                        "IT"
                );

        when(employeeService.findById(1))
                .thenReturn(employee);

        mockMvc.perform(
                        get("/api/employees/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nabin"))
                .andExpect(jsonPath("$.email").value("nabin@gmail.com"))
                .andExpect(jsonPath("$.department").value("IT"));
    }

    @Test
    void shouldReturnNotFoundWhenEmployeeDoesNotExist() throws Exception {

        when(employeeService.findById(99))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Employee not found"
                        )
                );

        mockMvc.perform(
                        get("/api/employees/99")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateEmployee() throws Exception {

        EmployeeResponse response =
                new EmployeeResponse(
                        1,
                        "Nabin",
                        "nabin@gmail.com",
                        "IT"
                );

        when(employeeService.save(any(EmployeeRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Nabin",
                                            "email": "nabin@gmail.com",
                                            "department": "IT"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nabin"))
                .andExpect(jsonPath("$.email").value("nabin@gmail.com"))
                .andExpect(jsonPath("$.department").value("IT"));
    }

    @Test
    void shouldRejectInvalidEmployee() throws Exception {

        mockMvc.perform(
                        post("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "name": "",
                                        "email": "invalid-email",
                                        "department": ""
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldUpdateEmployee() throws Exception {

        EmployeeResponse response =
                new EmployeeResponse(
                        1,
                        "Nabin Updated",
                        "nabin.updated@gmail.com",
                        "Development"
                );

        when(employeeService.updateEmployee(
                eq(1),
                any(EmployeeRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/api/employees/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "name": "Nabin Updated",
                                        "email": "nabin.updated@gmail.com",
                                        "department": "Development"
                                    }
                                    """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nabin Updated"))
                .andExpect(jsonPath("$.email").value("nabin.updated@gmail.com"))
                .andExpect(jsonPath("$.department").value("Development"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingEmployee() throws Exception {

        when(employeeService.updateEmployee(
                eq(99),
                any(EmployeeRequest.class)
        )).thenThrow(
                new ResourceNotFoundException(
                        "Employee not found with id: 99"
                )
        );

        mockMvc.perform(
                        put("/api/employees/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "name": "Nabin",
                                        "email": "nabin@gmail.com",
                                        "department": "IT"
                                    }
                                    """)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteEmployee() throws Exception {

        mockMvc.perform(
                        delete("/api/employees/1")
                )
                .andExpect(status().isNoContent());

        verify(employeeService).deleteById(1);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingEmployee() throws Exception {

        doThrow(
                new ResourceNotFoundException(
                        "Employee not found with id: 99"
                )
        ).when(employeeService).deleteById(99);

        mockMvc.perform(
                        delete("/api/employees/99")
                )
                .andExpect(status().isNotFound());
    }

}