//package com.example.webshop.config;
//
//import com.example.webshop.user.User;
//import com.example.webshop.user.UserRepository;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//@Component
//@RequiredArgsConstructor
//public class FakeUserLogin extends OncePerRequestFilter {
//	private final UserRepository userRepository;
//	private final static Logger LOGGER = LoggerFactory.getLogger(FakeUserLogin.class);
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//		User user = userRepository.findByUser(1l);
////		if(user==null) {
////			filterChain.doFilter(request,response);
////		}
//		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//				user,
//				null,
//				user.getAuthorities()
//		);
//
//		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//		filterChain.doFilter(request,response);
//	}
//}
