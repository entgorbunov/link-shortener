//package com.panyukovnn.linkShortener;
//
//import com.panyukov.LoggingConfiguration;
//import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
//import com.panyukovnn.linkShortener.model.LinkInfo;
//import com.panyukovnn.linkShortener.repository.impl.LinkInfoRepositoryImpl;
//import com.panyukovnn.linkShortener.service.LinkInfoServiceImpl;
//
//import java.time.LocalDateTime;
//
//public class Main {
//    public static void main(String[] args) {
//        LinkInfoRepositoryImpl repository = new LinkInfoRepositoryImpl();
//        LinkInfoServiceImpl linkInfoServiceImpl = new LinkInfoServiceImpl(repository);
//        LinkInfo gitHub = linkInfoServiceImpl.createLinkInfo(new CreateShortLinkRequest(
//                "https://github.com/entgorbunov/link-shortener",
//                LocalDateTime.now().plusDays(7),
//                "GitHub",
//                true
//        ));
//
//
//        String shortLink = gitHub.getShortLink();
//        LinkInfo byShortLink = linkInfoServiceImpl.getByShortLink(shortLink);
//
//        LoggingConfiguration.testLog("Testlog");
//
//        System.out.println(shortLink);
//        System.out.println(byShortLink.getLink());
//        LinkInfo errorShortLink = linkInfoServiceImpl.getByShortLink("shortLink");
//
//        System.out.println(errorShortLink);
//
//    }
//}
//
