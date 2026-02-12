package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class CompanyDTO {
    private Long id;
    private String name;
    private Double budget;
    private List<EmployeeDTO> employees;
}
