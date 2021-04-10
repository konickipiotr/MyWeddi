package com.myweddi.db;

import com.myweddi.model.Post;
import com.myweddi.model.Posttype;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUseridOrderByCreationdateDesc(long userid);
    List<Post> findAllByOrderByCreationdateDesc(Pageable pageable);


    List<Post> findByWeddingidOrderByCreationdateDesc(Long hostweddingid, Pageable pageable);
    List<Post> findByPosttypeOrderByCreationdateDesc(Posttype posttype, Pageable pageable);
}
