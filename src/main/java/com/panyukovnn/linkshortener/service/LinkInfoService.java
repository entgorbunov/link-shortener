package com.panyukovnn.linkshortener.service;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.model.LinkInfoResponse;

import java.util.List;
import java.util.Optional;

public interface LinkInfoService {

    Optional<LinkInfoResponse> getByShortLink(String shortLink);

    List<LinkInfoResponse> findByFilter();

    LinkInfoResponse createLinkInfo(CreateShortLinkRequest request);
}
