package io.github.mdakhlakurrahman.linkmint.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateShortUrlRequest {

    @URL(message = "Please provide a valid URL")
    @NotBlank(message = "Original URL is required")
    private String originalUrl;
}