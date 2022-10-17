package com.project.questapp.repos;

import com.project.questapp.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByUserIdAndPostId(Long userId, Long postId);

    List<Comment> findByUserId(Long userId);

    List<Comment> findByPostId(Long postId);

    // with query
    @Query(value = "select id from post order by create_date desc limit 5 where user_id = :userId",nativeQuery = true)
    List<Comment> findTopByUserId(@Param("userId") Long userId);
}
