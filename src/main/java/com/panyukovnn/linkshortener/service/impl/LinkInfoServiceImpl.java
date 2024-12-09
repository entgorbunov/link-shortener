package com.panyukovnn.linkshortener.service.impl;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.UpdateShortLinkRequest;
import com.panyukovnn.linkshortener.exceptions.NotFoundException;
import com.panyukovnn.linkshortener.model.LinkInfo;
import com.panyukovnn.linkshortener.model.LinkInfoResponse;
import com.panyukovnn.linkshortener.properties.LinkInfoProperty;
import com.panyukovnn.linkshortener.repository.LinkInfoRepository;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LinkInfoServiceImpl implements LinkInfoService {

	private final LinkInfoProperty linkInfoProperty;
	private final LinkInfoRepository linkInfoRepository;

	public LinkInfoServiceImpl(LinkInfoProperty linkInfoProperty, LinkInfoRepository linkInfoRepository) {
		this.linkInfoProperty = linkInfoProperty;
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
			.shortLink(RandomStringUtils.randomAlphanumeric(linkInfoProperty.getShortLinkLength()))
			.openingCount(0L)
			.build();

		LinkInfo savedLinkInfo = linkInfoRepository.save(linkInfo);

		return convertToResponse(savedLinkInfo);
	}

	@Override
	public LinkInfoResponse updateLinkInfo(UpdateShortLinkRequest request) {
		if (request.getId() == null) {
			throw new IllegalArgumentException("ID должен присутствовать");
		}
		LinkInfo linkInfo = linkInfoRepository.findByShortLink(request.getShortLink());
		if (linkInfo == null) {
			throw new NotFoundException("Ссылка не найдена, id:" + request.getId());
		}
		if (request.getDescription() != null) {
			linkInfo.setDescription(request.getDescription());
		}
		if (request.getActive() != null) {
			linkInfo.setActive(request.getActive());
		}
		if (request.getEndTime() != null) {
			linkInfo.setEndTime(request.getEndTime());
		}
		LinkInfo updatedLinkInfo = linkInfoRepository.save(linkInfo);
		return convertToResponse(updatedLinkInfo);
	}

	@Override
	public void deleteById(UUID id) {
		linkInfoRepository.deleteById(id);
	}


}
