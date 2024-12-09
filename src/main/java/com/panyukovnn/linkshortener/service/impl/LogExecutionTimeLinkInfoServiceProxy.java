package com.panyukovnn.linkshortener.service.impl;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.UpdateShortLinkRequest;
import com.panyukovnn.linkshortener.model.LinkInfoResponse;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class LogExecutionTimeLinkInfoServiceProxy implements LinkInfoService {

	private final LinkInfoService linkInfoService;

	public LogExecutionTimeLinkInfoServiceProxy(LinkInfoService linkInfoService) {
		this.linkInfoService = linkInfoService;
	}

	@Override
	public Optional<LinkInfoResponse> getByShortLink(String shortLink) {
		long startTime = System.currentTimeMillis();
		try {
			return linkInfoService.getByShortLink(shortLink);
		} finally {
			long executionTime = System.currentTimeMillis() - startTime;
			log.info("Метод getByShortLink выполнился за {} мс", executionTime);
		}
	}

	@Override
	public List<LinkInfoResponse> findByFilter() {
		long startTime = System.currentTimeMillis();
		try {
			return linkInfoService.findByFilter();
		} finally {
			long executionTime = System.currentTimeMillis() - startTime;
			log.info("Метод findByFilter выполнился за {} мс", executionTime);
		}
	}

	@Override
	public LinkInfoResponse createLinkInfo(CreateShortLinkRequest request) {
		long startTime = System.currentTimeMillis();
		try {
			return linkInfoService.createLinkInfo(request);
		} finally {
			long executionTime = System.currentTimeMillis() - startTime;
			log.info("Метод createLinkInfo выполнился за {} мс", executionTime);
		}
	}

	@Override
	public LinkInfoResponse updateLinkInfo(UpdateShortLinkRequest request) {
		long startTime = System.currentTimeMillis();
		try {
			return linkInfoService.updateLinkInfo(request);
		} finally {
			long executionTime = System.currentTimeMillis() - startTime;
			log.info("Метод updateLinkInfo выполнился за {} мс", executionTime);
		}
	}

	@Override
	public void deleteById(UUID id) {
		long startTime = System.currentTimeMillis();
		try {
			linkInfoService.deleteById(id);
		} finally {
			long executionTime = System.currentTimeMillis() - startTime;
			log.info("Метод deleteById выполнился за {} мс", executionTime);
		}
	}
}
