package com.panyukovnn.linkShortener.repository;

import com.panyukovnn.linkShortener.model.LinkInfo;

import java.util.Optional;

public interface LinkInfoRepository {

    public Optional<LinkInfo> findByShortLink(String shortLink);

    public LinkInfo save(LinkInfo linkInfo);
}
