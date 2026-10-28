package com.vikash.social.post;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
  List<Post> findByCreatedByOrderByIdDesc(String createdBy);
}
