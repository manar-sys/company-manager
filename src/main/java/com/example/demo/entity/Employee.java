package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private double salary;

    @ManyToOne (fetch=FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;
    
}
