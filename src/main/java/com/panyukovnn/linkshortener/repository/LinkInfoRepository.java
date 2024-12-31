package com.panyukovnn.linkshortener.repository;

import com.panyukovnn.linkshortener.model.LinkInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LinkInfoRepository {

    Optional<LinkInfo> findByShortLinkAndActiveIsTrueAndEndTimeAfterOrEndTimeIsNull(String shortLink, LocalDateTime now);

    Optional<LinkInfo> findByShortLink(String shortLink);

    LinkInfo save(LinkInfo linkInfo);

    List<LinkInfo> findAll();

    void deleteById(UUID id);
}
