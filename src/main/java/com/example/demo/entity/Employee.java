package com.example.demo.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Employee extends BaseEntity {
    private String firstName;
    private String lastName;
    private String position;
    private BigDecimal salary;

    @ManyToOne (fetch=FetchType.LAZY)
    @JoinColumn(name = "company_id")

    private Company company;
    
}
