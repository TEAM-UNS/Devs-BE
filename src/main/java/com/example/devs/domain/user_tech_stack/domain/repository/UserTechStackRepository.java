package com.example.devs.domain.user_tech_stack.domain.repository;

import com.example.devs.domain.user_tech_stack.domain.UserTechStack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTechStackRepository extends JpaRepository<UserTechStack, Long> {
}
