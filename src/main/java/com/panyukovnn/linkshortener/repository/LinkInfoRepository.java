package com.panyukovnn.linkshortener.repository;

import com.panyukovnn.linkshortener.model.LinkInfo;

import java.util.List;
import java.util.UUID;

public interface LinkInfoRepository {

    LinkInfo findByShortLink(String shortLink);

    LinkInfo save(LinkInfo linkInfo);

    List<LinkInfo> findAll();

    void deleteById(UUID id);
}
