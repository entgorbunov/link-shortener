package com.panyukovnn.linkShortener.service;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

public class LinkService {

    private final Map<String, CreateShortLinkRequest> linkStorage = new HashMap<>();

    public String generateShortLink(CreateShortLinkRequest createShortLinkRequest) {
        String shortLink = RandomStringUtils.randomAlphanumeric(8);

        linkStorage.put(shortLink, createShortLinkRequest);

        return shortLink;
    }

    public CreateShortLinkRequest getLink(String shortLink) {
        return linkStorage.get(shortLink);
    }


}
