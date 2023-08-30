package com.example.webshop.config;

import com.example.webshop.user.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final Logout logoutHandler;
//    private final FirstTimeLoginFilter firstTimeLoginFilter;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final UserRepositoryImpl userRepository;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests().requestMatchers("/api/v1/auth/**","/tst/**","/order/momoNotification").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                .formLogin()
//                .loginProcessingUrl("/api/v1/auth/authenticate")
//                .successHandler(authenticationSuccessHandler)
//                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(firstTimeLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter,UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .permitAll();


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Bean
//    public FilterRegistrationBean JwtAuthRegistrationBean() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(jwtAuthFilter);
//        filterRegistrationBean.setOrder(1);
//        return filterRegistrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean LoginAuthRegistrationBean() throws Exception {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(firstTimeLoginFilter());
//        filterRegistrationBean.setOrder(2);
//        return filterRegistrationBean;
//    }

    @Bean
    public FirstTimeLoginFilter firstTimeLoginFilter() throws Exception {
        FirstTimeLoginFilter firstTimeLoginFilter = new FirstTimeLoginFilter(userRepository);
        firstTimeLoginFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        firstTimeLoginFilter.setAuthenticationManager(authenticationManager());
        return firstTimeLoginFilter;
    }

}
