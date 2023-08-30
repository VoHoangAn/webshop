package com.example.webshop;

import com.example.webshop.service.FileStorageService;
import com.example.webshop.user.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class WebshopApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(WebshopApplication.class, args);
	}

	private final static Logger LOGGER = LoggerFactory.getLogger(WebshopApplication.class);
	private final UserRepositoryImpl userRepository;
	private final PasswordEncoder passwordEncoder;
	private final FileStorageService storageService;

	@Override
	public void run(String... args) throws Exception {
//		storageService.deleteAll();
//		LOGGER.error("RUNNING");
//		User user = User.builder().firstName("vo").lastname("an")
//				.role(Role.USER).email("vohoangan2000@gmail.com")
//				.password(passwordEncoder.encode("123")).build();
//		userRepository.save(user);

//		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//				user,
//				null,
//				user.getAuthorities()
//		);
//		LOGGER.error(authenticationToken.toString());
//		MockHttpServletRequest request = new MockHttpServletRequest();
//		request.setServerName("www.localhost.com");
//		request.setRequestURI("api/v1/auth/registration");
//		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}
}
