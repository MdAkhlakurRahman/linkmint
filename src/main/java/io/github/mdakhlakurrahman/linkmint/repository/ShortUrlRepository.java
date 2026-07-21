package io.github.mdakhlakurrahman.linkmint.repository;

import io.github.mdakhlakurrahman.linkmint.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    boolean existsByShortCode(String shortCode);

    Optional<ShortUrl> findByShortCode(String shortcode);

}