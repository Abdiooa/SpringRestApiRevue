package com.bestdevelloperarchi.revurestapi.services;

import com.bestdevelloperarchi.revurestapi.dto.EmployeeRequest;
import com.bestdevelloperarchi.revurestapi.dto.EmployeeResponse;
import com.bestdevelloperarchi.revurestapi.entities.Employee;
import com.bestdevelloperarchi.revurestapi.exceptions.EmailAlreadyExistsException;
import com.bestdevelloperarchi.revurestapi.exceptions.EmployeeNotFoundException;
import com.bestdevelloperarchi.revurestapi.repositories.EmployeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableCaching
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeRepository employeRepository;
    @Override
    @Cacheable("employees")
    public List<EmployeeResponse> getAllEmployees() {
        doLongRunningTask();
        return employeRepository.findAll().stream().map(this::mapToPersonResponse).collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = {"employees","employee"}, allEntries = true)
    public EmployeeResponse saveEmployee(EmployeeRequest employeeRequest) {
        if(employeRepository.existsByEmailId(employeeRequest.getEmailId())){
            throw new EmailAlreadyExistsException();
        }
        validateInput(employeeRequest);
        Employee employee = mapToEmployee(employeeRequest);
        employeRepository.save(employee);
        log.info(" Employee {} saved successfully.",employee.getId());
        return mapToPersonResponse(employee);
    }

    @Override
    @Cacheable(value = "employee",key = "#employeeId")
//    @CacheEvict(value = {"employee","employees"}, allEntries = true)
    public EmployeeResponse findEmployeeById(Long employeeId) {
        doLongRunningTask();
        if(employeRepository.findById(employeeId).isEmpty()){
            throw new EmployeeNotFoundException();
        }
        Optional<Employee> optionalEmployee = employeRepository.findById(employeeId);
        if(!optionalEmployee.isPresent()){
            throw new IllegalArgumentException("Employee with the id "+ employeeId+ " not found");
        }
        return mapToPersonResponse(optionalEmployee.get());
    }

    @Override
//    @CacheEvict(value = "employee", key = "#employeeId")
    @CacheEvict(value = {"employee","employees"}, allEntries = true)
    public void deleteEmp(Long employeeId) {
        if(employeRepository.findById(employeeId).isEmpty()){
            throw new EmployeeNotFoundException();
        }
        Optional<Employee> optionalEmployee = employeRepository.findById(employeeId);
        if(!optionalEmployee.isPresent()){
            throw new IllegalArgumentException("Employee with the id "+ employeeId+ " not found");
        }
        employeRepository.deleteById(employeeId);
        //employeRepository.delete(optionalEmployee.get());
    }

    @Override
    @CacheEvict(value = "employee", key = "#employeeId")
    public EmployeeResponse updateEmployee(Long employeeId, EmployeeRequest employeeRequest) {
        if(employeRepository.findById(employeeId).isEmpty()){
            throw new EmployeeNotFoundException();
        }
        Optional<Employee> optionalEmployee = employeRepository.findById(employeeId);
        if(!optionalEmployee.isPresent()){
            throw new IllegalArgumentException("Employee with the id "+ employeeId+ " not found");
        }
        Employee employee = optionalEmployee.get();
        if (employeeRequest.getName()!=null && !employeeRequest.getName().isEmpty()){
            employee.setName(employeeRequest.getName());
        }
        if (employeeRequest.getRole()!=null && !employeeRequest.getRole().isEmpty()){
            employee.setRole(employeeRequest.getRole());
        }
        if (employeeRequest.getMobileNo()!=null && !employeeRequest.getMobileNo().isEmpty()){
            employee.setMobileNo(employeeRequest.getMobileNo());
        }
        if (employeeRequest.getEmailId()!=null && !employeeRequest.getEmailId().isEmpty()){
            employee.setEmailId(employeeRequest.getEmailId());
        }
        employeRepository.save(employee);
        log.info("Person {} updated succefully.",employee.getId());
        return mapToPersonResponse(employee);
    }

    public void validateInput(EmployeeRequest employeeRequest){
        if(employeeRequest.getName() == null || employeeRequest.getRole() == null ||
                employeeRequest.getMobileNo() == null || employeeRequest.getEmailId() == null){
            throw new IllegalArgumentException(
                    "Invalid input data."
            );
        }
    }
    public Employee mapToEmployee(EmployeeRequest employeeRequest){
        return Employee.builder()
                .name(employeeRequest.getName())
                .role(employeeRequest.getRole())
                .mobileNo(employeeRequest.getMobileNo())
                .emailId(employeeRequest.getEmailId())
                .build();
    }
    public EmployeeResponse mapToPersonResponse(Employee employee){
        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .role(employee.getRole())
                .mobileNo(employee.getMobileNo())
                .emailId(employee.getEmailId())
                .build();
    }
    private void doLongRunningTask() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
