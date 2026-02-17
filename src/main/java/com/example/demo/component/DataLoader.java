package com.example.demo.component;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Company;
import com.example.demo.entity.Employee;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Admin admin = new Admin();
        admin.setUsername("manar");
        admin.setEmail("admin@company.com");
        adminRepository.save(admin);

        Company techCorp = new Company();
        techCorp.setName("Tech Corp");
        techCorp.setBudget(BigDecimal.valueOf(500000));
        techCorp.setAdmin(admin);

        Company creativeAgency = new Company();
        creativeAgency.setName("Creative Agency");
        creativeAgency.setBudget(BigDecimal.valueOf(200000));
        creativeAgency.setAdmin(admin);

        companyRepository.saveAll(Arrays.asList(techCorp, creativeAgency));

        Employee emp1 = new Employee();
        emp1.setFirstName("Ali");
        emp1.setLastName("Kaya");
        emp1.setSalary(BigDecimal.valueOf(10000));
        emp1.setCompany(techCorp);

        Employee emp2 = new Employee();
        emp2.setFirstName("Ayşe");
        emp2.setLastName("Yılmaz");
        emp2.setSalary(BigDecimal.valueOf(12000));
        emp2.setCompany(techCorp);

        Employee emp3 = new Employee();
        emp3.setFirstName("Can");
        emp3.setLastName("Demir");
        emp3.setSalary(BigDecimal.valueOf(8000));
        emp3.setCompany(creativeAgency);

        employeeRepository.saveAll(Arrays.asList(emp1, emp2, emp3));

        System.out.println("#########################################");
        System.out.println("Örnek veriler başarıyla yüklendi!");
        System.out.println("#########################################");
    }
}