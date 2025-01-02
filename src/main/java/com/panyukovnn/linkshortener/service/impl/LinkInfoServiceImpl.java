package com.panyukovnn.linkshortener.service.impl;

import com.panyukovnn.linkshortener.beanpostprocessor.LogExecutionTime;
import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.FilterLinkInfoRequest;
import com.panyukovnn.linkshortener.dto.LinkInfoResponse;
import com.panyukovnn.linkshortener.dto.UpdateShortLinkRequest;
import com.panyukovnn.linkshortener.exceptions.NotFoundException;
import com.panyukovnn.linkshortener.mapper.LinkMapper;
import com.panyukovnn.linkshortener.model.LinkInfo;
import com.panyukovnn.linkshortener.properties.LinkInfoProperty;
import com.panyukovnn.linkshortener.repository.LinkInfoRepository;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkInfoServiceImpl implements LinkInfoService {

    private final LinkInfoProperty linkInfoProperty;
    private final LinkInfoRepository linkInfoRepository;
    private final LinkMapper linkMapper;


    @LogExecutionTime
    @Override
    public LinkInfoResponse getByShortLink(String shortLink) {
        LinkInfo linkInfo = linkInfoRepository.findActiveShortLink(shortLink, LocalDateTime.now())
            .orElseThrow(() -> new NotFoundException("Ссылка " + shortLink + " не найдена"));

        linkInfoRepository.incrementOpeningCountByShortLink(shortLink);

        return linkMapper.toResponse(linkInfo);
    }

    @LogExecutionTime
    @Override
    public List<LinkInfoResponse> findByFilter(FilterLinkInfoRequest filterLinkInfoRequest) {
        return linkInfoRepository.findByFilter(
                filterLinkInfoRequest.getLinkPart(),
                filterLinkInfoRequest.getEndTimeFrom(),
                filterLinkInfoRequest.getEndTimeTo(),
                filterLinkInfoRequest.getDescriptionPart(),
                filterLinkInfoRequest.getActive()
            )
            .stream()
            .map(linkMapper::toResponse)
            .toList();
    }

    @Override
    public List<LinkInfoResponse> findAll() {
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
        LinkInfo linkInfo = linkInfoRepository.findById(UUID.fromString(request.getId()))
            .orElseThrow(() -> new NotFoundException("Ссылка не найдена, id: " + request.getId()));

        linkInfo.setDescription(request.getDescription());
        linkInfo.setActive(request.getActive());
        linkInfo.setLink(request.getLink());
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
