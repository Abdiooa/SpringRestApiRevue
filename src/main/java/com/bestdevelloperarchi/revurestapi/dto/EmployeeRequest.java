package com.bestdevelloperarchi.revurestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    private String name;
    private String role;
    private String mobileNo;
    private String emailId;
}
