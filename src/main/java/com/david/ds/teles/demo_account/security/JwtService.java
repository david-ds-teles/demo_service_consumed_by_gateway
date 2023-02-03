package com.david.ds.teles.demo_account.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

	@Value("${spring.security.secretKey}")
	private String secretKey;

	public String generateToken(String userName, String ...roles) {

		final List<String> convertedRoles = Arrays.stream(roles).map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role).toList();
		final Date expireAt = Date.from(LocalDateTime.now().plus(1, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toInstant());
		final String token = Jwts.builder()
				.setSubject(userName)
				.setExpiration(expireAt)
				.claim("roles", convertedRoles)
				.signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encode(secretKey.getBytes()))
				.compact();

		return token;
	}

	public UserDetails isTokenValid(String token) {
		if (!StringUtils.hasText(token))
			return null;

		Claims claims = Jwts.parser()
				.setSigningKey(Base64.getEncoder().encode(secretKey.getBytes()))
				.parseClaimsJws(token)
				.getBody();

		final LocalDateTime expiration = claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		if (expiration.isBefore(LocalDateTime.now()))
			return null;

		final String userName = claims.getSubject();
		final List<String> roles = claims.get("roles", ArrayList.class);

		return User.builder()
				.username(userName)
				.password(token)
				.authorities(roles.stream().reduce((a,b) -> a + "," + b).get())
				.build();
	}
}
