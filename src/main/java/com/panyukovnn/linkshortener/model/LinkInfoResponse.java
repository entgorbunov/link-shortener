package com.panyukovnn.linkshortener.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class LinkInfoResponse {

    private String link;
    private LocalDateTime endTime;
    private String description;
    private Boolean active;
    private UUID id;
    private String shortLink;
    private Long openingCount;
}
