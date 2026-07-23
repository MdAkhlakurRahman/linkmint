package io.github.mdakhlakurrahman.linkmint.service;

import io.github.mdakhlakurrahman.linkmint.dto.request.CreateShortUrlRequest;
import io.github.mdakhlakurrahman.linkmint.dto.response.ShortUrlResponse;
import io.github.mdakhlakurrahman.linkmint.entity.ShortUrl;
import io.github.mdakhlakurrahman.linkmint.exception.ShortUrlNotFoundException;
import io.github.mdakhlakurrahman.linkmint.generator.ShortCodeGenerator;
import io.github.mdakhlakurrahman.linkmint.mapper.ShortUrlMapper;
import io.github.mdakhlakurrahman.linkmint.repository.ShortUrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final ShortUrlMapper shortUrlMapper;

    public ShortUrlService(ShortUrlRepository shortUrlRepository,
                           ShortCodeGenerator shortCodeGenerator,
                           ShortUrlMapper shortUrlMapper) {
        this.shortUrlRepository = shortUrlRepository;
        this.shortCodeGenerator = shortCodeGenerator;
        this.shortUrlMapper = shortUrlMapper;
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
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> {
                    log.warn("Short code {} not found", shortCode);
                    return new ShortUrlNotFoundException("Short URL not found: " + shortCode);
                });
        log.info("Found original URL for short code: {}", shortCode);
        return shortUrl;
    }
}