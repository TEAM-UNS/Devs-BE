package com.example.devs.domain.major.domain.repository;

import com.example.devs.domain.major.domain.MajorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorRepository extends JpaRepository<MajorEntity, Long> {
}
