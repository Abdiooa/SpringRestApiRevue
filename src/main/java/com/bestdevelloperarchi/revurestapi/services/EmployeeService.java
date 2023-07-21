package com.bestdevelloperarchi.revurestapi.services;

import com.bestdevelloperarchi.revurestapi.dto.EmployeeRequest;
import com.bestdevelloperarchi.revurestapi.dto.EmployeeResponse;
import com.bestdevelloperarchi.revurestapi.entities.Employee;
import com.bestdevelloperarchi.revurestapi.exceptions.EmailAlreadyExistsException;
import com.bestdevelloperarchi.revurestapi.exceptions.EmployeeNotFoundException;

import java.util.List;

public interface EmployeeService {
    public List<EmployeeResponse> getAllEmployees();
    public EmployeeResponse saveEmployee(EmployeeRequest employeeRequest) throws EmailAlreadyExistsException;
    public EmployeeResponse findEmployeeById(Long employeeId) throws EmployeeNotFoundException;
    public void deleteEmp(Long employeeId) throws EmployeeNotFoundException;
    public EmployeeResponse updateEmployee(Long employeeId, EmployeeRequest employeeRequest) throws EmployeeNotFoundException;
}
