package com.robotlab.Shortify.repository;

import com.robotlab.Shortify.entity.UrlDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlDetailRepository extends JpaRepository<UrlDetail, String> {

    Optional<UrlDetail> findUrlDetailById(String id);

}
