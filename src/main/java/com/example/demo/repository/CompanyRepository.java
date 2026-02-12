package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    // Standart findAll() yerine bunu kullanadığımızda,
    // şirketler ve onların çalışanları tek bir sorguyla çekilir, 
    // böylece N+1 probleminden kaçınılır.:
    @Query("SELECT c FROM Company c LEFT JOIN FETCH c.admin LEFT JOIN FETCH c.employees")
    List<Company> findAllWithDetails();

    boolean existsByName(String name);
}
