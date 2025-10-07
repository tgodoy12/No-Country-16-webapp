package com.example.demo.models;

import com.example.demo.DTO.UserDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "patients")
@Data
@SuperBuilder
@NoArgsConstructor
public class Patient extends User {

    // cuando se defina los atributos los agregamos

    public Patient(UserDTO userDTO) {
        super(userDTO);
        this.setRole(Role.ROLE_PATIENT);
    }


}
