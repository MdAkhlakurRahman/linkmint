package io.github.mdakhlakurrahman.linkmint.exception;

public class ShortUrlNotFoundException extends RuntimeException{
        public ShortUrlNotFoundException(String message) {
            super(message);
        }
}