package io.github.mdakhlakurrahman.linkmint.controller;

import io.github.mdakhlakurrahman.linkmint.entity.ShortUrl;
import io.github.mdakhlakurrahman.linkmint.service.ShortUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
public class RedirectController {

    private final ShortUrlService shortUrlService;

    public RedirectController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        log.info("Redirect requested for short code: {}",shortCode)
        ShortUrl link = shortUrlService.findByShortCode(shortCode);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(link.getOriginalUrl()))
                .build();
    }
}
