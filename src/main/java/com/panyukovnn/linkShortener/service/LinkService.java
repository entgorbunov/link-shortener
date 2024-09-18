package com.panyukovnn.linkShortener.service;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

public class LinkService {

    public static final int COUNT = 8;
    private final Map<String, CreateShortLinkRequest> linkStorage = new HashMap<>();

    public String generateShortLink(CreateShortLinkRequest createShortLinkRequest) {
        String shortLink = RandomStringUtils.randomAlphanumeric(COUNT);

        linkStorage.put(shortLink, createShortLinkRequest);

        return shortLink;
    }

    public CreateShortLinkRequest getLink(String shortLink) {
        return linkStorage.get(shortLink);
    }


}
