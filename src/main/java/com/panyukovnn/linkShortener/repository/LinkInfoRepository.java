package com.panyukovnn.linkShortener.repository;

import com.panyukovnn.linkShortener.model.LinkInfo;

import java.util.List;

public interface LinkInfoRepository {

    LinkInfo findByShortLink(String shortLink);

    LinkInfo save(LinkInfo linkInfo);

    List<LinkInfo> findAll();
}
