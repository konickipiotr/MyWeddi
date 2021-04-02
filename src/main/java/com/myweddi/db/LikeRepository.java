package com.myweddi.db;

import com.myweddi.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByPostidAndUserid(Long postid, Long userid);
    void deleteByPostidAndUserid(Long postid, Long userid);
    List<Like> findAllByPostid(Long postid);
}
