package io.github.mdakhlakurrahman.linkmint.controller;

import io.github.mdakhlakurrahman.linkmint.dto.request.CreateShortUrlRequest;
import io.github.mdakhlakurrahman.linkmint.dto.response.ShortUrlResponse;
import io.github.mdakhlakurrahman.linkmint.service.ShortUrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/short-urls")
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @PostMapping
    public ResponseEntity<ShortUrlResponse> createShortUrl(@Valid @RequestBody CreateShortUrlRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shortUrlService.createShortUrl(request));
    }

}
