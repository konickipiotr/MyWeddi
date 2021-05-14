package com.myweddi.db;

import com.myweddi.model.WeddiLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<WeddiLike, Long> {
    boolean existsByPostidAndUserid(Long postid, Long userid);
    void deleteByPostidAndUserid(Long postid, Long userid);
    List<WeddiLike> findByPostid(Long postid);

    boolean existsByUserid(Long userid);
    void deleteByUserid(Long userid);
}
