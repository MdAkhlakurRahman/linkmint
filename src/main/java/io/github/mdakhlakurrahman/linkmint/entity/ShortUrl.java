package io.github.mdakhlakurrahman.linkmint.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "short_urls")
@Entity
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,length = 2048)
    private String originalUrl;
    @Column(nullable = false,length = 20,unique = true)
    private String shortCode;
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
