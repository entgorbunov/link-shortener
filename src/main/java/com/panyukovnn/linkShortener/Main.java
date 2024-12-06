package com.panyukovnn.linkShortener;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkShortener.model.LinkInfoResponse;
import com.panyukovnn.linkShortener.repository.LinkInfoRepository;
import com.panyukovnn.linkShortener.repository.impl.LinkInfoRepositoryImpl;
import com.panyukovnn.linkShortener.service.LinkInfoService;
import com.panyukovnn.linkShortener.service.impl.LinkInfoServiceImpl;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        LinkInfoRepository repository = new LinkInfoRepositoryImpl();
        LinkInfoService linkService = new LinkInfoServiceImpl(repository);
        LinkInfoResponse linkInfo = linkService.createLinkInfo(new CreateShortLinkRequest(
                "https://github.com/entgorbunov/link-shortener",
                LocalDateTime.now().plusDays(7),
                "GitHub",
                true
        ));
        System.out.println(linkInfo.getShortLink());
    }
}
