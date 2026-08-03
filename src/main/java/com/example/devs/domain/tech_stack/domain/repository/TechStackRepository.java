package com.example.devs.domain.tech_stack.domain.repository;

import com.example.devs.domain.tech_stack.domain.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {
}
