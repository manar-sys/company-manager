package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    // Admin'e şirket atama (Admin'in yönetebileceği şirketleri belirleme)
    @PostMapping("/{adminId}/assign-company/{companyId}")
    public ResponseEntity<Void> assign(@PathVariable Long adminId, @PathVariable Long companyId) {
        adminService.assignAdminToCompany(adminId, companyId);
        return ResponseEntity.noContent().build();
    }
}
