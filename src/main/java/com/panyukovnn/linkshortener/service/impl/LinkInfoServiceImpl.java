package com.panyukovnn.linkshortener.service.impl;

import com.panyukovn.annotation.LogExecutionTime;
import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.FilterLinkInfoRequest;
import com.panyukovnn.linkshortener.dto.LinkInfoResponse;
import com.panyukovnn.linkshortener.dto.PageableRequest;
import com.panyukovnn.linkshortener.dto.SortRequest;
import com.panyukovnn.linkshortener.dto.UpdateShortLinkRequest;
import com.panyukovnn.linkshortener.exceptions.NotFoundException;
import com.panyukovnn.linkshortener.mapper.LinkMapper;
import com.panyukovnn.linkshortener.model.LinkInfo;
import com.panyukovnn.linkshortener.properties.LinkInfoProperty;
import com.panyukovnn.linkshortener.repository.LinkInfoRepository;
import com.panyukovnn.linkshortener.service.LinkInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    public Page<LinkInfoResponse> findByFilter(FilterLinkInfoRequest filterLinkInfoRequest) {
        PageableRequest page = filterLinkInfoRequest.getPage();

        Sort sort = buildSort(page.getSorts());

        Pageable pageable = PageRequest.of(
            page.getNumber() - 1,
            page.getSize(),
            sort
        );

        return linkInfoRepository.findByFilter(
                filterLinkInfoRequest.getLinkPart(),
                filterLinkInfoRequest.getEndTimeFrom(),
                filterLinkInfoRequest.getEndTimeTo(),
                filterLinkInfoRequest.getDescriptionPart(),
                filterLinkInfoRequest.getActive(),
                pageable
            )
            .map(linkMapper::toResponse);
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

    private Sort buildSort(@Valid List<SortRequest> sorts) {
        if (CollectionUtils.isEmpty(sorts)) {
            return Sort.unsorted();
        }

        List<Sort.Order> orders = sorts.stream()
            .map(sort -> new Sort.Order(
                Sort.Direction.valueOf(sort.getDirection()),
                sort.getField()
            ))
            .toList();

        return Sort.by(orders);
    }

}
