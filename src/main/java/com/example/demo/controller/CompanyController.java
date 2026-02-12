package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Company;
import com.example.demo.service.CompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    
    @PostMapping
    public ResponseEntity<Company> create(@RequestBody Company company) {
        return ResponseEntity.ok(companyService.createCompany(company));
    }

    // N+1 PROBLEMLİ LİSTELEME (Bunu problemi gözlemlemek için kullan)
    @GetMapping("/n-plus-one-test")
    public List<Company> getAllWithProblem() {
        // Bu metod repository'deki standart findAll()'u kullanırsa konsolda N+1 sorgu görürsün
        return companyService.getAllStandard(); 
    }

    // N+1 ÇÖZÜLMÜŞ LİSTELEME (Performanslı hali)
    @GetMapping("/optimized")
    public List<Company> getAllOptimized() {
        // JOIN FETCH kullandığımız servisi çağırır
        return companyService.getAllCompaniesWithDetails();
    }
}
