package com.vikash.social.post;

import com.vikash.social.events.PostEventProducer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {
  private final PostRepository posts; private final PostEventProducer events;
  public PostController(PostRepository posts,PostEventProducer events){this.posts=posts;this.events=events;}
  public record PostRequest(@NotBlank String content, Instant scheduledAt) {}
  @GetMapping public List<Post> list(Authentication auth){return posts.findByCreatedByOrderByIdDesc(auth.getName());}
  @PostMapping public ResponseEntity<?> create(@Valid @RequestBody PostRequest request,Authentication auth){
    String status=request.scheduledAt()==null?"DRAFT":"SCHEDULED";
    Post post=posts.save(new Post(request.content(),status,request.scheduledAt(),auth.getName()));
    if(request.scheduledAt()!=null) events.send("{\"postId\":"+post.getId()+",\"scheduledAt\":\""+request.scheduledAt()+"\"}");
    return ResponseEntity.status(HttpStatus.CREATED).body(post);
  }
  @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable Long id,Authentication auth){
    Optional<Post> found=posts.findById(id);
    if(found.isEmpty() || !found.get().getCreatedBy().equals(auth.getName())) return ResponseEntity.notFound().build();
    posts.deleteById(id);return ResponseEntity.noContent().build();
  }
}
