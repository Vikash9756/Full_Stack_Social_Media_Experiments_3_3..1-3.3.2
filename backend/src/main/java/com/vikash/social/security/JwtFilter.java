package com.vikash.social.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
  private final byte[] secret;
  public JwtFilter(@Value("${app.jwt.secret}") String secret){this.secret=secret.getBytes(StandardCharsets.UTF_8);}
  @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
    String header=req.getHeader("Authorization");
    if(header!=null && header.startsWith("Bearer ")) {
      try {
        Claims claims=Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret)).build()
          .parseSignedClaims(header.substring(7)).getPayload();
        String role=claims.get("role",String.class);
        var auth=new UsernamePasswordAuthenticationToken(claims.getSubject(),null,
          List.of(new SimpleGrantedAuthority("ROLE_"+role)));
        SecurityContextHolder.getContext().setAuthentication(auth);
      } catch(JwtException | IllegalArgumentException ignored) {
        SecurityContextHolder.clearContext();
      }
    }
    chain.doFilter(req,res);
  }
}
