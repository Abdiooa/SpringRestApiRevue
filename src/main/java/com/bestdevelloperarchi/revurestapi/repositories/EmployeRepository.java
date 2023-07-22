package com.bestdevelloperarchi.revurestapi.repositories;

import com.bestdevelloperarchi.revurestapi.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeRepository extends JpaRepository<Employee,Long> {
    boolean existsByEmailId(String emailId);

    Optional<Employee> findByEmailId(String emailId);
}
