package com.panyukovnn.linkshortener.repository;

import com.panyukovnn.linkshortener.model.LinkInfo;

import java.util.List;

public interface LinkInfoRepository {

    LinkInfo findByShortLink(String shortLink);

    LinkInfo save(LinkInfo linkInfo);

    List<LinkInfo> findAll();
}
