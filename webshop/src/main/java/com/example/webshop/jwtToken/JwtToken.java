package com.example.webshop.jwtToken;

import com.example.webshop.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private JwtTokenType jwtTokenType = JwtTokenType.BEARER;
    private boolean revoked;
    private boolean expired;
    @CreationTimestamp
    private Timestamp timestamp;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
