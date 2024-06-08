package com.panyukovnn.linkShortener.service;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkShortener.model.LinkInfo;
import com.panyukovnn.linkShortener.repository.LinkInfoRepository;
import com.panyukovnn.linkShortener.repository.impl.LinkInfoRepositoryImpl;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Optional;

public class LinkInfoService {
    private final LinkInfoRepository linkInfoRepository = new LinkInfoRepositoryImpl();

    public LinkInfo createLinkInfo(CreateShortLinkRequest request) {
        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setLink(request.getLink());
        linkInfo.setEndTime(request.getEndTime());
        linkInfo.setDescription(request.getDescription());
        linkInfo.setActive(request.getActive());

        String shortLink = generateShortLink();
        linkInfo.setShortLink(shortLink);
        linkInfo.setOpeningCount(0L);
        return linkInfoRepository.save(linkInfo);
    }

    private String generateShortLink() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    public String getByShortLink(String  shortLink) {
        Optional<LinkInfo> optionalLinkInfo = linkInfoRepository.findByShortLink(shortLink);
        return optionalLinkInfo.map(LinkInfo::getLink).orElse(null);
    }
}
