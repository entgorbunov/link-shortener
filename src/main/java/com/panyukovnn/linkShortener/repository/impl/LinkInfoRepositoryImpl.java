package com.panyukovnn.linkShortener.repository.impl;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkShortener.model.LinkInfo;
import com.panyukovnn.linkShortener.repository.LinkInfoRepository;
import com.panyukovnn.linkShortener.util.Constants;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LinkInfoRepositoryImpl implements LinkInfoRepository {

    private final ConcurrentHashMap<String, LinkInfo> linkInfoMap = new ConcurrentHashMap<>();

    @Override
    public Optional<LinkInfo> findByShortLink(String shortLink) {
        return Optional.ofNullable(linkInfoMap.get(shortLink));
    }

    @Override
    public LinkInfo save(LinkInfo linkInfo) {
        linkInfo.setId(UUID.randomUUID());
        linkInfo.setOpeningCount(0L);
        linkInfoMap.put(linkInfo.getShortLink(), linkInfo);
        return linkInfo;
    }
}
