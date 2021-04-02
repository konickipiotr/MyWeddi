package com.myweddi.db;

import com.myweddi.model.Comment;
import com.myweddi.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostidOrderByCreationdateAsc(long userid);
    List<Comment> findAllByOrderByCreationdateDesc(Pageable pageable);
    void deleteByPostid(Long postid);
}
