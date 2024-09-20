package com.panyukovnn.linkShortener.service;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkShortener.exceptions.NotFoundException;
import com.panyukovnn.linkShortener.model.LinkInfo;
import com.panyukovnn.linkShortener.repository.LinkInfoRepository;
import com.panyukovnn.linkShortener.repository.impl.LinkInfoRepositoryImpl;
import com.panyukovnn.linkShortener.util.Constants;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class LinkInfoService {

    private final LinkInfoRepository repository;

    public LinkInfoService(LinkInfoRepository repository) {
        this.repository = repository;
    }

    public LinkInfo createLinkInfo(CreateShortLinkRequest request) {
        String shortLink = RandomStringUtils.randomAlphanumeric(Constants.COUNT);

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setId(UUID.randomUUID());
        linkInfo.setShortLink(shortLink);
        linkInfo.setLink(request.getLink());
        linkInfo.setOpeningCount(0L);
        linkInfo.setEndTime(request.getEndTime());
        linkInfo.setDescription(request.getDescription());
        linkInfo.setActive(true);

        return repository.save(linkInfo);
    }

    public LinkInfo getByShortLink(String shortLink) {
        return repository.findByShortLink(shortLink)
                .orElseThrow(() -> new NotFoundException("Короткая ссылка " + shortLink + " не найдена"));
    }



}
