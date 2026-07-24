package io.github.mdakhlakurrahman.linkmint.service;

import io.github.mdakhlakurrahman.linkmint.dto.request.CreateShortUrlRequest;
import io.github.mdakhlakurrahman.linkmint.dto.response.ShortUrlResponse;
import io.github.mdakhlakurrahman.linkmint.entity.ShortUrl;
import io.github.mdakhlakurrahman.linkmint.exception.ShortUrlExpiredException;
import io.github.mdakhlakurrahman.linkmint.exception.ShortUrlNotFoundException;
import io.github.mdakhlakurrahman.linkmint.generator.ShortCodeGenerator;
import io.github.mdakhlakurrahman.linkmint.mapper.ShortUrlMapper;
import io.github.mdakhlakurrahman.linkmint.repository.ShortUrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final ShortUrlMapper shortUrlMapper;
    private final RedisTemplate<String, ShortUrl> redisTemplate;

    public ShortUrlService(ShortUrlRepository shortUrlRepository,
                           ShortCodeGenerator shortCodeGenerator,
                           ShortUrlMapper shortUrlMapper,
                           RedisTemplate<String, ShortUrl> redisTemplate) {

        this.shortUrlRepository = shortUrlRepository;
        this.shortCodeGenerator = shortCodeGenerator;
        this.shortUrlMapper = shortUrlMapper;
        this.redisTemplate = redisTemplate;
    }


    public ShortUrlResponse createShortUrl(CreateShortUrlRequest request) {
        String shortCode = generateUniqueShortCode();
        log.info("Generated short code: {}", shortCode);
        ShortUrl shortUrl = shortUrlMapper.toEntity(
                request,
                shortCode,
                LocalDateTime.now()
        );

        ShortUrl saved = shortUrlRepository.save(shortUrl);
        log.info("Successfully created short URL with code: {}", shortCode);
        return shortUrlMapper.toResponse(saved);
    }

    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = shortCodeGenerator.generate();
        } while (shortUrlRepository.existsByShortCode(shortCode));
        return shortCode;
    }

    public ShortUrl findByShortCode(String shortCode) {
        String cacheKey = "shortUrl:" + shortCode;

        // 1. Check Redis
        ShortUrl cached = redisTemplate.opsForValue().get(cacheKey);

        // 2. Cache Hit
        if (cached != null) {
            log.info("Cache HIT for short code: {}", shortCode);
            return cached;
        }

        // 3. Cache Miss
        log.info("Cache MISS for short code: {}", shortCode);

        // 4. Query Database
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> {
                    log.warn("Short code {} not found", shortCode);
                    return new ShortUrlNotFoundException("Short URL not found: " + shortCode);
                });

        // 5. Store in Redis for 10 minutes
        redisTemplate.opsForValue().set(cacheKey, shortUrl, Duration.ofMinutes(10));
        log.info("Stored short code {} in Redis cache", shortCode);

        if (shortUrl.getExpiresAt() != null && shortUrl.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ShortUrlExpiredException("Short URL has expired: " + shortCode);
        }

        // 6. Return Response
        return shortUrl;

    }


    public void deleteByShortCode(String shortCode) {
        String cacheKey = "shortUrl:" + shortCode;
        ShortUrl shortUrl=shortUrlRepository.findByShortCode(shortCode).orElseThrow(() -> {
            log.warn("Short code {} does not exists", shortCode);
            return new ShortUrlNotFoundException("Short URL not found: " + shortCode);
        });

        // Delete from PostgreSQL
        shortUrlRepository.delete(shortUrl);
        log.info("Deleted short URL {} from database", shortCode);
        // Delete from Redis
        redisTemplate.delete(cacheKey);
        log.info("Removed short URL {} from Redis cache", shortCode);
    }

    @Transactional
    public void incrementClickCount(String shortCode) {
        int updated = shortUrlRepository.incrementClickCount(shortCode);
        if (updated == 0) {
            throw new ShortUrlNotFoundException("Short URL not found: " + shortCode);
        }
        log.info("Incremented click count for {}", shortCode);
    }

}