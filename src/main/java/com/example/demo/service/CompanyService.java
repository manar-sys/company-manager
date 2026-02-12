package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Company;
import com.example.demo.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    // N+1 Problemini TETİKLEYEN metod
    public List<Company> getAllStandard() {
        // Bu metod sadece Şirketleri getirir. 
        // her şirketin içindeki 'employees' listesine erişmeye çalışırsa N+1 başlar.
        return companyRepository.findAll(); 
    }

    // N+1 Çözümlü liste getirme
    public List<Company> getAllCompaniesWithDetails() {
        // Repository'de yazdığımız JOIN FETCH metodu burada çağrılır
        return companyRepository.findAllWithDetails();
    }

    public Company createCompany(Company company) {
        // Gelen verinin boş olup olmadığını kontrol et
        if (company.getName() == null || company.getName().trim().isEmpty()) {
            throw new RuntimeException("Şirket ismi boş olamaz!");
        }

        // İsmin başındaki ve sonundaki boşlukları temizle
        String trimmedName = company.getName().trim();

        // Aynı isimde şirket var mı?
        if (companyRepository.existsByName(trimmedName)) {
            throw new RuntimeException("Bu isimde bir şirket zaten kayıtlı: " + trimmedName);
        }

        // İsmi temizlenmiş haliyle kaydet
        company.setName(trimmedName);
        return companyRepository.save(company);
    }
}
