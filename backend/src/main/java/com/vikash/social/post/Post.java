package com.vikash.social.post;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Post {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false,length=5000) private String content;
  @Column(nullable=false) private String status;
  private Instant scheduledAt;
  @Column(nullable=false) private String createdBy;
  protected Post(){}
  public Post(String content,String status,Instant scheduledAt,String createdBy){
    this.content=content;this.status=status;this.scheduledAt=scheduledAt;this.createdBy=createdBy;
  }
  public Long getId(){return id;} public String getContent(){return content;}
  public String getStatus(){return status;} public Instant getScheduledAt(){return scheduledAt;}
  public String getCreatedBy(){return createdBy;}
  public void setStatus(String status){this.status=status;}
}
