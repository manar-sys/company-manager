package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Company;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.CompanyRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public void assignAdminToCompany(Long adminId, Long companyId) {
    // Şirketi ve Admini bul 
    Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Şirket bulunamadı!"));
    Admin admin = adminRepository.findById(adminId)
            .orElseThrow(() -> new RuntimeException("Admin bulunamadı!"));

    // Bu şirketin zaten bir admini var mı?
    if (company.getAdmin() != null) {
        throw new RuntimeException("Hata: Bu şirketin zaten bir sorumlusu var: " 
                                    + company.getAdmin().getUsername());
    }

    // 3. Atamayı yap ve kaydet
    company.setAdmin(admin);
    companyRepository.save(company); 
    }
}
