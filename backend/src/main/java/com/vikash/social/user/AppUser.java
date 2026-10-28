package com.vikash.social.user;

import jakarta.persistence.*;

@Entity
@Table(name="app_users")
public class AppUser {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false, unique=true) private String email;
  @Column(nullable=false) private String passwordHash;
  @Column(nullable=false) private String role = "VIEWER";
  protected AppUser() {}
  public AppUser(String email, String passwordHash, String role) {
    this.email=email; this.passwordHash=passwordHash; this.role=role;
  }
  public Long getId(){return id;}
  public String getEmail(){return email;}
  public String getPasswordHash(){return passwordHash;}
  public String getRole(){return role;}
}
