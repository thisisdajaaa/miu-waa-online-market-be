package com.example.minionlinemarket.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
// issues rising because of the abstract class jackason cant properly deserialize refer to the README.md file
public class MyUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    //    private String username; changed to avoid conflict with hibernate
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, length = 100, nullable = false)
    private String email;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    public String getUsername() {
        return email;
    }

}


