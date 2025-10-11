package com.healthtech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "user_id")
public class Patient extends User {

    // Esta anotación @Column es importante para mapear correctamente
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @Column(name = "identification_number", unique = true)
    private String identificationNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String address;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Column(name = "blood_type")
    private String bloodType;

    private String allergies;
}