package com.example.demo.models;

import com.example.demo.DTO.UserDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "doctors")
@Data
@SuperBuilder
@NoArgsConstructor
public class Doctor extends User {

    //despues agregar mas atributos

    public Doctor(UserDTO userDTO) {
        super(userDTO);
        this.setRole(Role.ROLE_DOCTOR);
    }
}
