package com.robotlab.Shortify.Repository;

import com.robotlab.Shortify.Entity.UrlDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlDetailRepository extends JpaRepository<UrlDetail, String> {

}
