package com.vikash.social.analytics;

import com.vikash.social.post.PostRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
  private final PostRepository posts;
  public AnalyticsController(PostRepository posts){this.posts=posts;}
  @GetMapping("/summary")
  public Map<String,Object> summary(Authentication auth){
    var all=posts.findByCreatedByOrderByIdDesc(auth.getName());
    var byStatus=all.stream().collect(Collectors.groupingBy(p->p.getStatus(),Collectors.counting()));
    return Map.of("totalPosts",all.size(),"byStatus",byStatus);
  }
}
