package com.robotlab.shortify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "UrlDetail")
@Table(name = "url_detail")
public class UrlDetail {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "long_url", length = 4096)
    private String longUrl;

    @Column(name = "expiration_date")
    private OffsetDateTime expirationDate;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

}
