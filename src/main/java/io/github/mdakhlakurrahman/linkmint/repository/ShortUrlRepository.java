package io.github.mdakhlakurrahman.linkmint.repository;

import io.github.mdakhlakurrahman.linkmint.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    boolean existsByShortCode(String shortCode);

}