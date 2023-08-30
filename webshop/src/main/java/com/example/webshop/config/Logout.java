package com.example.webshop.config;

import com.example.webshop.jwtToken.JwtTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Logout implements LogoutHandler {
    private final JwtTokenRepository tokenRepository;

    private final static Logger LOGGER = LoggerFactory.getLogger(Logout.class);
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LOGGER.error("logout");
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        if(authHeader==null||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwtToken = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwtToken)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}
