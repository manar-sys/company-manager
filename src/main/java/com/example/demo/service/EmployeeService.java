package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Company;
import com.example.demo.entity.Employee;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.EmployeeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Final alanlar için constructor oluşturur (SOLID - DI)
public class EmployeeService {
    private final EmployeeRepository employeeRepository; 
    private final CompanyRepository companyRepository; 

    @Transactional
    public String hireEmployee(Long companyId, Employee employee) {
        
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Şirket bulunamadı!"));

        
        if (company.getBudget() < employee.getSalary()) {
            return "Hata: Şirketin bu maaşı karşılayacak bütçesi yok!";
        }

        // Şirket bütçesini güncelleme işlemini burada yapıyoruz
        company.setBudget(company.getBudget() - employee.getSalary());
        
        employee.setCompany(company);
        employeeRepository.save(employee);
        companyRepository.save(company); // Güncel bütçeyi kaydet

        return "Çalışan " + company.getName() + " şirketine başarıyla atandı.";
    }
    
}
