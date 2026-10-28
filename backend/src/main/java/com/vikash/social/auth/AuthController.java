package com.vikash.social.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService auth;
  public AuthController(AuthService auth){this.auth=auth;}
  public record AuthRequest(@Email @NotBlank String email, @NotBlank @Size(min=8) String password) {}
  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody AuthRequest request) {
    try { return ResponseEntity.status(201).body(Map.of("token",auth.register(request.email(),request.password()))); }
    catch(IllegalArgumentException ex){return ResponseEntity.badRequest().body(Map.of("error",ex.getMessage()));}
  }
  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
    try { return ResponseEntity.ok(Map.of("token",auth.login(request.email(),request.password()))); }
    catch(IllegalArgumentException ex){return ResponseEntity.status(401).body(Map.of("error",ex.getMessage()));}
  }
}
