package com.panyukovnn.linkShortener.service.impl;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkShortener.exceptions.NotFoundException;
import com.panyukovnn.linkShortener.model.LinkInfo;
import com.panyukovnn.linkShortener.model.LinkInfoResponse;
import com.panyukovnn.linkShortener.repository.LinkInfoRepository;
import com.panyukovnn.linkShortener.service.LinkInfoService;
import com.panyukovnn.linkShortener.util.Constants;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.UUID;

public class LinkInfoServiceImpl implements LinkInfoService {

    private final LinkInfoRepository repository;

    public LinkInfoServiceImpl(LinkInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public LinkInfoResponse getByShortLink(String shortLink) {
        LinkInfo byShortLink = repository.findByShortLink(shortLink);
        if (byShortLink == null) {
            throw new NotFoundException("Link not found: " + shortLink);
        }
        return convertToResponse(byShortLink);
    }

    @Override
    public List<LinkInfoResponse> findByFilter() {
        return repository.findAll()
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

        LinkInfo savedLinkInfo = repository.save(linkInfo);

        return convertToResponse(savedLinkInfo);
    }

    protected static LinkInfoResponse convertToResponse(LinkInfo linkInfo) {
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
}