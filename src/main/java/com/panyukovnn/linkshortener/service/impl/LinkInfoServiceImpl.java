package com.panyukovnn.linkshortener.service.impl;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.exceptions.NotFoundException;
import com.panyukovnn.linkshortener.model.LinkInfo;
import com.panyukovnn.linkshortener.model.LinkInfoResponse;
import com.panyukovnn.linkshortener.repository.LinkInfoRepository;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import com.panyukovnn.linkshortener.util.Constants;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LinkInfoServiceImpl implements LinkInfoService {

	private final LinkInfoRepository linkInfoRepository;

	public LinkInfoServiceImpl(LinkInfoRepository linkInfoRepository) {
		this.linkInfoRepository = linkInfoRepository;
	}

	private static LinkInfoResponse convertToResponse(LinkInfo linkInfo) {
		return LinkInfoResponse.builder()
			.link(linkInfo.getLink())
			.id(linkInfo.getId())
			.active(linkInfo.getActive())
			.description(linkInfo.getDescription())
			.endTime(linkInfo.getEndTime())
			.openingCount(linkInfo.getOpeningCount())
			.shortLink(linkInfo.getShortLink())
			.build();
	}

	@Override
	public Optional<LinkInfoResponse> getByShortLink(String shortLink) {
		LinkInfo linkInfo = linkInfoRepository.findByShortLink(shortLink);
		if (linkInfo == null) {
			throw new NotFoundException("Link not found: " + shortLink);
		}
		return Optional.of(convertToResponse(linkInfo));
	}

	@Override
	public List<LinkInfoResponse> findByFilter() {
		return linkInfoRepository.findAll()
			.stream()
			.map(LinkInfoServiceImpl::convertToResponse)
			.toList();
	}

	@Override
	public LinkInfoResponse createLinkInfo(CreateShortLinkRequest request) {
		LinkInfo linkInfo = LinkInfo.builder()
			.active(request.getActive())
			.link(request.getLink())
			.endTime(request.getEndTime())
			.description(request.getDescription())
			.id(UUID.randomUUID())
			.shortLink(RandomStringUtils.randomAlphanumeric(Constants.SHORT_LINK_LENGTH))
			.openingCount(0L)
			.build();

		LinkInfo savedLinkInfo = linkInfoRepository.save(linkInfo);

		return convertToResponse(savedLinkInfo);
	}
}
