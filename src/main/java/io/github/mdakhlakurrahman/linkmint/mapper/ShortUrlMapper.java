package io.github.mdakhlakurrahman.linkmint.mapper;

import io.github.mdakhlakurrahman.linkmint.dto.request.CreateShortUrlRequest;
import io.github.mdakhlakurrahman.linkmint.dto.response.ShortUrlResponse;
import io.github.mdakhlakurrahman.linkmint.entity.ShortUrl;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ShortUrlMapper {

    public ShortUrl toEntity(CreateShortUrlRequest request, String shortCode,
                             LocalDateTime createdAt) {
        return ShortUrl.builder()
                .originalUrl(request.getOriginalUrl())
                .expiresAt(request.getExpiresAt())
                .shortCode(shortCode)
                .createdAt(createdAt)
                .build();
    }

    public ShortUrlResponse toResponse(ShortUrl entity) {
        return ShortUrlResponse.builder()
                .shortCode(entity.getShortCode())
                .originalUrl(entity.getOriginalUrl())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
