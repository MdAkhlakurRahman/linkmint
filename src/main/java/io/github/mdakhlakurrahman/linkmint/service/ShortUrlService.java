package io.github.mdakhlakurrahman.linkmint.service;

import io.github.mdakhlakurrahman.linkmint.dto.request.CreateShortUrlRequest;
import io.github.mdakhlakurrahman.linkmint.dto.response.ShortUrlResponse;
import io.github.mdakhlakurrahman.linkmint.entity.ShortUrl;
import io.github.mdakhlakurrahman.linkmint.generator.ShortCodeGenerator;
import io.github.mdakhlakurrahman.linkmint.mapper.ShortUrlMapper;
import io.github.mdakhlakurrahman.linkmint.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        ShortUrl shortUrl = shortUrlMapper.toEntity(
                request,
                shortCode,
                LocalDateTime.now()
        );

        ShortUrl saved = shortUrlRepository.save(shortUrl);
        return shortUrlMapper.toResponse(saved);
    }

    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = shortCodeGenerator.generate();
        } while (shortUrlRepository.existsByShortCode(shortCode));
        return shortCode;
    }
}