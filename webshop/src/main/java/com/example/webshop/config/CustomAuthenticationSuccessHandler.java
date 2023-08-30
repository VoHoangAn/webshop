package com.example.webshop.config;

import com.example.webshop.jwtToken.JwtToken;
import com.example.webshop.jwtToken.JwtTokenRepository;
import com.example.webshop.service.JwtService;
import com.example.webshop.user.User;
import com.example.webshop.user.UserRepositoryImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@RequiredArgsConstructor
//AuthenticationSuccessHandler
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepositoryImpl userRepository;
    private final JwtTokenRepository jwtTokenRepository;

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

//        LOGGER.error("running cookie");
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
        var user = userRepository.findByEmail(authToken.getName())
                .orElseThrow(()->new IllegalStateException("user not exist"));

        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(jwtToken,user);

        Cookie cookie = new Cookie("auth.access_token",jwtToken);
//        cookie.setSecure(false);
        cookie.setPath("/");
//        LOGGER.error("running cookie");
        response.addCookie(cookie);
    }

    public void revokeAllUserTokens(User user) {
        var listValidUserToken = jwtTokenRepository.findAllValidTokenByUser(user.getId());
        if(listValidUserToken.isEmpty()) {
            return;
        }
        listValidUserToken.forEach(jwtToken -> {
            jwtToken.setRevoked(true);
            jwtToken.setExpired(true);
        });
        jwtTokenRepository.saveAll(listValidUserToken);
    }

    public void saveUserToken(String jwtToken, User savedUser) {
        JwtToken token = JwtToken.builder().user(savedUser)
                .token(jwtToken)
                .revoked(false)
                .expired(false)
                .build();
        jwtTokenRepository.save(token);
    }
}
