package com.robotlab.Shortify.Repository;

import com.robotlab.Shortify.Entity.LengthConfig;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LengthConfigRepository extends JpaRepository<LengthConfig, String> {

    @Modifying
    @Transactional
    @Query("UPDATE LengthConfig ce SET ce.length = ce.length + 1 WHERE ce.id = :id")
    void incrementAliasLength(@Param("id") String id);

}
