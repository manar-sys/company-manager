package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CompanyDTO;
import com.example.demo.entity.Company;
import com.example.demo.service.CompanyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyDTO> create(
            @Valid @RequestBody CompanyDTO companyDTO) {

        return ResponseEntity.ok(
                companyService.createCompany(companyDTO)
        );
    }

    // فقط لعرض مشكلة N+1 (Entity يرجع)
    @GetMapping("/n-plus-one-test")
    public List<Company> getAllWithProblem() {
        return companyService.getAllStandard();
    }

    // النسخة المحسنة
    @GetMapping("/optimized")
    public ResponseEntity<List<CompanyDTO>> getCompaniesOptimized() {
        return ResponseEntity.ok(
                companyService.getAllCompaniesWithDetails()
        );
    }
}