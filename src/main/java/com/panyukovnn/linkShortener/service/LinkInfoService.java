package com.panyukovnn.linkShortener.service;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkShortener.model.LinkInfoResponse;

import java.util.List;

public interface LinkInfoService {

    LinkInfoResponse getByShortLink(String shortLink);

    List<LinkInfoResponse> findByFilter();

    LinkInfoResponse createLinkInfo(CreateShortLinkRequest request);
}
