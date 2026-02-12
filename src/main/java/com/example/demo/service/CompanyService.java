package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CompanyDTO;
import com.example.demo.dto.EmployeeDTO;
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
   @Transactional(readOnly = true)
    public List<CompanyDTO> getAllCompaniesWithDetails() {
        // JOIN FETCH kullanan
        List<Company> companies = companyRepository.findAllWithDetails();

        return companies.stream().map(company -> {
            CompanyDTO dto = new CompanyDTO();
            dto.setId(company.getId());
            dto.setName(company.getName());
            dto.setBudget(company.getBudget());
            
            // DTO içinde EmployeeDTO listesi oluşturup dolduruyoruz
            if (company.getEmployees() != null) {
                dto.setEmployees(company.getEmployees().stream().map(emp -> {
                    EmployeeDTO eDto = new EmployeeDTO();
                    eDto.setId(emp.getId());
                    eDto.setFirstName(emp.getFirstName());
                    eDto.setLastName(emp.getLastName());
                    eDto.setSalary(emp.getSalary());
                    return eDto;
                }).collect(Collectors.toList()));
            }
            return dto;
        }).collect(Collectors.toList());
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
