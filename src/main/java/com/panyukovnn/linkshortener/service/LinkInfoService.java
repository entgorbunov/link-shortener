package com.panyukovnn.linkshortener.service;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.model.LinkInfoResponse;

import java.util.List;

public interface LinkInfoService {

    LinkInfoResponse getByShortLink(String shortLink);

    List<LinkInfoResponse> findByFilter();

    LinkInfoResponse createLinkInfo(CreateShortLinkRequest request);
}
