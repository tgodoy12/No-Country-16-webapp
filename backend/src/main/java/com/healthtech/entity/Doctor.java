package com.healthtech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "doctors")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "user_id")
public class Doctor extends User {

    // Esta anotación @Column es importante para mapear correctamente
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    private String specialization;

    private String qualification;

    @Column(name = "experience_years")
    private Integer experienceYears;

    private String bio;
}