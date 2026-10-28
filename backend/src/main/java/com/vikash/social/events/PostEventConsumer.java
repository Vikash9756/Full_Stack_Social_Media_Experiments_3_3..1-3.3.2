package com.vikash.social.events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PostEventConsumer {
  @KafkaListener(topics="scheduled-posts", groupId="social-post-workers")
  public void receive(String message) {
    // Demo consumer only. Connect a platform-specific publisher here after validating due time and credentials.
    System.out.println("Received scheduled-post event: "+message);
  }
}
