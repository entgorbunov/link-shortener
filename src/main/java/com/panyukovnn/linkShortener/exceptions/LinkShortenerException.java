package com.panyukovnn.linkShortener.exceptions;

public class LinkShortenerException extends RuntimeException {

    public LinkShortenerException(String message) {
        super(message);
    }

    public LinkShortenerException(String message, Exception e) {
        super(message, e);
    }
}