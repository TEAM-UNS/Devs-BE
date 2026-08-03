package com.example.devs.domain.major.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "tbl_open_time")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MajorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_id",nullable = false)
    private Long id;

    @Column(name = "major_name", nullable = false)
    private String name;
}
