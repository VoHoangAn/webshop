package com.example.webshop.config;

import com.example.webshop.auth.AuthRequest;
import com.example.webshop.user.UserRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//kHAI BAO THANG
import java.io.IOException;
public class FirstTimeLoginFilter extends AbstractAuthenticationProcessingFilter {
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER
            = new AntPathRequestMatcher("/api/v1/auth/authenticate", "POST");
    private String usernameParameter = "email";
    private String passwordParameter = "password";
    private boolean postOnly = true;
    @Autowired
    private final UserRepositoryImpl userRepository;
//    private AuthenticationManager authenticationManager;
    public FirstTimeLoginFilter(UserRepositoryImpl userRepository) {super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.userRepository = userRepository;
    }
//    public FirstTimeLoginFilter(AuthenticationManager authenticationManager) {
//        super(DEFAULT_ANT_PATH_REQUEST_MATCHER,authenticationManager);
//    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            ObjectMapper mapper = new ObjectMapper();
            AuthRequest authData = mapper.readValue(request.getInputStream(), AuthRequest.class);

            String username = authData.getEmail();// this.obtainUsername(request);
            if(!this.userRepository.existsUserByEmail(username))
                {throw new IllegalAccessError("User hasnt existed") ;}

            username = username != null ? username.trim() : "";
            String password = authData.getPassword();//this.obtainPassword(request);
            password = password != null ? password : "";
            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
            this.setDetails(request, authRequest);
//            this.getSuccessHandler().onAuthenticationSuccess(request,response,authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    @Nullable
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.passwordParameter);
    }

    @Nullable
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.usernameParameter);
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
