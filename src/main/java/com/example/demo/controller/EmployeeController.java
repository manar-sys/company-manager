package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/{companyId}/hire")
    public ResponseEntity<String> hire(
            @PathVariable Long companyId,
            @Valid @RequestBody EmployeeDTO employeeDTO) {

        String result = employeeService.hireEmployee(companyId, employeeDTO);

        return ResponseEntity.ok(result);
    }
}