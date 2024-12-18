package com.panyukovnn.linkshortener.mapper;

import com.panyukovnn.linkshortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkshortener.dto.LinkInfoResponse;
import com.panyukovnn.linkshortener.model.LinkInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LinkMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "openingCount", constant = "0L")
    LinkInfo fromCreateRequest(CreateShortLinkRequest request, String shortLink);

    LinkInfoResponse toResponse(LinkInfo linkInfo);
}
