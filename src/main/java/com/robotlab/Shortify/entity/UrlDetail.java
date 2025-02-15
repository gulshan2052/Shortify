package com.robotlab.Shortify.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity(name = "UrlDetail")
@Table(name = "url_detail")
public class UrlDetail {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "long_url")
    private String longUrl;

    @Column(name = "created_date")
    private OffsetDateTime createdDate;

    @Column(name = "ttl_minutes")
    private Integer ttlMinutes;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

}
