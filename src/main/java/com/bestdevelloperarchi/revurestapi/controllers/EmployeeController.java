package com.bestdevelloperarchi.revurestapi.controllers;

import com.bestdevelloperarchi.revurestapi.dto.EmployeeRequest;
import com.bestdevelloperarchi.revurestapi.dto.EmployeeResponse;
import com.bestdevelloperarchi.revurestapi.exceptions.EmailAlreadyExistsException;
import com.bestdevelloperarchi.revurestapi.exceptions.EmployeeNotFoundException;
import com.bestdevelloperarchi.revurestapi.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EmployeeController {
    private final EmployeeService employeeService;
    @PostMapping("")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeRequest employeeRequest){
        try{
            EmployeeResponse employeeResponse = employeeService.saveEmployee(employeeRequest);
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(employeeResponse,headers,HttpStatus.CREATED);
        }catch (EmailAlreadyExistsException emailAlreadyExistsException){
            return new ResponseEntity<>("Email already used by other employee we sorry",HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            return new ResponseEntity<>("An error occurred while processing the request.",HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees(){
        try{
            List<EmployeeResponse> employeeResponses = employeeService.getAllEmployees();
            return new ResponseEntity<>(employeeResponses,HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    // First Approach to handle Exception on Spring Boot
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable("id") Long id){
        try {
            EmployeeResponse employeeResponse = employeeService.findEmployeeById(id);
            return new ResponseEntity<>(employeeResponse,HttpStatus.OK);
        }catch (EmployeeNotFoundException employeeNotFoundException){
            return new ResponseEntity<>("An employee with this identifiant does not exists",HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") Long id, @RequestBody EmployeeRequest employeeRequest){
        try{
            EmployeeResponse employeeResponse = employeeService.updateEmployee(id,employeeRequest);
            return new ResponseEntity<>(employeeResponse,HttpStatus.OK);
        }catch (EmployeeNotFoundException employeeNotFoundException){
            return new ResponseEntity<>("An employee with this identifiant does not exists",HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    // First Approach to handle Exception on Spring Boot
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long id){
        try{
            employeeService.deleteEmp(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (EmployeeNotFoundException employeeNotFoundException){
            return new ResponseEntity<>("An employee with this identifiant does not exists",HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
