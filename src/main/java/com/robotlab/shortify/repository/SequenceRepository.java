package com.robotlab.shortify.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SequenceRepository {

    private final JdbcTemplate jdbcTemplate;

    public SequenceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long nextCounterValue() {
        Long val = jdbcTemplate.queryForObject(
                "SELECT NEXT VALUE FOR url_counter_seq",
                Long.class
        );
        if (val == null) {
            throw new IllegalStateException("Sequence returned null");
        }
        return val;
    }

}
