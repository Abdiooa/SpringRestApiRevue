package com.bestdevelloperarchi.revurestapi.repositories;

import com.bestdevelloperarchi.revurestapi.dto.EmployeeRequest;
import com.bestdevelloperarchi.revurestapi.entities.Employee;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;


import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeRepositoryTest {
    @Autowired
    private EmployeRepository employeRepository;
    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveEmployeeTest(){
        Employee employee = Employee.builder()
                .name("Abdi")
                .role("Software Engineer")
                .mobileNo("7894561230")
                .emailId("martelluiz125@gmail.com")
                .build();
        employeRepository.save(employee);
        Assertions.assertThat(employee.getId()).isGreaterThan(0);
    }
    @Test
    @Order(3)
    public void getListOfEmployeesTest(){
        List<Employee> employees = employeRepository.findAll();
        Assertions.assertThat(employees.size()).isGreaterThan(0);
    }
    @Test
    @Order(2)
    public void getEmployeeTest(){
        Employee employee = employeRepository.findById(1L).get();
        Assertions.assertThat(employee.getId()).isEqualTo(1L);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateEmployeeTest(){
        Employee employee = employeRepository.findById(1L).get();
        employee.setEmailId("abdiooa45@gmail.com");

        Employee employeeUpdate = employeRepository.save(employee);

        Assertions.assertThat(employeeUpdate.getEmailId()).isEqualTo("abdiooa45@gmail.com");

    }

    @Test
    @Order(6)
    @Rollback(value = false)
    public void deleteEmployee(){
        Employee employee = employeRepository.findById(1L).get();

        employeRepository.delete(employee);
        Employee employee1 = null;
        Optional<Employee> optionalEmployee = employeRepository.findByEmailId("abdiooa45@gmail.com");
        if (optionalEmployee.isPresent()){
            employee1 = optionalEmployee.get();
        }
        Assertions.assertThat(employee1).isNull();
    }

    @Test
    @Order(5)
    void existsByEmailId_when_employeeexists() {
        boolean exist = employeRepository.existsByEmailId("abdiooa45@gmail.com");
        Employee employee = null;
        if(exist){
            employee = employeRepository.findByEmailId("abdiooa45@gmail.com").get();
            System.out.println(employee);
        }
        Assertions.assertThat(employee).isNotNull();
        Assertions.assertThat(exist).isEqualTo(true);
    }
    @Test
    @Order(7)
    void existsByEmailId_when_employeedonotexists() {
        boolean exist = employeRepository.existsByEmailId("abdiooa45@gmail.com");
        Employee employee = null;
        if(exist){
            employee = employeRepository.findByEmailId("abdiooa45@gmail.com").get();
            System.out.println(employee);
        }
        Assertions.assertThat(employee).isNull();
        Assertions.assertThat(exist).isEqualTo(false);
    }
}