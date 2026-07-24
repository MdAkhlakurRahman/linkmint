package io.github.mdakhlakurrahman.linkmint.repository;

import io.github.mdakhlakurrahman.linkmint.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    boolean existsByShortCode(String shortCode);

    Optional<ShortUrl> findByShortCode(String shortcode);

    @Transactional
    @Modifying
    @Query("""
            UPDATE ShortUrl s
            SET s.clickCount = s.clickCount + 1
            WHERE s.shortCode = :shortCode
            """)
    int incrementClickCount(@Param("shortCode") String shortCode);

}