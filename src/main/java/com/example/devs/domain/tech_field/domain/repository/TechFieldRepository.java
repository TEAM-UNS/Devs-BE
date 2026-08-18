package com.example.devs.domain.tech_field.domain.repository;

import com.example.devs.domain.tech_field.domain.TechField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TechFieldRepository extends JpaRepository<TechField, Integer> {
    List<TechField> findAllByOrderBySortOrderAsc();

    Optional<TechField> findByCodeIgnoreCase(String code);
}
