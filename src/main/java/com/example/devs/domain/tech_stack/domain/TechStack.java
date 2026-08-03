package com.example.devs.domain.tech_stack.domain;

import com.example.devs.domain.major.domain.Major;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "tbl_tech_stack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_stack_id",nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;

    @Column(name = "tech_stack_name",nullable = false)
    private String name;
}
