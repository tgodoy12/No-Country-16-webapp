package com.example.demo.models;

import com.example.demo.DTO.UserDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;


    @Column(unique = true, nullable = false)
    protected String email;

    @Column(nullable = false)
    protected String password;

    @Column(nullable = false)
    protected String firstName;

    @Column(nullable = false)
    protected String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Role role;



    public User(UserDTO userDTO) {
        this.email = userDTO.email();
        this.firstName = userDTO.firstName();
        this.lastName = userDTO.lastName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 🚨 CORRECCIÓN: Usamos Collections.singletonList para asegurar la compatibilidad de tipo
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    // Retorna el campo usado para el login (nuestro email)
    @Override
    public String getUsername() {
        return email;
    }

    // Métodos para el estado de la cuenta (los ponemos en true por defecto para el MVP)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

