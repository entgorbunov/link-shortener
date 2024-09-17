package com.panyukovnn.linkShortener;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkShortener.service.LinkService;

import java.time.ZonedDateTime;

public class Main {
    public static void main(String[] args) {
        LinkService linkService = new LinkService();
        String s = linkService.generateShortLink(new CreateShortLinkRequest(
                "https://github.com/entgorbunov/link-shortener",
                ZonedDateTime.now().plusDays(7),
                "GitHub",
                true
        ));
        System.out.println(s);
    }
}
