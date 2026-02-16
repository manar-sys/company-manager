package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CompanyDTO;
import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Company;
import com.example.demo.exception.BusinessException;
import com.example.demo.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    // -------------------------------------------------
    // N+1 Problemini TETİKLEYEN metod (bilerek entity dönüyor)
    // -------------------------------------------------
    public List<Company> getAllStandard() {
        return companyRepository.findAll();
    }

    // -------------------------------------------------
    // N+1 Çözümlü listeleme (JOIN FETCH)
    // -------------------------------------------------
    @Transactional(readOnly = true)
    public List<CompanyDTO> getAllCompaniesWithDetails() {

        List<Company> companies = companyRepository.findAllWithDetails();

        return companies.stream()
                .map(this::mapToCompanyDTO)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------
    // Company oluşturma (DTO ile)
    // -------------------------------------------------
    @Transactional
    public CompanyDTO createCompany(CompanyDTO dto) {

        String trimmedName = dto.getName().trim();

        // Aynı isimde şirket kontrolü
        if (companyRepository.existsByName(trimmedName)) {
            throw new BusinessException("Company already exists with name: " + trimmedName);
        }

        Company company = new Company();
        company.setName(trimmedName);
        company.setBudget(dto.getBudget());

        Company saved = companyRepository.save(company);

        return mapToCompanyDTO(saved);
    }

    // -------------------------------------------------
    // Mapper metodları (temiz ve tekrar kullanılabilir)
    // -------------------------------------------------

    private CompanyDTO mapToCompanyDTO(Company company) {

        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setBudget(company.getBudget());

        if (company.getEmployees() != null) {
            dto.setEmployees(
                    company.getEmployees()
                            .stream()
                            .map(emp -> {
                                EmployeeDTO eDto = new EmployeeDTO();
                                eDto.setId(emp.getId());
                                eDto.setFirstName(emp.getFirstName());
                                eDto.setLastName(emp.getLastName());
                                eDto.setSalary(emp.getSalary());
                                return eDto;
                            })
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
}