package com.panyukovnn.linkShortener;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkShortener.service.LinkService;

public class Main {
    public static void main(String[] args) {
        LinkService linkService = new LinkService();
        String s = linkService.generateShortLink(new CreateShortLinkRequest());
    }
}
