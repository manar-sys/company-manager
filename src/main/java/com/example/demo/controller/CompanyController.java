package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // CREATE
    @PostMapping
    public ResponseEntity<CompanyDTO> create(
            @Valid @RequestBody CompanyDTO companyDTO) {

        return ResponseEntity.ok(
                companyService.createCompany(companyDTO)
        );
    }

    // N+1 problem test
    @GetMapping("/n-plus-one-test")
    public List<Company> getAllWithProblem() {
        return companyService.getAllStandard();
    }

    // Optimized list
    @GetMapping("/optimized")
    public ResponseEntity<List<CompanyDTO>> getCompaniesOptimized() {
        return ResponseEntity.ok(
                companyService.getAllCompaniesWithDetails()
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyDTO dto
    ) {
        return ResponseEntity.ok(companyService.updateCompany(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    // PAGINATION + SORTING
    @GetMapping
    public ResponseEntity<Page<CompanyDTO>> listCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return ResponseEntity.ok(
                companyService.listCompanies(page, size, sortBy)
        );
    }
}