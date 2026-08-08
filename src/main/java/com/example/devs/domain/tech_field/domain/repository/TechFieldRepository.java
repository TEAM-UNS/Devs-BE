package com.example.devs.domain.tech_field.domain.repository;

import com.example.devs.domain.tech_field.domain.TechField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechFieldRepository extends JpaRepository<TechField, Integer> {
    List<TechField> findAllByOrderBySortOrderAsc();
}
