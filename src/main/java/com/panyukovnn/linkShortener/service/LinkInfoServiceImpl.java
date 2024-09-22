package com.panyukovnn.linkShortener.service;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkShortener.exceptions.NotFoundException;
import com.panyukovnn.linkShortener.model.LinkInfo;
import com.panyukovnn.linkShortener.repository.LinkInfoRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LinkInfoServiceImpl implements LinkInfoService{

    @Value("${link-shortener.short-link-length}")
    private int shortLinkLength;
    private final LinkInfoRepository repository;

    public LinkInfoServiceImpl(LinkInfoRepository repository) {
        this.repository = repository;
    }

    public LinkInfo createLinkInfo(CreateShortLinkRequest request) {
        String shortLink = RandomStringUtils.randomAlphanumeric(shortLinkLength);

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
