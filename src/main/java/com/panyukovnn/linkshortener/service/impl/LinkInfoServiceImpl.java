package com.panyukovnn.linkshortener.service.impl;

import com.panyukovnn.linkshortener.beanpostprocessor.LogExecutionTime;
import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.LinkInfoResponse;
import com.panyukovnn.linkshortener.dto.UpdateShortLinkRequest;
import com.panyukovnn.linkshortener.exceptions.NotFoundException;
import com.panyukovnn.linkshortener.mapper.LinkMapper;
import com.panyukovnn.linkshortener.model.LinkInfo;
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
    private LinkMapper linkMapper;

    @Autowired
    public LinkInfoServiceImpl(LinkInfoProperty linkInfoProperty, LinkInfoRepository linkInfoRepository, LinkMapper linkMapper) {
        this.linkInfoProperty = linkInfoProperty;
        this.linkInfoRepository = linkInfoRepository;
        this.linkMapper = linkMapper;
    }

    public LinkInfoServiceImpl() {
    }

    @LogExecutionTime
    @Override
    public LinkInfoResponse getByShortLink(String shortLink) {
        LinkInfo linkInfo = linkInfoRepository.findByShortLink(shortLink)
            .orElseThrow(() -> new NotFoundException("Ссылка не найдена"));
        return linkMapper.toResponse(linkInfo);
    }

    @LogExecutionTime
    @Override
    public List<LinkInfoResponse> findByFilter() {
        return linkInfoRepository.findAll()
            .stream()
            .map(linkMapper::toResponse)
            .toList();
    }

    @LogExecutionTime
    @Override
    public LinkInfoResponse createLinkInfo(CreateShortLinkRequest request) {
        String shortLink = RandomStringUtils.randomAlphanumeric(linkInfoProperty.shortLinkLength());
        LinkInfo linkInfo = linkMapper.fromCreateRequest(request, shortLink);
        LinkInfo savedLinkInfo = linkInfoRepository.save(linkInfo);

        return linkMapper.toResponse(savedLinkInfo);
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
        return linkMapper.toResponse(updatedLinkInfo);
    }

    @LogExecutionTime
    @Override
    public void deleteById(UUID id) {
        linkInfoRepository.deleteById(id);
    }

}
