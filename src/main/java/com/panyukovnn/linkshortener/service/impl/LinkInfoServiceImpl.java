package com.panyukovnn.linkshortener.service.impl;

import com.panyukovnn.linkshortener.beanpostprocessor.LogExecutionTime;
import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.UpdateShortLinkRequest;
import com.panyukovnn.linkshortener.exceptions.NotFoundException;
import com.panyukovnn.linkshortener.model.LinkInfo;
import com.panyukovnn.linkshortener.model.LinkInfoResponse;
import com.panyukovnn.linkshortener.properties.LinkInfoProperty;
import com.panyukovnn.linkshortener.repository.LinkInfoRepository;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class LinkInfoServiceImpl implements LinkInfoService {

    private LinkInfoProperty linkInfoProperty;
    private LinkInfoRepository linkInfoRepository;

    @Autowired
    public LinkInfoServiceImpl(LinkInfoProperty linkInfoProperty, LinkInfoRepository linkInfoRepository) {
        this.linkInfoProperty = linkInfoProperty;
        this.linkInfoRepository = linkInfoRepository;
    }

    public LinkInfoServiceImpl() {
    }

    @LogExecutionTime
    @Override
    public LinkInfoResponse getByShortLink(String shortLink) {
        LinkInfo linkInfo = linkInfoRepository.findByShortLink(shortLink)
            .orElseThrow(() -> new NotFoundException("Ссылка не найдена"));
        return convertToResponse(linkInfo);
    }

    @LogExecutionTime
    @Override
    public List<LinkInfoResponse> findByFilter() {
        return linkInfoRepository.findAll()
            .stream()
            .map(LinkInfoServiceImpl::convertToResponse)
            .toList();
    }

    @LogExecutionTime
    @Override
    public LinkInfoResponse createLinkInfo(CreateShortLinkRequest request) {
        LinkInfo linkInfo = LinkInfo.builder()
            .active(request.getActive())
            .link(request.getLink())
            .endTime(request.getEndTime())
            .description(request.getDescription())
            .id(UUID.randomUUID())
            .shortLink(RandomStringUtils.randomAlphanumeric(linkInfoProperty.shortLinkLength()))
            .openingCount(0L)
            .build();

        LinkInfo savedLinkInfo = linkInfoRepository.save(linkInfo);

        return convertToResponse(savedLinkInfo);
    }

    @LogExecutionTime
    @Override
    public LinkInfoResponse updateLinkInfo(UpdateShortLinkRequest request) {
        LinkInfo linkInfo = linkInfoRepository.findByShortLink(request.getLink())
            .orElseThrow(() -> new NotFoundException("Ссылка не найдена, id: " + request.getId()));
        if (request.getDescription() != null) {
            linkInfo.setDescription(request.getDescription());
        }
        if (request.getActive() != null) {
            linkInfo.setActive(request.getActive());
        }
        if (request.getLink() != null) {
            linkInfo.setLink(request.getLink());
        }
        linkInfo.setEndTime(request.getEndTime());
        LinkInfo updatedLinkInfo = linkInfoRepository.save(linkInfo);
        return convertToResponse(updatedLinkInfo);
    }

    @LogExecutionTime
    @Override
    public void deleteById(UUID id) {
        linkInfoRepository.deleteById(id);
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
}
