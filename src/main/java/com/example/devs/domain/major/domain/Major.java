package com.example.devs.domain.major.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "tbl_major")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_id",nullable = false)
    private Long id;

    @Column(name = "major_name", nullable = false)
    private String name;
}
