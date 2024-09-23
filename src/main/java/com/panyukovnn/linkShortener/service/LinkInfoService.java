package com.panyukovnn.linkShortener.service;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkShortener.model.LinkInfo;

public interface LinkInfoService {

    LinkInfo createLinkInfo(CreateShortLinkRequest request);

    LinkInfo getByShortLink(String shortLink);
}
