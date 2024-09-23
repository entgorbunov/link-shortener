package com.panyukovnn.linkShortener;

import com.panyukov.LoggingConfiguration;
import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkShortener.model.LinkInfo;
import com.panyukovnn.linkShortener.repository.LinkInfoRepository;
import com.panyukovnn.linkShortener.repository.impl.LinkInfoRepositoryImpl;
import com.panyukovnn.linkShortener.service.LinkInfoService;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        LinkInfoRepositoryImpl repository = new LinkInfoRepositoryImpl();
        LinkInfoService linkInfoService = new LinkInfoService(repository);
        LinkInfo gitHub = linkInfoService.createLinkInfo(new CreateShortLinkRequest(
                "https://github.com/entgorbunov/link-shortener",
                LocalDateTime.now().plusDays(7),
                "GitHub",
                true
        ));


        String shortLink = gitHub.getShortLink();
        LinkInfo byShortLink = linkInfoService.getByShortLink(shortLink);

        LoggingConfiguration.testLog("Testlog");

        System.out.println(shortLink);
        System.out.println(byShortLink.getLink());
        LinkInfo errorShortLink = linkInfoService.getByShortLink("shortLink");

        System.out.println(errorShortLink);

    }
}

