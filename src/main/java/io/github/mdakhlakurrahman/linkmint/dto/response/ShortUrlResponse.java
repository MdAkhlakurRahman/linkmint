package io.github.mdakhlakurrahman.linkmint.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlResponse {

    private String shortCode;
    private String originalUrl;
    private LocalDateTime createdAt;
}
