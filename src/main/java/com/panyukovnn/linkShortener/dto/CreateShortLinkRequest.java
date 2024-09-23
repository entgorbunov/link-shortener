package com.panyukovnn.linkShortener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateShortLinkRequest {
    private String link;
    private LocalDateTime endTime;
    private String description;
    private Boolean active;

}
