package com.myweddi.db;

import com.myweddi.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByPostid(Long postid);
    void deleteByPostid(Long postid);
    List<Photo> findByUserid(Long userid);
}
