package com.vikash.social.auth;

import com.vikash.social.user.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class AuthService {
  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final byte[] secret;
  public AuthService(UserRepository users, PasswordEncoder encoder,
      @Value("${app.jwt.secret}") String secret) {
    this.users=users; this.encoder=encoder; this.secret=secret.getBytes(StandardCharsets.UTF_8);
  }
  public String register(String email, String password) {
    if(users.existsByEmail(email)) throw new IllegalArgumentException("Email already registered");
    AppUser user=users.save(new AppUser(email, encoder.encode(password), "EDITOR"));
    return token(user);
  }
  public String login(String email, String password) {
    AppUser user=users.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    if(!encoder.matches(password,user.getPasswordHash())) throw new IllegalArgumentException("Invalid email or password");
    return token(user);
  }
  private String token(AppUser user) {
    long now=System.currentTimeMillis();
    return Jwts.builder().subject(user.getEmail()).claim("role",user.getRole())
      .issuedAt(new Date(now)).expiration(new Date(now+86400000))
      .signWith(Keys.hmacShaKeyFor(secret)).compact();
  }
}
