package com.panyukovnn.linkshortener.repository.impl;

import com.panyukovnn.linkshortener.model.LinkInfo;
import com.panyukovnn.linkshortener.repository.LinkInfoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LinkInfoRepositoryImpl implements LinkInfoRepository {

	private final Map<String, LinkInfo> linkStorage = new ConcurrentHashMap<>();

	@Override
	public LinkInfo findByShortLink(String shortLink) {
		return linkStorage.get(shortLink);
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
}
