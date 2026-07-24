package io.github.mdakhlakurrahman.linkmint.exception;

public class ShortUrlExpiredException extends RuntimeException {
    public ShortUrlExpiredException(String message) {
        super(message);
    }
}
