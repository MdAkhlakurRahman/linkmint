package io.github.mdakhlakurrahman.linkmint.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateShortUrlRequest {

    private String originalUrl;
    private String shortUrl;
}