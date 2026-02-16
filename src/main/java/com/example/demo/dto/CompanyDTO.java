package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Data
public class CompanyDTO {
    private Long id;

    @NotBlank(message = "Company name must not be blank")
    private String name;

    @NotNull(message = "Budget is required")
    @Positive(message = "Budget must be positive")
    private BigDecimal budget;

    private List<EmployeeDTO> employees;
}
