package com.bestdevelloperarchi.revurestapi.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @SequenceGenerator(
            name = "employee_sequence",
            sequenceName = "employee_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "employee_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;
    @Column(name = "employee_name",nullable = false)
    private String name;
    @Column(name = "employee_role")
    private String role;
    @Column(name = "employee_mobile")
    private String mobileNo;
    @Column(name = "employee_email")
    private String emailId;
}
