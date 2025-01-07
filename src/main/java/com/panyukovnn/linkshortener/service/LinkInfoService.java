package com.panyukovnn.linkshortener.service;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.FilterLinkInfoRequest;
import com.panyukovnn.linkshortener.dto.LinkInfoResponse;
import com.panyukovnn.linkshortener.dto.UpdateShortLinkRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface LinkInfoService {

    LinkInfoResponse getByShortLink(String shortLink);

    List<LinkInfoResponse> findByFilter(FilterLinkInfoRequest filterLinkInfoRequest);

    LinkInfoResponse createLinkInfo(CreateShortLinkRequest request);

    LinkInfoResponse updateLinkInfo(UpdateShortLinkRequest request);

    void deleteById(UUID id);
}
