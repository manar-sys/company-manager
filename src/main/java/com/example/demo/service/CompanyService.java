package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CompanyDTO;
import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Company;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    // -------------------------------------------------
    // N+1 Problemini TETİKLEYEN metod
    // -------------------------------------------------
    public List<Company> getAllStandard() {
        return companyRepository.findAll();
    }

    // -------------------------------------------------
    // N+1 Çözümlü listeleme
    // -------------------------------------------------
    @Transactional(readOnly = true)
    public List<CompanyDTO> getAllCompaniesWithDetails() {

        List<Company> companies = companyRepository.findAllWithDetails();

        return companies.stream()
                .map(this::mapToCompanyDTO)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------
    // CREATE
    // -------------------------------------------------
    @Transactional
    public CompanyDTO createCompany(CompanyDTO dto) {

        String trimmedName = dto.getName().trim();

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
    // UPDATE
    // -------------------------------------------------
    @Transactional
    public CompanyDTO updateCompany(Long id, CompanyDTO dto) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        company.setName(dto.getName().trim());
        company.setBudget(dto.getBudget());

        Company updated = companyRepository.save(company);

        return mapToCompanyDTO(updated);
    }

    // -------------------------------------------------
    // DELETE
    // -------------------------------------------------
    @Transactional
    public void deleteCompany(Long id) {

        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company not found");
        }

        companyRepository.deleteById(id);
    }

    // -------------------------------------------------
    // PAGINATION + SORTING
    // -------------------------------------------------
    @Transactional(readOnly = true)
    public Page<CompanyDTO> listCompanies(int page, int size, String sortBy) {

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(sortBy).ascending()
        );

        Page<Company> companies = companyRepository.findAll(pageRequest);

        return companies.map(this::mapToCompanyDTO);
    }

    // -------------------------------------------------
    // MAPPER
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