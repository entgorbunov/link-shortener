package com.panyukovnn.linkShortener;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkShortener.model.LinkInfo;
import com.panyukovnn.linkShortener.service.LinkInfoService;

import java.time.ZonedDateTime;

public class Main {
    public static void main(String[] args) {
        LinkInfoService linkInfoService = new LinkInfoService();
        CreateShortLinkRequest request = new CreateShortLinkRequest();
        request.setLink("https://example.com/long-link");
        request.setEndTime(ZonedDateTime.now().plusDays(30));
        request.setDescription("Example description");
        request.setActive(true);

        LinkInfo createdLinkInfo = linkInfoService.createLinkInfo(request);
        String shortLink = createdLinkInfo.getShortLink();
        System.out.println(shortLink);

        String longLink = linkInfoService.getByShortLink(shortLink);
        if (request.getLink().equals(longLink)) {
            System.out.println("The retrieved long link matches the original link.");
        } else {
            System.out.println("The retrieved long link does not match the original link.");
        }

    }

}
