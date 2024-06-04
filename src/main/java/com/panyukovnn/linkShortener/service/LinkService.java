package com.panyukovnn.linkShortener.service;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import org.apache.commons.lang3.RandomStringUtils;

public class LinkService {

    public String generateShortLink(CreateShortLinkRequest createShortLinkRequest) {
        return RandomStringUtils.randomAlphanumeric(8);
    }
}
