package com.panyukovnn.linkShortener.repository;

import com.panyukovnn.linkShortener.model.LinkInfo;

import java.util.Optional;

public interface LinkInfoRepository {
    Optional<LinkInfo> findByShortLink(String shortLink);
    LinkInfo save(LinkInfo linkInfo);
}
