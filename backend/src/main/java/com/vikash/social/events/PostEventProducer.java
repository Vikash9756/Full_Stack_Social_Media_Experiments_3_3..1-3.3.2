package com.vikash.social.events;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PostEventProducer {
  private final KafkaTemplate<String,String> kafka;
  public PostEventProducer(KafkaTemplate<String,String> kafka){this.kafka=kafka;}
  public void send(String message) {
    try { kafka.send("scheduled-posts", message); }
    catch(Exception ex) { System.err.println("Kafka event could not be sent: "+ex.getMessage()); }
  }
}
