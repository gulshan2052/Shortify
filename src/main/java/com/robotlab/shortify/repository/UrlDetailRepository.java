package com.robotlab.shortify.repository;

import com.robotlab.shortify.entity.UrlDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface UrlDetailRepository extends JpaRepository<UrlDetail, String> {

    @Modifying
    @Transactional
    @Query("DELETE FROM UrlDetail e WHERE e.expirationDate IS NOT NULL AND e.expirationDate <= :currentDateTime")
    int deleteExpiredEntries(@Param("currentDateTime") OffsetDateTime currentDateTime);

}
