package com.bestdevelloperarchi.revurestapi.controllers;

// Integration Test for crud rest apis with springboottest annotation


import com.bestdevelloperarchi.revurestapi.dto.EmployeeRequest;
import com.bestdevelloperarchi.revurestapi.entities.Employee;
import com.bestdevelloperarchi.revurestapi.repositories.EmployeRepository;
import com.bestdevelloperarchi.revurestapi.services.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeRepository employeRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    void setup(){
        employeRepository.deleteAll();
    }

    @Test
    @DisplayName("POST An employee and save it ")
    @Order(1)
    void createEmployee() throws  Exception{
        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .name("AOO")
                .role("developer")
                .mobileNo("7894563210")
                .emailId("abdiooa45@gmail.com")
                .build();
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequest)));

        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name",is(employeeRequest.getName())))
                .andExpect(jsonPath("$.role",is(employeeRequest.getRole())))
                .andExpect(jsonPath("$.mobileNo",is(employeeRequest.getMobileNo())))
                .andExpect(jsonPath("$.emailId",is(employeeRequest.getEmailId())));
    }
    @Test
    @DisplayName(" POST a list employee and save them all and then return the list of the saved employee")
    @Order(3)
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
        // given - precondition or setup
        List<Employee> employees = new ArrayList<>();
        employees.add(Employee.builder().name("Tupac").role("rapper").mobileNo("7895632541").emailId("shakur@gmail.com").build());
        employees.add(Employee.builder().name("Salman").role("actor").mobileNo("7895632541").emailId("salman@gmail.com").build());
        employeRepository.saveAll(employees);

        // when

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees"));

        // then

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(employees.size())));
    }
    @Test
    @DisplayName(" Given an id for the employee, find the employee by this id and then return the employee ****")
    @Order(2)
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        // given
        List<Employee> employees = employeRepository.findAll();
        System.out.println(employees);
        long employeeId = 4L;
        Employee employee = Employee.builder()
                .name("AOO")
                .role("developer")
                .mobileNo("7894561230")
                .emailId("abdiooa45@gmail.com")
                .build();
        employeRepository.save(employee);

        System.out.println(employees);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/{id}",employeeId));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(employee.getName())))
                .andExpect(jsonPath("$.role",is(employee.getRole())))
                .andExpect(jsonPath("$.emailId",is(employee.getEmailId())));
    }
    @Test
    @DisplayName(" Given an none valid id for the employee, find the employee by this id and the error")
    @Order(4)
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .name("Ramesh")
                .role("Fadatare")
                .mobileNo("7894563210")
                .emailId("ramesh@gmail.com")
                .build();
        employeRepository.save(employee);

        // when -  action or the behaviour that we are going test

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/{id}",employeeId));

        // then - verify the output
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName(" Given an employee save it and update the values of the user and the verify if it is correspond each other")
    @Order(5)
    public void givenSaveEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception{
        // given  -  precondition or setup
        Employee employee = Employee.builder()
                .name("Ramesh")
                .role("Fadatare")
                .mobileNo("7894563210")
                .emailId("ramesh@gmail.com")
                .build();
        Employee savedemployee = employeRepository.save(employee);
        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .name("Abdi")
                .role("software engineer")
                .mobileNo("4747474741")
                .emailId("abdiooa451@gmail.com")
                .build();
        // when -  action or the behaviour that we are going test

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/employees/{id}",savedemployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequest)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(employeeRequest.getName())))
                .andExpect(jsonPath("$.role",is(employeeRequest.getRole())))
                .andExpect(jsonPath("$.emailId",is(employeeRequest.getEmailId())));

    }

    @Test
    @DisplayName(" Given an none valid employee  and try to update the values of the employee and the verify that the status if they  correspond each other")
    @Order(6)
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception{
        // given  -  precondition or setup
        long employeeId = 2L;
        Employee employee = Employee.builder()
                .name("Ramesh")
                .role("Fadatare")
                .mobileNo("7894563210")
                .emailId("ramesh@gmail.com")
                .build();
        Employee savedemployee = employeRepository.save(employee);
        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .name("Abdi")
                .role("software engineer")
                .mobileNo("4747474741")
                .emailId("abdiooa451@gmail.com")
                .build();
        // when -  action or the behaviour that we are going test

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequest)));
        // then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest());

    }


    @Test
    @Order(7)
    @DisplayName("Given an employee Id when try to delete the employee with the id if successfully delete return 204")
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        // given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .name("Chitiyaan")
                .role("Kiliyaan")
                .mobileNo("7894561230")
                .emailId("ramesh@gmail.com")
                .build();

        employeRepository.save(savedEmployee);

        // when - action or behaviour that we are going test

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employees/{id}",savedEmployee.getId()));

        // then - verify the output

        resultActions.andExpect(status().isNoContent())
                .andDo(print());

    }
}