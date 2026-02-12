package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    // Çalışan işe alma (Bütçe kontrolü serviste yapılıyor)
    @PostMapping("/{companyId}/hire")
    public ResponseEntity<String> hire(@PathVariable Long companyId, @RequestBody Employee employee) {
        String result = employeeService.hireEmployee(companyId, employee);
        return ResponseEntity.ok(result);
    }
}
