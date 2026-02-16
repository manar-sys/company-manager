package com.example.demo.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Company;
import com.example.demo.entity.Employee;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public String hireEmployee(Long companyId, EmployeeDTO dto) {

        // 1️⃣ company var mı kontrol
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Company not found with id: " + companyId)
                );

        BigDecimal salary = dto.getSalary();
        BigDecimal currentBudget = company.getBudget();

        // 2️⃣ bütçe kontrolü
        if (currentBudget.compareTo(salary) < 0) {
            throw new BusinessException("Company does not have enough budget for this salary.");
        }

        // 3️⃣ yeni employee oluştur
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setSalary(salary);
        employee.setCompany(company);

        // 4️⃣ budget düşür
        company.setBudget(currentBudget.subtract(salary));

        // 5️⃣ kaydet
        employeeRepository.save(employee);
        companyRepository.save(company);

        return "Employee hired successfully to company: " + company.getName();
    }
}