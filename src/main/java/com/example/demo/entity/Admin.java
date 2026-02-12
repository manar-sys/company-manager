package com.example.demo.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter 
@Setter
public class Admin extends BaseEntity {
    private String username;
    private String email;
    
    @OneToMany
    @JsonIgnore
    private List<Company> managedCompanies;
}
