package com.bestdevelloperarchi.revurestapi.services;

import com.bestdevelloperarchi.revurestapi.dto.EmployeeRequest;
import com.bestdevelloperarchi.revurestapi.dto.EmployeeResponse;
import com.bestdevelloperarchi.revurestapi.entities.Employee;
import com.bestdevelloperarchi.revurestapi.exceptions.EmailAlreadyExistsException;
import com.bestdevelloperarchi.revurestapi.exceptions.EmployeeNotFoundException;
import com.bestdevelloperarchi.revurestapi.repositories.EmployeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

//we build our test class and  we attach to mockito extension to it
/*
Alternatively, we can enable Mockito annotations programmatically by invoking MockitoAnnotations.openMocks():

* @BeforeEach
public void init() {
    MockitoAnnotations.openMocks(this);
}
* */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    // we attach the @Mock annotation to inject for an instance variable that we can use anywhere in the test class
    @Mock
    private EmployeRepository employeRepository;


    @InjectMocks
    private EmployeeServiceImpl employeeService;
    // explaination for this: https://stackoverflow.com/questions/16467685/difference-between-mock-and-injectmocks

    private EmployeeRequest employeeRequest;
    //@BeforeEach is used to signal that the annotated method should be executed before each @Test method in the current test class.


    @BeforeEach
    public void setup(){
        employeeRequest = EmployeeRequest.builder()
                .name("Abdiooa")
                .role("Developer")
                .mobileNo("7878787879")
                .emailId("abdiooa45@gmail.com")
                .build();
    }


    @DisplayName(" Junit test for save Employee method")
    @Test
    void saveEmployee() {
        // given - precondition or setup
        given(employeRepository.existsByEmailId(employeeRequest.getEmailId())).willReturn(false);
        Employee employee= employeeService.mapToEmployee(employeeRequest);
        given(employeRepository.save(employee)).willReturn(employee);
        // when -  action or the behaviour that we are going test
        EmployeeResponse employeeResponse=employeeService.saveEmployee(employeeRequest);
        // then - verify the output
        assertThat(employeeResponse).isNotNull();
    }
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        //  given - precondition or setup
        given(employeRepository.existsByEmailId(employeeRequest.getEmailId()))
                .willReturn(true);

        // when -  action or the behaviour that we are going test
        org.junit.jupiter.api.Assertions.assertThrows(EmailAlreadyExistsException.class,()->{
           employeeService.saveEmployee(employeeRequest);
        });

        // then
        verify(employeRepository, never()).save(any(Employee.class));
    }


    @DisplayName(" Junit test for getAllEmployee method")
    @Test
    void getAllEmployees() {

        // given - precondition or setup
        EmployeeRequest employeeRequest1 = EmployeeRequest.builder()
                .name("Salman")
                .role("Actor")
                .mobileNo("7894561230")
                .emailId("salman@gmail.com")
                .build();
        List<Employee> employees = new ArrayList<>();
        employees.add(employeeService.mapToEmployee(employeeRequest));
        employees.add(employeeService.mapToEmployee(employeeRequest1));
        given(employeRepository.findAll()).willReturn(employees);

        // when -  action or the behaviour that we are going test
        List<EmployeeResponse> employeeResponses = employeeService.getAllEmployees();
        // then - verify the output
        assertThat(employeeResponses).isNotNull();
        assertThat(employeeResponses.size()).isEqualTo(2);

    }


    @DisplayName("JUnit test for getAllEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){
        EmployeeRequest employeeRequest1 = EmployeeRequest.builder()
                .name("Salman")
                .role("Actor")
                .mobileNo("7894561230")
                .emailId("salman@gmail.com")
                .build();
        given(employeRepository.findAll()).willReturn(Collections.emptyList());
        List<EmployeeResponse> employeeResponses = employeeService.getAllEmployees();
        // then - verify the output
        assertThat(employeeResponses).isEmpty();
        assertThat(employeeResponses.size()).isEqualTo(0);
    }
    /*
    *
    * The first test is for the positive scenario, where there are employees in the database. The second test is for the negative scenario, where there are no employees in the database.
In the first test, we first create an EmployeeRequest object and then add it to a list of employees. We then call the findAll() method on the employeeRepository mock and pass in the list of employees. The mock returns the list of employees, which is then passed to the getAllEmployees() method on the employeeService. The getAllEmployees() method then converts the list of employees to a list of EmployeeResponse objects and returns the list.
In the second test, we first create an EmployeeRequest object and then call the findAll() method on the employeeRepository mock. The mock returns an empty list of employees. The getAllEmployees() method then converts the empty list of employees to a list of EmployeeResponse objects and returns the empty list.
    * */

    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        //given
        Employee employee = employeeService.mapToEmployee(employeeRequest);
        given(employeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when
        EmployeeResponse savedEmployee = employeeService.findEmployeeById(1L);

        // then
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    void deleteEmpOne(){
    }

    @Test
    void updateEmployee() {
    }
}