package com.panyukovnn.linkshortener.service;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.UpdateShortLinkRequest;
import com.panyukovnn.linkshortener.model.LinkInfoResponse;

import java.util.List;
import java.util.UUID;

public interface LinkInfoService {

    LinkInfoResponse getByShortLink(String shortLink);

    List<LinkInfoResponse> findByFilter();

    LinkInfoResponse createLinkInfo(CreateShortLinkRequest request);

    LinkInfoResponse updateLinkInfo(UpdateShortLinkRequest request);

    void deleteById(UUID id);
}
