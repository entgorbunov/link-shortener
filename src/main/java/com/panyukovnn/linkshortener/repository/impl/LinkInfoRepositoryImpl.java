package com.panyukovnn.linkshortener.repository.impl;

import com.panyukovnn.linkshortener.model.LinkInfo;
import com.panyukovnn.linkshortener.repository.LinkInfoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LinkInfoRepositoryImpl implements LinkInfoRepository {

    private final Map<String, LinkInfo> linkStorage = new ConcurrentHashMap<>();

    @Override
    public Optional<LinkInfo> findByShortLink(String shortLink) {
        return Optional.ofNullable(linkStorage.get(shortLink));
    }

    @Override
    public LinkInfo save(LinkInfo linkInfo) {
        linkInfo.setId(UUID.randomUUID());
        linkStorage.put(linkInfo.getShortLink(), linkInfo);
        return linkInfo;
    }

    @Override
    public List<LinkInfo> findAll() {
        return new ArrayList<>(linkStorage.values());
    }

    @Override
    public void deleteById(UUID id) {
        linkStorage.values().removeIf(linkInfo -> linkInfo.getId().equals(id));
    }
}
