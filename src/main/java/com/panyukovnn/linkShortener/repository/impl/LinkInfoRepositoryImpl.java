package com.panyukovnn.linkShortener.repository.impl;

import com.panyukovnn.linkShortener.model.LinkInfo;
import com.panyukovnn.linkShortener.repository.LinkInfoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class LinkInfoRepositoryImpl implements LinkInfoRepository {

    private final ConcurrentMap<String, LinkInfo> linkInfoMap = new ConcurrentHashMap<>();

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
