package com.david.ds.teles.demo_account;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.david.ds.teles.demo_account.security.JwtService;
import com.david.ds.teles.demo_account.security.Login;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class AccountController {

	private final JwtService jwtService;

	@GetMapping("/hello")
	public String hello() {
		return "Hello from Account Service";
	}

	@PostMapping("/login")
	public String login(@RequestBody Login login) {
		final String token = jwtService.generateToken(login.username(), "USER");
		return token;
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping
	public UserDetails account() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return userDetails;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin")
	public String admin() {
		return "only for those with ADMIN role";
	}
}
