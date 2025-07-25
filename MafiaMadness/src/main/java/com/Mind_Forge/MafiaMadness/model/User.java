package com.Mind_Forge.MafiaMadness.model;



import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

// Our entity class that represents data model in our application
// Maps directly to database table
// Used Lombok Annotation to Auto generate Getters & Setters 

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    private boolean enabled;
    
    @Column(name = "verification_code")
    private String verificationCode;
    
    @Column(name = "verification_experation")
    private LocalDateTime verificationCodeExpiresAt;



    // Generated Constructor including all fields excluding id
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //Generated a no args constructor
    public User(){}



    // Methods from UserDetails that are Overridden for development we shall not include logic yet just return
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }
    @Override
    public boolean isEnabled(){
        return enabled;
    }
    

}
