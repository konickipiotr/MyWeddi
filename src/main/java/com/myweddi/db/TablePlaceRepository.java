package com.myweddi.db;

import com.myweddi.model.TablePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TablePlaceRepository extends JpaRepository<TablePlace, Long> {

    boolean existsByWeddingid(Long aLong);
    List<TablePlace> findByWeddingid(Long weddingid);
    void deleteByWeddingid(Long weddingid);
}
