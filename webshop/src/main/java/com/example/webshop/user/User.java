package com.example.webshop.user;

import com.example.webshop.jwtToken.JwtToken;
import com.example.webshop.address.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    @JsonValue
    private Long id;
    private String firstName;
    private String lastname;
    private String email;
    private String phone;
    private String image;
    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Address> address;
    private String password;
    private boolean locked;
    private boolean enabled;
    @CreationTimestamp
    private Timestamp timestamp;
//    @OneToMany(mappedBy = "user")
//    @JsonIgnore
//    private List<JwtToken> jwtTokens;

    @Enumerated(EnumType.STRING)
    private Role role;
//    @Enumerated(EnumType.STRING)
//    private State state = State.UNLOCKED;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
